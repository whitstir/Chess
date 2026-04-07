package repl;

import chess.ChessGame;

import java.util.Scanner;

import static java.lang.System.out;

public class GameplayClient implements ServerMessageObserver {
    private final Scanner scanner = new Scanner(System.in);
    private final WebSocketCommunicator webSocketCommunicator;
    private final String authToken;
    private final int gameID;
    private final ChessGame.TeamColor playerColor;
    private ChessGame currentGame;
    boolean enteredGame = false;

    public GameplayClient(WebSocketCommunicator webSocketCommunicator, String authToken, int gameID,
                          ChessGame.TeamColor playerColor) {
        this.webSocketCommunicator = webSocketCommunicator;
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public void run() {
        boolean enteredGame = true;
        while (enteredGame) {
            String[] input = getInput();
            switch (input[0].toLowerCase()) {
                case "help" -> {
                    printHelp();
                }
                case "redraw" -> {
                    redrawBoard();
                }
                case "leave" -> {
                    leaveGame();
                    enteredGame = false;
                }
                case "move" -> {
                    makeMove();
                }
                case "resign" -> {
                    resignGame();
                }
                case "highlight" -> {
                    highlightMoves();
                }
                default -> out.println("Unknown command. Type help.");
            }
        }
    }
    private String[] getInput() {
        if (enteredGame) {
            out.print("\n[LOGGED IN] >>> ");
        } else {
            out.print("\n[LOGGED OUT] >>> ");
        }
        return scanner.nextLine().split(" ");
    }


}
