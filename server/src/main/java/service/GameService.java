package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;

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
            throw new DataAccessException("");
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
}
