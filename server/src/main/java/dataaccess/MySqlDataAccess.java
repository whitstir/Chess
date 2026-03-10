package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class MySqlDataAccess implements DataAccess {
    public void clear() {

    }

    public void createUser(UserData user) throws DataAccessException {

    }

    public UserData getUser(String username) {

    }

    public void createGame(GameData game) {

    }

    public GameData getGame(int gameID) {

    }

    public Collection<GameData> listGames() {

    }

    public void updateGame(GameData game) throws DataAccessException {

    }

    public void createAuth(AuthData auth) {

    }

    public AuthData getAuth(String authToken) {

    }

    public void deleteAuth(String authToken) {

    }

}
