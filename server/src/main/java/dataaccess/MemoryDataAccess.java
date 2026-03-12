package dataaccess;

import model.*;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<String, AuthData> authTokens = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public void clear() {
        users.clear();
        games.clear();
        authTokens.clear();
    }

    public void createUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("Username already exists");
        }
        users.put(user.username(), user);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public int createGame(GameData game) {
        games.put(game.gameID(), game);
        return 0;
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public void updateGame(GameData game) throws DataAccessException {
        if (!games.containsKey(game.gameID())) {
            throw new DataAccessException("Game not found");
        }
        games.put(game.gameID(), game);
    }

    public void createAuth(AuthData auth) {
        authTokens.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }
}
