package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public Collection<ChessMove> calcOrthogonalMoves(chess.ChessBoard board, chess.ChessPosition position) {

        int[][] possibleDirections = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        return findMultipleMoves(possibleDirections, board, position);
    }

    public Collection<ChessMove> calcDiagonalMoves(chess.ChessBoard board, chess.ChessPosition position) {
        int[][] possibleDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return findMultipleMoves(possibleDirections, board, position);
    }

    public Collection<ChessMove> calcJumpMoves(chess.ChessBoard board, chess.ChessPosition position) {
        int[][] possibleDirections = {{1, 2}, {2, 1}, {-1, 2}, {2, -1}, {-1, -2}, {-2, -1}, {1, -2}, {-2, 1}};
        return findOneMove(possibleDirections, board, position);
    }

    public Collection<ChessMove> calcKingMoves(chess.ChessBoard board, chess.ChessPosition position) {
        int[][] possibleDirections = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return findOneMove(possibleDirections, board, position);
    }

    public Collection<ChessMove> calcPawnMoves(chess.ChessBoard board, chess.ChessPosition position) {
        return findPawnMoves(board, position);
    }

    public Collection<ChessMove> findPawnMoves(chess.ChessBoard board, chess.ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        possibleMoves.addAll(getForwardPawnMoves(board, position, row, col, piece));
        possibleMoves.addAll(getCapturePawnMoves(board, position, row, col, piece));

        return possibleMoves;

    }

    private Collection<ChessMove> getCapturePawnMoves(ChessBoard board, ChessPosition position, int row, int col, ChessPiece piece) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int[][] capturingDirections;

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            capturingDirections = new int[][]{{1, 1}, {1, -1}};
        } else {
            capturingDirections = new int[][]{{-1, 1}, {-1, -1}};
        }

        for (int[] direction : capturingDirections) {
            int newRow = row + direction[0];
            int newColumn = col + direction[1];
            if (onBoard(newRow, newColumn)) {
                ChessPosition newPosition = new ChessPosition(newRow, newColumn);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (!targetIsEmpty(targetPiece) && piece.getTeamColor() != targetPiece.getTeamColor()) {
                    possibleMoves.addAll(getPawnPromotion(position, newPosition, newRow, col, piece));
                }
            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> getForwardPawnMoves(chess.ChessBoard board, chess.ChessPosition position, int row, int col, ChessPiece piece) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int[][] forwardsDirections;

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            forwardsDirections = new int[][]{{1, 0}};
        } else {
            forwardsDirections = new int[][]{{-1, 0}};
        }

        for (int[] direction : forwardsDirections) {
            int newRow = row + direction[0];
            if (onBoard(newRow, col)) {
                ChessPosition newPosition = new ChessPosition(newRow, col);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetIsEmpty(targetPiece)) {
                    possibleMoves.addAll(getPawnPromotion(position, newPosition, newRow, col, piece));

                    if (pawnAtStart(row, piece)) {
                        int twoRow = row + (2 * direction[0]);

                        if (onBoard(twoRow, col)) {
                            ChessPosition twoPosition = new ChessPosition(twoRow, col);
                            ChessPiece twoPiece = board.getPiece(twoPosition);

                            if (targetIsEmpty(twoPiece)) {
                                possibleMoves.add(new ChessMove(position, twoPosition, null));
                            }
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> getPawnPromotion(ChessPosition position, ChessPosition newPosition, int newRow, int col, ChessPiece piece) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (pawnAtEnd(newRow, col, piece)) {
            possibleMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
            possibleMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
            possibleMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
        } else {
            possibleMoves.add(new ChessMove(position, newPosition, null));
        }
        return possibleMoves;
    }

    public boolean pawnAtStart(int row, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return row == 2;
        } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return row == 7;
        } else {
            return false;
        }
    }

    public boolean pawnAtEnd(int row, int col, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return row == 8;
        } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return row == 1;
        } else {
            return false;
        }
    }

    public Collection<ChessMove> findOneMove(int[][] possibleDirections, chess.ChessBoard board, chess.ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        for (int[] direction : possibleDirections) {
            int newRow = row + direction[0];
            int newColumn = col + direction[1];
            if (onBoard(newRow, newColumn)) {
                ChessPosition newPosition = new ChessPosition(newRow, newColumn);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetIsEmpty(targetPiece)) {
                    possibleMoves.add(new ChessMove(position, newPosition, null));
                } else {
                    if (piece.getTeamColor() != targetPiece.getTeamColor()) {
                        possibleMoves.add(new ChessMove(position, newPosition, null));
                    }
                }
            }
        }
        return possibleMoves;
    }

    public Collection<ChessMove> findMultipleMoves(int[][] possibleDirections, chess.ChessBoard board, chess.ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        for (int[] direction : possibleDirections) {
            int newRow = row + direction[0];
            int newColumn = col + direction[1];
            while (onBoard(newRow, newColumn)) {
                ChessPosition newPosition = new ChessPosition(newRow, newColumn);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetIsEmpty(targetPiece)) {
                    possibleMoves.add(new ChessMove(position, newPosition, null));
                } else {
                    if (piece.getTeamColor() != targetPiece.getTeamColor()) {
                        possibleMoves.add(new ChessMove(position, newPosition, null));
                    }
                    break;
                }
                newRow += direction[0];
                newColumn += direction[1];
            }
        }
        return possibleMoves;
    }

    public boolean onBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    public boolean targetIsEmpty(ChessPiece piece) {
        return piece == null;
    }
}
