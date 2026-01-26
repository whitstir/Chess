package chess;

import java.util.Collection;

public class PawnMovesCalculator {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator = new PieceMovesCalculator();
        return calculator.calcPawnMoves(board, position);
    }
}
