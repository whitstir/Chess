package repl;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.out;

public class Gameplay implements ServerMessageObserver {
    private final Scanner scanner = new Scanner(System.in);
    private WebSocketCommunicator webSocketCommunicator;
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

    public void setWebSocketCommunicator(WebSocketCommunicator webSocketCommunicator) {
        this.webSocketCommunicator = webSocketCommunicator;
    }

    public void run() {
        enteredGame = true;
        while (enteredGame) {
            String[] input = getInput();
            switch (input[0].toLowerCase()) {
                case "help" -> {
                    printHelp();
                    printRedraw();
                    printLeave();
                    printMove();
                    printResign();
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
                    highlightMoves(input);
                }
                default -> out.println("Unknown command. Type help.");
            }
        }
    }

    private void resignGame() {
        out.print("Are you sure you want to resign? ");
        String answer = scanner.nextLine().toLowerCase();
        if (answer.equals("yes")) {
            try {
                webSocketCommunicator.send(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
            } catch (Exception e) {
                out.println("Could not resign.");
            }
        } else {
            out.println("Resign cancelled.");
        }

    }

    private void leaveGame() {
        try {
            webSocketCommunicator.send(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
            webSocketCommunicator.close();
        } catch (Exception e) {
            out.println("Couldn't leave the game." + e.getMessage());
        }
    }

    private void highlightMoves(String[] input) {
        if (input.length < 2) {
            printHighlights();
            return;
        }
        if (currentGame == null) {
            out.println("No game loaded.");
            return;
        }
        try {
            ChessGame.TeamColor drawFrom;
            ChessPosition position = getPosition(input[1]);
            Collection<ChessMove> possibleMoves = currentGame.validMoves(position);
            if (playerColor != null) {
                drawFrom = playerColor;
            } else {
                drawFrom = ChessGame.TeamColor.WHITE;
            }
            BoardDrawing.drawHighlights(currentGame, drawFrom, position, possibleMoves);
        } catch (Exception e) {
            out.println("Invalid square selected.");
        }
    }

    private void makeMove(String[] input) {
        if (playerColor == null) {
            out.println("Observers can't make moves!");
            return;
        }
        if (input.length < 3) {
            printMove();
        }
        try {
            ChessPosition fromPosition = getPosition(input[1]);
            ChessPosition toPosition = getPosition(input[2]);
            ChessPiece.PieceType promotion = null;

            if (input.length >= 4) {
                promotion = getPromotion(input[3]);
            }
            ChessMove move = new ChessMove(fromPosition, toPosition, promotion);
            webSocketCommunicator.send(new MakeMoveCommand(authToken, gameID, move));
        } catch (Exception e) {
            out.println("Invalid move: " + e.getMessage());
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

    private ChessPiece.PieceType getPromotion(String s) {
        return switch (s.toLowerCase()) {
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new IllegalArgumentException("Invalid promotion piece");
        };
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

    private void printHelp() { out.println("help - with possible commands"); }

    private void printRedraw() {
        out.println("redraw - redraws the chess board");
    }

    private void printLeave() {
        out.println("leave - exit the game");
    }

    private void printMove() { out.println("move <from> <to> <promotionpiece (optional)> - make a move"); }

    private void printResign() {
        out.println("resign - forfeit the game");
    }

    private void printHighlights() {
        out.println("highlight <square> - shows all legal moves of the selected piece");
    }

    private String[] getInput() {
        String side;
        if (playerColor != null) {
            side = playerColor.toString();
        } else {
            side = "OBSERVER";
        }
        out.print("\n[" + side + "] >>> ");
        return scanner.nextLine().split(" ");
    }

    @Override
    public void onMessage(ServerMessage message) {
        System.out.println("[DEBUG] onMessage called, type: " + message.getServerMessageType());
        out.println();
        String side;
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage lgm = (LoadGameMessage) message;
            System.out.println("[DEBUG CLIENT] game object: " + lgm.getGame());
            System.out.println("[DEBUG CLIENT] chess game: " + lgm.getGame().game());
            currentGame = lgm.getGame().game();
            redrawBoard();
        } else if (message.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            out.println("[ERROR] " + ((ErrorMessage) message).getErrorMessage());
        } else if (message.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            out.println(((NotificationMessage) message).getNotificationMessage());
        }
        if (playerColor != null) {
            side = playerColor.toString();
        } else {
            side = "OBSERVER";
        }
        out.print("\n[" + side + "] >>> ");
    }

}
