package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;

import javax.xml.crypto.Data;

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
    public void createGameSuccessfully() throws DataAccessException  {
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



}
