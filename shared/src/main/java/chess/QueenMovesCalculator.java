package chess;

import java.util.Collection;

public class QueenMovesCalculator {

    private final int[][] directions = {{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{-1,0},{0,1},{0,-1}};

    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator = new PieceMovesCalculator();
        return calculator.findMoves(board, position, directions);
    }
}
