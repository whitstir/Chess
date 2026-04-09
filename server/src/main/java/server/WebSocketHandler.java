package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import static java.lang.System.out;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final DataAccess dao;
    private final Gson gson = new Gson();
    private static final String GAME_NOT_FOUND_ERROR = "Error: game not found";
    private static final String NOT_YOUR_TURN_ERROR = "Error: it is not your turn";

    public WebSocketHandler(DataAccess dao) {
        this.dao = dao;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("[DEBUG SERVER] WsConnectContext fired - connection opened");
        out.println("WebSocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        out.println("WebSocket closed");
        connections.removeFromAll(ctx);
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        System.out.println("[DEBUG SERVER] handleMessage raw: " + ctx.message());
        try {
            UserGameCommand action = gson.fromJson(ctx.message(), UserGameCommand.class);
            var auth = dao.getAuth(action.getAuthToken());
            String side;
            if (auth == null) {
                sendError(ctx, "Error: invalid auth token");
                return;
            }
            String username = auth.username();
            if (action.getCommandType() == UserGameCommand.CommandType.CONNECT) {
                handleConnect(ctx, action, username);
            } else if (action.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
                MakeMoveCommand makeMoveCommand = gson.fromJson(ctx.message(), MakeMoveCommand.class);
                handleMakeMove(ctx, makeMoveCommand, username);
            } else if (action.getCommandType() == UserGameCommand.CommandType.LEAVE) {
                handleLeave(ctx, action, username);
            } else if (action.getCommandType() == UserGameCommand.CommandType.RESIGN) {
                handleResign(ctx, action, username);
            }
        } catch (Exception e) {
            sendError(ctx, "Error: " + e.getMessage());
        }
    }

    private void handleConnect(WsContext ctx, UserGameCommand userGameCommand, String username)
            throws DataAccessException {
        System.out.println("[DEBUG SERVER] handleConnect called for user: " + username + " gameID: " + userGameCommand.getGameID());
        int gameID = userGameCommand.getGameID();
        GameData game = dao.getGame(gameID);
        System.out.println("[DEBUG SERVER] game found: " + (game != null));
        String role;
        if (game == null) {
            sendError(ctx, GAME_NOT_FOUND_ERROR);
            return;
        }
        connections.add(gameID, ctx);
        ctx.send(gson.toJson(new LoadGameMessage(game)));
        if (username.equals(game.whiteUsername())) {
            role = "white";
        } else if (username.equals(game.blackUsername())) {
            role = "black";
        } else {
            role = "observer";
        }
        connections.broadcast(gameID, ctx, gson.toJson(new NotificationMessage(username + " joined as " + role)));
    }

    private void handleMakeMove(WsContext ctx, MakeMoveCommand makeMoveCommand, String username)
            throws DataAccessException {
        int gameID = makeMoveCommand.getGameID();
        GameData game = dao.getGame(gameID);
        chess.ChessGame.TeamColor opponent;
        String opponentName;
        boolean isWhite = username.equals(game.whiteUsername());
        boolean isBlack = username.equals(game.blackUsername());
        chess.ChessGame.TeamColor turn = game.game().getTeamTurn();
        if (game.game().isGameOver()) {
            sendError(ctx, "Error: game is already over");
            return;
        }
        if (!isWhite && !isBlack) {
            sendError(ctx, "Error: observers cannot make moves");
            return;
        }
        if (turn == chess.ChessGame.TeamColor.WHITE && !isWhite) {
            sendError(ctx, NOT_YOUR_TURN_ERROR);
            return;
        }
        if (turn == chess.ChessGame.TeamColor.BLACK && !isBlack) {
            sendError(ctx, NOT_YOUR_TURN_ERROR);
            return;
        }
        try {
            game.game().makeMove(makeMoveCommand.getMove());
        } catch (chess.InvalidMoveException e) {
            sendError(ctx, "Error: invalid move");
            return;
        }
        dao.updateGame(game);
        connections.broadcastAll(gameID, gson.toJson(new LoadGameMessage(game)));
        connections.broadcast(gameID, ctx, gson.toJson(new NotificationMessage(
                username + " moved " + makeMoveCommand.getMove().toString())));

        if (turn == chess.ChessGame.TeamColor.WHITE) {
            opponent = chess.ChessGame.TeamColor.BLACK;
            opponentName = game.blackUsername();
        } else {
            opponent = chess.ChessGame.TeamColor.WHITE;
            opponentName = game.whiteUsername();
        }
        if (game.game().isInCheckmate(opponent)) {
            connections.broadcastAll(gameID, gson.toJson(new NotificationMessage(
                    opponentName + " is in checkmate! Game over.")));
        } else if (game.game().isInCheck(opponent)) {
            connections.broadcastAll(gameID, gson.toJson(new NotificationMessage(
                    opponentName + " is in check!")));
        } else if (game.game().isInStalemate(opponent))
            connections.broadcastAll(gameID, gson.toJson(new NotificationMessage(
                    "Stalemate! The game is a draw.")));
    }

    private void handleLeave(WsContext ctx, UserGameCommand userGameCommand, String username)
            throws DataAccessException {
        int gameID = userGameCommand.getGameID();
        GameData game = dao.getGame(gameID);
        if (game != null) {
            if (username.equals(game.whiteUsername())) {
                dao.updateGame(new GameData(
                        game.gameID(), null, game.blackUsername(),
                        game.gameName(), game.game()));
            } else if (username.equals(game.blackUsername())) {
                dao.updateGame(new GameData(
                        game.gameID(), game.whiteUsername(), null,
                        game.gameName(), game.game()));
            }
        }
        connections.remove(gameID, ctx);
        connections.broadcast(gameID, ctx, gson.toJson(
                new NotificationMessage(username + " left the game")));
    }

    private void handleResign(WsContext ctx, UserGameCommand userGameCommand, String username)
            throws DataAccessException {
        int gameID = userGameCommand.getGameID();
        GameData game = dao.getGame(gameID);

        if (game == null) {
            sendError(ctx, GAME_NOT_FOUND_ERROR);
            return;
        }
        if (game.game().isGameOver()) {
            sendError(ctx, "Error: game is already over");
            return;
        }
        if (!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
            sendError(ctx, "Error: observers can't resign");
            return;
        }
        game.game().setGameOver(true);
        dao.updateGame(game);
        connections.broadcastAll(gameID, gson.toJson(new NotificationMessage(username + " resigned. Game over.")));
    }

    private void sendError(WsContext ctx, String message) {
        ctx.send(gson.toJson(new ErrorMessage(message)));
    }
}
