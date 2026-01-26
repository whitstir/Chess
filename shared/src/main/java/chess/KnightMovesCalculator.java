package chess;

import java.util.Collection;

public class KnightMovesCalculator {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator = new PieceMovesCalculator();
        return calculator.calcJumpMoves(board, position);
    }
}