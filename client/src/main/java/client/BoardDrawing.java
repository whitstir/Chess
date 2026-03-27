package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

import java.util.concurrent.ScheduledExecutorService;


public class BoardDrawing {
    public void drawBoard(ChessGame game, ChessGame.TeamColor side) {
        if (side == ChessGame.TeamColor.WHITE) {
            drawWhiteBoard(game);
        } else {
            drawBlackBoard(game);
        }
    }

    private  void drawWhiteBoard(ChessGame game) {
        printColumnsWhite();
        for (int row = 8; row > 0; row--) {
            System.out.print(row + " ");
            for (int col = 1; col <= 8; col++) {
                makeSquare(game, row, col);
            }
            System.out.println(row + " ");
        }
        printColumnsWhite();
    }

    private void makeSquare(ChessGame game, int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece chessPiece = game.getBoard().getPiece(position);
        colorSquare(row, col);
        if (chessPiece == null) {
            System.out.println(EscapeSequences.EMPTY);
        } else {
            System.out.println(getPiece(chessPiece));
        }
    }

    private String getPiece(ChessPiece chessPiece) {
        return switch (chessPiece.getTeamColor()) {
            case WHITE -> getWhitePiece(chessPiece);
            case BLACK -> getBlackPiece(chessPiece);
        };
    }

    private String getBlackPiece(ChessPiece chessPiece) {
        return switch (chessPiece.getPieceType()) {
            case KING -> EscapeSequences.BLACK_KING;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case PAWN -> EscapeSequences.BLACK_PAWN;
        };
    }

    private String getWhitePiece(ChessPiece chessPiece) {
        return switch (chessPiece.getPieceType()) {
            case KING -> EscapeSequences.WHITE_KING;
            case QUEEN -> EscapeSequences.WHITE_QUEEN;
            case BISHOP -> EscapeSequences.WHITE_BISHOP;
            case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
            case ROOK -> EscapeSequences.WHITE_ROOK;
            case PAWN -> EscapeSequences.WHITE_PAWN;
        };
    }

    private void colorSquare(int row, int col) {
        boolean lightSquare = ((row + col) % 2 == 0);
        if (lightSquare) {
            System.out.println(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        } else {
            System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        }
    }

    private void drawBlackBoard(ChessGame game) {
        printColumnsBlack();
        for (int row = 1; row <= 8; row++) {
            System.out.print(row + " ");
            for (int col = 8; col > 0; col--) {
                makeSquare(game, row, col);
            }
            System.out.println(row + " ");
        }
        printColumnsBlack();
    }

    private void printColumnsWhite() {
        System.out.print("   ");
        for (char c = 'a'; c < 'i'; c++) {
            System.out.print(" " + c + " ");
        }
        System.out.println();
    }

    private void printColumnsBlack() {
        System.out.print("   ");
        for (char c = 'h'; c >= 'a'; c--) {
            System.out.print(" " + c + " ");
        }
        System.out.println();
    }
}
