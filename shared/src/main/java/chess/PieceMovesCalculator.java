package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public Collection<ChessMove> findMoves(ChessBoard board, ChessPosition position, int[][] directions) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (piece.getPieceType() == ChessPiece.PieceType.QUEEN || piece.getPieceType() == ChessPiece.PieceType.BISHOP
                    || piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                while (onBoard(newRow, newCol)) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
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
                    newCol += direction[1];
                }
            } else {
                if (onBoard(newRow, newCol)) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
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
        }
        return possibleMoves;
    }

    public Collection<ChessMove> findPawnMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(findForwardPawnMoves(board, position));
        possibleMoves.addAll(findCapturePawnMoves(board, position));
        return possibleMoves;
    }

    private Collection<ChessMove> findCapturePawnMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        int[][] captureDirections;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            captureDirections = new int[][] {{1,1},{1,-1}};
        } else {
            captureDirections = new int[][] {{-1,1},{-1,-1}};
        }

        for (int[] direction : captureDirections) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (onBoard(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (!targetIsEmpty(targetPiece) && piece.getTeamColor() != targetPiece.getTeamColor()) {
                    possibleMoves.addAll(findPawnPromotions(board, position, newPosition, newRow, piece));
                }
            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> findForwardPawnMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        int[][] forwardDirections;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (pawnAtStart(row, piece)) {
                forwardDirections = new int[][]{{1, 0}, {2, 0}};
            } else {
                forwardDirections = new int[][]{{1, 0}};
            }
        } else {
            if (pawnAtStart(row, piece)) {
                forwardDirections = new int[][]{{-1, 0}, {-2, 0}};
            } else {
                forwardDirections = new int[][]{{-1, 0}};
            }
        }

        for (int[] direction : forwardDirections) {
            int newRow = row + direction[0];
            if (onBoard(newRow, col)) {
                ChessPosition newPosition = new ChessPosition(newRow, col);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (!targetIsEmpty(targetPiece)) {
                    continue;
                }
                if (direction[0] == 2 || direction[0] == -2) {
                    int middleRow = row + (direction[0] / 2);
                    ChessPosition middlePosition = new ChessPosition(middleRow, col);
                    ChessPiece middlePiece = board.getPiece(middlePosition);
                    if (!targetIsEmpty(middlePiece)) {
                        continue;
                    } else {
                        possibleMoves.addAll(findPawnPromotions(board, position, newPosition, newRow, piece));
                    }
                } else {
                    possibleMoves.addAll(findPawnPromotions(board, position, newPosition, newRow, piece));
                }
            }

        }
        return possibleMoves;
    }

    private Collection<ChessMove> findPawnPromotions(ChessBoard board, ChessPosition position, ChessPosition newPosition,
                                                     int row, ChessPiece piece) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (pawnAtEnd(row, piece)) {
            possibleMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
            possibleMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
            possibleMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
        } else {
            possibleMoves.add(new ChessMove(position, newPosition, null));
        }
        return possibleMoves;
    }

    private boolean pawnAtStart(int row, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return row == 2;
        } else {
            return row == 7;
        }
    }

    private boolean pawnAtEnd(int row, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return row == 8;
        } else {
            return row == 1;
        }
    }

    private boolean targetIsEmpty(ChessPiece piece) {
        return piece == null;
    }

    private boolean onBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}

