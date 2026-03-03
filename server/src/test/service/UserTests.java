package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.mainServices.UserService;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private MemoryDataAccess testDAO;
    private UserService testService;

    @BeforeEach
    public void setup() {
        testDAO = new MemoryDataAccess();
        testService = new UserService(testDAO);
    }

    //My positive create/register user test

    @Test
    public void createUserSuccessfully() throws DataAccessException {
        RegisterRequest testUser = new RegisterRequest("whitney", "12345", "email@email.com");
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

    //My negative create/register user tests

    @Test
    public void createDuplicateUser() throws DataAccessException {
        RegisterRequest testUser = new RegisterRequest("whitney", "12345", "email@email.com");
        testService.registerUser(testUser);

        assertThrows(DataAccessException.class, () -> {
            testService.registerUser(testUser);
        });
    }

    @Test
    public void createUserMissingFields() throws DataAccessException {
        RegisterRequest missingUsername = new RegisterRequest(null, "12345", "email@email.com");
        RegisterRequest missingPassword = new RegisterRequest("whitney", null, "email@email.com");
        RegisterRequest missingEmail = new RegisterRequest("whitney", "12345", null);
        RegisterRequest missingEverything = new RegisterRequest(null, null, null);

        assertThrows(DataAccessException.class, () -> testService.registerUser(missingUsername));
        assertThrows(DataAccessException.class, () -> testService.registerUser(missingPassword));
        assertThrows(DataAccessException.class, () -> testService.registerUser(missingEmail));
        assertThrows(DataAccessException.class, () -> testService.registerUser(missingEverything));
    }

    //My positive login test

    @Test
    public void loginSuccessfully() throws DataAccessException {
        testDAO.createUser(new UserData("whitney", "12345", "email@email.com"));
        LoginRequest testUser = new LoginRequest("whitney", "12345");
        LoginResult testResult = testService.login(testUser);

        assertEquals("whitney", testResult.username());
        assertNotNull(testResult.authToken());
        assertNotNull(testDAO.getAuth(testResult.authToken()));
    }

    //My negative login tests

    @Test
    public void loginMissingFields() throws DataAccessException {
        LoginRequest missingUsername = new LoginRequest(null, "12345");
        LoginRequest missingPassword = new LoginRequest("whitney", null);

        assertThrows(DataAccessException.class, () -> testService.login(missingUsername));
        assertThrows(DataAccessException.class, () -> testService.login(missingPassword));
    }

    @Test
    public void loginWrongPassword() throws DataAccessException {
        testDAO.createUser(new UserData("whitney", "12345", "email@email.com"));
        LoginRequest testRequest = new LoginRequest("whitney", "00000");

        assertThrows(DataAccessException.class, () -> testService.login(testRequest));
    }

    @Test
    public void loginUserNotFound() throws DataAccessException {
        LoginRequest testRequest = new LoginRequest("whitney", "12345");

        assertThrows(DataAccessException.class, () -> testService.login(testRequest));
    }

    //My positive logout test

    @Test
    public void logoutSuccessfully() throws DataAccessException {
        testDAO.createUser(new UserData("whitney", "12345", "email@email.com"));
        String token = UserService.generateToken();
        testDAO.createAuth(new AuthData(token, "whitney"));
        LogoutRequest testRequest = new LogoutRequest(token);
        testService.logout(testRequest);

        assertNull(testDAO.getAuth(token));
    }

    //My negative logout test

    @Test
    public void logoutMissingToken() throws DataAccessException {
        LogoutRequest testRequest = new LogoutRequest(null);
        assertThrows(DataAccessException.class, () -> testService.logout(testRequest));
    }
}
