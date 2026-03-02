import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.UserService;

import javax.xml.crypto.Data;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ServerTests {

    private MemoryDataAccess testDAO;
    private UserData testUser;

    @BeforeEach
    public void setup() {
        testDAO = new MemoryDataAccess();
        testUser = new UserData("whitney", "12345", "whitstir@byu.edu");
    }

    @Test
    public void clearData() throws DataAccessException {
        ClearService testService = new ClearService(testDAO);
        testDAO.createUser(testUser);
        testService.clear();
        assertNull(testDAO.getUser(testUser.username()));
    }


}
