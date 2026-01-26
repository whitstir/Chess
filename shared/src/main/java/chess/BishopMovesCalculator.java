package chess;

import java.util.Collection;

public class BishopMovesCalculator {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator = new PieceMovesCalculator();
        return calculator.calcDiagonalMoves(board, position);
    }
}