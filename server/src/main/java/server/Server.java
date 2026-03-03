package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import io.javalin.http.Context;
import service.requests.CreateGameRequest;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.RegisterResult;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Server {

    private final Javalin javalin;
    private final DataAccess dao = new MemoryDataAccess();
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
        if (exception.getMessage().equals("User already exists") || exception.getMessage().equals("This color is already taken")) {
            status = 403;
            message = "Error: already taken";
        } else if (exception.getMessage().equals("Invalid input") || exception.getMessage().equals("No game found")) {
            status = 400;
            message = "Error: bad request";
        } else if (exception.getMessage().equals("Unauthorized") || exception.getMessage().equals("Invalid auth token") ||
                exception.getMessage().equals("Missing auth token") || exception.getMessage().equals("User does not exist") ||
                exception.getMessage().equals("Incorrect password")) {
            status = 401;
            message = "Error: unauthorized";
        } else {
            status = 500;
            message = "Error: " + exception.getMessage();
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

    }

    private void createGame(Context ctx) throws DataAccessException {

    }

    private void joinGame(Context ctx) throws DataAccessException {

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
