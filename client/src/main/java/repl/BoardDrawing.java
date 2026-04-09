package repl;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

import java.util.Collection;

public class BoardDrawing {
    public static void drawBoard(ChessGame game, ChessGame.TeamColor side) {
        if (side == ChessGame.TeamColor.WHITE) {
            drawWhiteBoard(game);
        } else {
            drawBlackBoard(game);
        }
    }

    private static void drawWhiteBoard(ChessGame game) {
        printColumnsWhite();
        for (int row = 8; row > 0; row--) {
            printBorderSquare(" " + row + " ");
            for (int col = 1; col <= 8; col++) {
                makeSquare(game, row, col);
            }
            printBorderSquare(" " + row + " ");
            System.out.println();
        }
        printColumnsWhite();
    }

    private static void drawBlackBoard(ChessGame game) {
        printColumnsBlack();
        for (int row = 1; row <= 8; row++) {
            printBorderSquare(" " + row + " ");
            for (int col = 8; col > 0; col--) {
                makeSquare(game, row, col);
            }
            printBorderSquare(" " + row + " ");
            System.out.println();
        }
        printColumnsBlack();
    }

    private static void makeSquare(ChessGame game, int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece chessPiece = game.getBoard().getPiece(position);
        colorSquare(row, col);
        if (chessPiece == null) {
            System.out.print(EscapeSequences.EMPTY);
        } else {
            System.out.print(getPiece(chessPiece));
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private static String getPiece(ChessPiece chessPiece) {
        return switch (chessPiece.getTeamColor()) {
            case WHITE -> getWhitePiece(chessPiece);
            case BLACK -> getBlackPiece(chessPiece);
        };
    }

    private static String getBlackPiece(ChessPiece chessPiece) {
        return switch (chessPiece.getPieceType()) {
            case KING -> EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_KING;
            case QUEEN -> EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_QUEEN;
            case BISHOP -> EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_KNIGHT;
            case ROOK -> EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_ROOK;
            case PAWN -> EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_PAWN;
        };
    }

    private static String getWhitePiece(ChessPiece chessPiece) {
        return switch (chessPiece.getPieceType()) {
            case KING -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.WHITE_KING;
            case QUEEN -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.WHITE_QUEEN;
            case BISHOP -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.WHITE_BISHOP;
            case KNIGHT -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.WHITE_KNIGHT;
            case ROOK -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.WHITE_ROOK;
            case PAWN -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.WHITE_PAWN;
        };
    }

    private static void colorSquare(int row, int col) {
        boolean lightSquare = ((row + col) % 2 == 0);
        if (lightSquare) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        }
    }

    private static void printBorderSquare(String text) {
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(text);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private static void printColumnsWhite() {
        printBorderSquare("   ");
        for (char c = 'a'; c <= 'h'; c++) {
            printBorderSquare(" " + c + " ");
        }
        printBorderSquare("   ");
        System.out.println();
    }

    private static void printColumnsBlack() {
        printBorderSquare("   ");
        for (char c = 'h'; c >= 'a'; c--) {
            printBorderSquare(" " + c + " ");
        }
        printBorderSquare("   ");
        System.out.println();
    }

    public static void drawHighlights(ChessGame game, ChessGame.TeamColor drawFrom, ChessPosition position,
                                      Collection<ChessMove> possibleMoves) {
    }
}