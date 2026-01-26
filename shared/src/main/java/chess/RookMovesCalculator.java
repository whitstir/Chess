package chess;

import java.util.Collection;

public class RookMovesCalculator {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator = new PieceMovesCalculator();
        return calculator.calcOrthogonalMoves(board, position);
    }
}
