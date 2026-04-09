package repl;

import chess.ChessMove;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

public class WebSocketFacade {
    private final WebSocketCommunicator communicator;

    public WebSocketFacade(String serverUrl, ServerMessageObserver observer) throws Exception {
        communicator = new WebSocketCommunicator(serverUrl, observer);
    }

    public void connect(String authToken, int gameID) throws Exception {
        communicator.send(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID));
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        communicator.send(new MakeMoveCommand(authToken, gameID, move));
    }

    public void leave(String authToken, int gameID) throws Exception {
        communicator.send(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
    }

    public void resign(String authToken, int gameID) throws Exception {
        communicator.send(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
    }

    public void close() throws Exception {
        communicator.close();
    }
}