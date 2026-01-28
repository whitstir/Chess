package chess;

import java.util.Collection;

public class KnightMovesCalculator {

    private final int[][] directions = {{1,2},{-1,2},{1,-2},{-1,-2},{2,1},{-2,1},{2,-1},{-2,-1}};

    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator = new PieceMovesCalculator();
        return calculator.findMoves(board, position, directions);
    }
}
