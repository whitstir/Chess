package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class DataTests {

    private MySqlDataAccess sqlDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        sqlDao = new MySqlDataAccess();
        sqlDao.clear();
    }

    @Test
    public void clearSuccess() {

    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        UserData testUser = new UserData("whit", "123", "email@email.com");
        sqlDao.createUser(testUser);
        UserData user = sqlDao.getUser("whit");

        assertNotNull(user);
        assertTrue(BCrypt.checkpw("123", user.password()));
        assertEquals("whit", user.username());
        assertEquals("email@email.com", user.email());

    }

    @Test
    public void getUserFail() throws DataAccessException {
        UserData testResult = sqlDao.getUser("badUsername");

        assertNull(testResult);
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        UserData testUser = new UserData("whit", "123", "email@email.com");
        sqlDao.createUser(testUser);
        UserData user = sqlDao.getUser("whit");

        assertNotNull(user);
        assertEquals(testUser.username(), user.username());
        assertNotEquals(testUser.password(), user.password());
        assertEquals(testUser.email(), user.email());
        assertTrue(BCrypt.checkpw("123", user.password()));
    }

    @Test
    public void createUserFail() throws DataAccessException {
        UserData testUserOne = new UserData("whit", "123", "email@email.com");
        sqlDao.createUser(testUserOne);
        UserData testUserTwo = new UserData("whit", "12345", "email@gmail.com");

        assertThrows(DataAccessException.class, () -> sqlDao.createUser(testUserTwo));
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        AuthData testAuth = new AuthData("123", "whit");
        sqlDao.createAuth(testAuth);
        AuthData auth = sqlDao.getAuth("123");

        assertNotNull(auth);
        assertEquals(testAuth.authToken(), auth.authToken());
        assertEquals(testAuth.username(), auth.username());
    }

    @Test
    public void createAuthFail() throws DataAccessException {
        AuthData testAuthOne = new AuthData("123", "whit");
        sqlDao.createAuth(testAuthOne);
        AuthData testAuthTwo = new AuthData("123", "whitney");

        assertThrows(DataAccessException.class, () -> sqlDao.createAuth(testAuthTwo));
    }

    @Test
    public void getAuthSuccess() throws DataAccessException {
        AuthData testAuth = new AuthData("123", "whit");
        sqlDao.createAuth(testAuth);
        AuthData auth = sqlDao.getAuth("123");

        assertNotNull(auth);
        assertEquals(testAuth.username(), auth.username());
        assertEquals(testAuth.authToken(), auth.authToken());
    }

    @Test
    public void getAuthFail() throws DataAccessException {
        AuthData testResult = sqlDao.getAuth("badToken");

        assertNull(testResult);
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, "white", "black", "game1", game);
        sqlDao.createGame(gameData);
        GameData savedGame = sqlDao.listGames().iterator().next();

        assertNotNull(savedGame);
        assertNotNull(savedGame.game());
        assertEquals("white", savedGame.whiteUsername());
        assertEquals("black", savedGame.blackUsername());
        assertEquals("game1", savedGame.gameName());
    }

    @Test
    public void createGameFail() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, "white", "black", null, game);

        assertThrows(DataAccessException.class, () -> sqlDao.createGame(gameData));
    }

    @Test
    public void getGameSuccess() throws DataAccessException {

    }

    @Test
    public void getGameFail() throws DataAccessException {

    }

    @Test
    public void listGamesSuccess() throws DataAccessException {

    }

    @Test
    public void listGamesFail() throws DataAccessException {

    }

    @Test
    public void updateGameSuccess() throws DataAccessException {

    }

    @Test
    public void updateGameFail() throws DataAccessException {

    }
}
