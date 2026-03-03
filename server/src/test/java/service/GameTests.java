package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    private MemoryDataAccess testDAO;
    private GameService testService;

    @BeforeEach
    public void setup() {
        testDAO = new MemoryDataAccess();
        testService = new GameService(testDAO);
    }

    //My positive createGame test

    @Test
    public void createGameSuccessfully() throws DataAccessException {
        testDAO.createUser(new UserData("whitney", "12345", "email@email.com"));
        String token = UserService.generateToken();
        testDAO.createAuth(new AuthData(token, "whitney"));
        CreateGameRequest testRequest = new CreateGameRequest("game1", token);
        CreateGameResult testResult = testService.createGame(testRequest);

        assertNotNull(testResult);
        assertNotNull(testDAO.getGame(testResult.gameID()));
        assertEquals(1, testResult.gameID());
    }

    //My negative createGame test

    @Test
    public void createGameBadToken() throws DataAccessException {
        CreateGameRequest testRequest = new CreateGameRequest("game1", "badToken");
        assertThrows(DataAccessException.class, () -> testService.createGame(testRequest));
    }

    //My positive joinGame test

    @Test
    public void joinGameSuccessfully() throws DataAccessException {
        testDAO.createUser(new UserData("whitney", "12345", "email@email.com"));
        String token = UserService.generateToken();
        testDAO.createAuth(new AuthData(token, "whitney"));
        GameData game = new GameData(1, null, null, "game1", new ChessGame());
        testDAO.createGame(game);
        JoinGameRequest testRequest = new JoinGameRequest(token, "WHITE", 1);
        testService.joinGame(testRequest);
        GameData updateGame = testDAO.getGame(1);

        assertNotNull(testDAO.getGame(testRequest.gameID()));
        assertEquals("whitney", updateGame.whiteUsername());
    }

    //My negative joinGame test

    @Test
    public void joinGameDoesNotExist() throws DataAccessException {
        testDAO.createUser(new UserData("whitney", "12345", "email@email.com"));
        String token = UserService.generateToken();
        testDAO.createAuth(new AuthData(token, "whitney"));
        JoinGameRequest testRequest = new JoinGameRequest(token, "WHITE", 5);

        assertThrows(DataAccessException.class, () -> testService.joinGame(testRequest));
    }

    //My positive listGames test

    @Test
    public void listGamesSuccessfully() throws DataAccessException {
        testDAO.createUser(new UserData("whitney", "12345", "email@email.com"));
        String token = UserService.generateToken();
        testDAO.createAuth(new AuthData(token, "whitney"));
        testDAO.createGame(new GameData(1, null, null, "game1", null));
        testDAO.createGame(new GameData(2, null, null, "game2", null));
        testDAO.createGame(new GameData(3, null, null, "game3", null));
        Collection<GameData> games = testService.listGames(token);

        assertEquals(3, games.size());
    }

    //My negative listGames test

    @Test
    public void listGamesBadToken() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> testService.listGames("badToken"));
    }
}
