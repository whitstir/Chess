package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import model.AuthData;
import service.ClearService;
import service.GameService;
import service.UserService;
import io.javalin.http.Context;

import javax.xml.crypto.Data;
import java.util.Map;

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

    private void exceptionHandler(DataAccessException ex, Context ctx) {

    }

    private void clear(Context ctx) throws DataAccessException {
        clearService.clear();
        ctx.status(200);
        ctx.result("{}");
    }

    private void register(Context ctx) throws DataAccessException {

    }

    private void login(Context ctx) throws DataAccessException {

    }

    private void logout(Context ctx) throws DataAccessException {

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
