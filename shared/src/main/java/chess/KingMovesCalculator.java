package chess;

import java.util.Collection;

public class KingMovesCalculator {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator = new PieceMovesCalculator();
        return calculator.calcKingMoves(board, position);
    }
}
