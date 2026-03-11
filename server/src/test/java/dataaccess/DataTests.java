package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class DataTests {

    private MySqlDataAccess sqlDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        sqlDao = new MySqlDataAccess();
        sqlDao.clear();
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        UserData testUser = new UserData("whit", "123", "email@email.com");
        UserData testResult = sqlDao.getUser("whit");
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
}
