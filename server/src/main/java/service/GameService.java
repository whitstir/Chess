package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;

import java.util.Collection;
import java.util.UUID;

public class GameService {

    private final DataAccess dao;
    private int nextGameID = 1;

    public GameService(DataAccess dao) {
        this.dao = dao;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public CreateGameResult createGame(CreateGameRequest gameRequest) throws DataAccessException {
        if (gameRequest.authToken() == null || dao.getAuth(gameRequest.authToken()) == null) {
            throw new DataAccessException("Unauthorized");
        }
        if (gameRequest.gameName() == null || gameRequest.gameName().isEmpty()) {
            throw new DataAccessException("No game found");
        }
        int newID = nextGameID++;
        GameData newGame = new GameData(newID, null, null, gameRequest.gameName(),
                new ChessGame());
        dao.createGame(newGame);
        return new CreateGameResult(newID);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        AuthData auth = dao.getAuth(joinGameRequest.authToken());
        GameData game = dao.getGame(joinGameRequest.gameID());

        if (joinGameRequest.authToken() == null || dao.getAuth(joinGameRequest.authToken()) == null) {
            throw new DataAccessException("Unauthorized");
        }

        String username = auth.username();

        if (joinGameRequest.playerColor() == null || joinGameRequest.playerColor().isEmpty() ||
                !joinGameRequest.playerColor().equals("WHITE") && !joinGameRequest.playerColor().equals("BLACK")) {
            throw new DataAccessException("Invalid input");
        }
        if (game == null) {
            throw new DataAccessException("No game found");
        }
        if (joinGameRequest.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new DataAccessException("This color is already taken");
            }
            game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            if (game.blackUsername() != null) {
                throw new DataAccessException("This color is already taken");
            }
            game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        dao.updateGame(game);
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        AuthData auth = dao.getAuth(authToken);
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("Unauthorized");
        }
        if (auth == null) {
            throw new DataAccessException("Unauthorized");
        }
        return dao.listGames();
    }
}
