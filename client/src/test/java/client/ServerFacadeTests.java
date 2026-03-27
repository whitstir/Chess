package client;

import model.GameData;
import org.junit.jupiter.api.*;
import repl.ServerFacade;
import server.Server;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        serverFacade.clear();
    }

    @Test
    void registerSuccess() throws Exception {
        var authData = serverFacade.register("whit","password", "email@email.com");

        assertNotNull(authData.authToken());
    }

    @Test
    void registerFail() throws Exception {
        serverFacade.register("whit","password", "email@email.com");

        assertThrows(RuntimeException.class, () -> {
            serverFacade.register("whit","password", "email@email.com");
        });
    }

    @Test
    void loginSuccess() throws Exception {
        serverFacade.register("whit","password", "email@email.com");
        var authData = serverFacade.login("whit", "password");

        assertNotNull(authData.authToken());
        assertEquals("whit", authData.username());
    }

    @Test
    void loginFail() throws Exception {
        serverFacade.register("whit","password", "email@email.com");

        assertThrows(RuntimeException.class, () -> {
            serverFacade.register("whit","badPassword", "email@email.com");
        });
    }

    @Test
    void logoutSuccess() throws Exception {
        serverFacade.register("whit","password", "email@email.com");
        serverFacade.login("whit", "password");

        assertDoesNotThrow(() -> {serverFacade.logout();});
        assertNull(serverFacade.getAuthToken());
    }

    @Test
    void logoutFail() throws Exception {
        serverFacade.setAuthToken(null);
        assertThrows(RuntimeException.class, () -> {serverFacade.logout();});
    }

    @Test
    void createGameSuccess() throws Exception {
        serverFacade.register("whit","password", "email@email.com");
        int gameID = serverFacade.createGame("game1");
        assertTrue(gameID > 0);
    }

    @Test
    void createGameFail() throws Exception {
        serverFacade.setAuthToken(null);
        assertThrows(RuntimeException.class, () -> {serverFacade.createGame("game1");});
    }

    @Test
    void listGamesSuccess() throws Exception {
        serverFacade.register("whit","password", "email@email.com");
        serverFacade.createGame("game1");
        serverFacade.createGame("game2");
        Collection<GameData> games = serverFacade.listGames();
        assertEquals(2, games.size());
    }

    @Test
    void listGamesFail() throws Exception {
        serverFacade.setAuthToken(null);
        assertThrows(RuntimeException.class, () -> {serverFacade.listGames();});
    }

    @Test
    void joinGameSuccess() throws Exception {
        serverFacade.register("whit","password", "email@email.com");
        int gameID = serverFacade.createGame("game1");
        assertDoesNotThrow(() -> {serverFacade.joinGame("WHITE", gameID);});
    }

    @Test
    void joinGameFail() throws Exception {
        serverFacade.register("whit","password", "email@email.com");
        int gameID = serverFacade.createGame("game1");
        assertThrows(RuntimeException.class, () -> {serverFacade.joinGame("WHITE", 10);});
    }
}
