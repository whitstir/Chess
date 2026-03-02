package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private MemoryDataAccess testDAO;
    private RegisterRequest testUser;
    private UserService testService;

    @BeforeEach
    public void setup() {
        testDAO = new MemoryDataAccess();
        testUser = new RegisterRequest("whitney", "12345", "whitstir@byu.edu");
        testService = new UserService(testDAO);
    }


    @Test
    public void createUserPass() throws DataAccessException {
        RegisterResult testResult = testService.registerUser(testUser);
        UserData storedUser = testDAO.getUser("whitney");
        AuthData storedAuth = testDAO.getAuth(testResult.authToken());

        assertEquals("whitney", testResult.username());
        assertNotNull(testResult.authToken());

        assertNotNull(storedUser);
        assertEquals("whitney", storedUser.username());

        assertNotNull(storedAuth);
        assertEquals("whitney", storedAuth.username());

        assertNotNull(storedUser);
        assertEquals(testUser.username(), storedUser.username());
    }

    @Test
    public void createUserFail() throws DataAccessException {
        //tries to create duplicate user
        testService.registerUser(testUser);
        assertThrows(DataAccessException.class, () -> {
            testService.registerUser(testUser);
        });
    }

}
