package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTests {

    private MemoryDataAccess testDAO;
    private UserData testUser;
    private ClearService testService;

    @BeforeEach
    public void setup() {
        testDAO = new MemoryDataAccess();
        testUser = new UserData("whitney", "12345", "whitstir@byu.edu");
        testService = new ClearService(testDAO);
    }

    @Test
    public void clearData() throws DataAccessException {
        testDAO.createUser(testUser);
        testService.clear();
        assertNull(testDAO.getUser(testUser.username()));
    }

}
