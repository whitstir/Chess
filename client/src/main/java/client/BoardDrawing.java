package client;

import chess.ChessGame;


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
