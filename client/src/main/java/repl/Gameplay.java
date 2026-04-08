package repl;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.out;

public class Gameplay implements ServerMessageObserver {
    private final Scanner scanner = new Scanner(System.in);
    private final WebSocketCommunicator webSocketCommunicator;
    private final String authToken;
    private final int gameID;
    private final ChessGame.TeamColor playerColor;
    private ChessGame currentGame;
    boolean enteredGame = false;

    public Gameplay(WebSocketCommunicator webSocketCommunicator, String authToken, int gameID,
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
                    makeMove(input);
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

    @Override
    public void onMessage(ServerMessage message) {
        out.println();
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            currentGame = ((LoadGameMessage) message).getGame().game();
            redrawBoard();
        } else if (message.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            out.println("[ERROR] " + ((ErrorMessage) message).getErrorMessage());
        } else if (message.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            out.println(((NotificationMessage) message).getMessage());
        }
        printPrompt();
    }

    private void makeMove(String[] input) {
        if (playerColor == null) {
            out.println("Observers can't make moves!");
            return;
        }
        if (input.length < 3) {
            out.println("For a normal move, please input 'move' <from> <to>");
            out.println("For a promotion move, please input 'move' <from> <to> <promotion piece>");
        }
        try {
            ChessPosition fromPosition = getPosition(input[1]);
            ChessPosition toPosition = getPosition(input[2]);
        } catch (Exception e) {
            out.println("Invalid move");
        }
    }

    private ChessPosition getPosition(String s) {
        if (s.length() != 2) {
            out.println("Invalid chess position");
        }
        char columnLetter = s.charAt(0);
        char rowNumber = s.charAt(1);
        int col;
        int row;
        col = switch (columnLetter) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> throw new IllegalArgumentException("Invalid column");
        };
        row = switch (rowNumber) {
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            default -> throw new IllegalArgumentException("Invalid row");
        };

        return new ChessPosition(row, col);
    }

    private void redrawBoard() {
        if (currentGame == null) {
            out.println("No game found");
            return;
        }
        ChessGame.TeamColor drawFrom;
        drawFrom = Objects.requireNonNullElse(playerColor, ChessGame.TeamColor.WHITE);
        BoardDrawing.drawBoard(currentGame, drawFrom);
    }

    private void printHelp() {
        out.println("help - with possible commands");
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
