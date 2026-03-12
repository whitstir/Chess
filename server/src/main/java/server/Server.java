package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import io.javalin.*;
import model.GameData;
import service.ClearService;
import service.GameService;
import service.UserService;
import io.javalin.http.Context;
import service.requests.*;
import service.results.CreateGameResult;
import service.results.LoginResult;
import service.results.RegisterResult;

import java.util.Collection;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final DataAccess dao;
    {
        try {
            dao = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private final ClearService clearService = new ClearService(dao);
    private final UserService userService = new UserService(dao);
    private final GameService gameService = new GameService(dao);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clear)
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .exception(DataAccessException.class, this::exceptionHandler);
    }

    private void exceptionHandler(DataAccessException exception, Context ctx) {
        int status;
        String message;

        switch (exception.getMessage()) {
            case "Bad request" -> {
                status = 400;
                message = "Error: bad request";
            }
            case "Already taken" -> {
                status = 403;
                message = "Error: already taken";
            }
            case "Unauthorized" -> {
                status = 401;
                message = "Error: unauthorized";
            }
            default -> {
                status = 500;
                message = "Error: " + exception.getMessage();
            }
        }

        ctx.status(status);
        ctx.result(new Gson().toJson(Map.of("message", message)));
    }

    private void clear(Context ctx) throws DataAccessException {
        clearService.clear();

        ctx.status(200);
        ctx.result("{}");
    }

    private void register(Context ctx) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.registerUser(registerRequest);

        ctx.status(200);
        ctx.result(new Gson().toJson(result));
    }

    private void login(Context ctx) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(loginRequest);

        ctx.status(200);
        ctx.result(new Gson().toJson(result));
    }

    private void logout(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        userService.logout(new LogoutRequest(authToken));

        ctx.status(200);
        ctx.result("{}");
    }

    private void listGames(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        Collection<GameData> games = gameService.listGames(authToken);

        ctx.status(200);
        ctx.result(new Gson().toJson(Map.of("games", games)));

    }

    private void createGame(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        createGameRequest = new CreateGameRequest(createGameRequest.gameName(), authToken);
        CreateGameResult result = gameService.createGame(createGameRequest);

        ctx.status(200);
        ctx.result(new Gson().toJson(result));
    }

    private void joinGame(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        joinGameRequest = new JoinGameRequest(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());
        gameService.joinGame(joinGameRequest);

        ctx.status(200);
        ctx.result("{}");
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
