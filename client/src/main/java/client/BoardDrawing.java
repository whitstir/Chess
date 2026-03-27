package client;

import chess.ChessGame;

public class BoardDrawing {
    public static void drawBoard(ChessGame game, ChessGame.TeamColor side) {
        if (side == ChessGame.TeamColor.WHITE) {
            drawWhiteBoard(game);
        } else {
            drawBlackBoarD(game);
        }
    }

    private static void drawBlackBoarD(ChessGame game) {
    }

    private static void drawWhiteBoard(ChessGame game) {
    }
}
