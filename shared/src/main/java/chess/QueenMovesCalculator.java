package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator = new PieceMovesCalculator();
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(calculator.calcOrthogonalMoves(board, position));
        possibleMoves.addAll(calculator.calcDiagonalMoves(board, position));
        return possibleMoves;
    }
}
