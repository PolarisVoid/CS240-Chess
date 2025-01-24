package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    public boolean validMoveKnight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        for (ChessMove move: new KnightMoves().getMoves(board, myPosition, color)) {
            ChessPiece piece = board.getPiece(move.getEndPosition());
            if (piece == null) {
                continue;
            }

            if (piece.getPieceType() != ChessPiece.PieceType.KNIGHT) {
                continue;
            }

            return false;
        }
        return true;
    }
    public boolean validMoveRook(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        for (ChessMove move: new RookMoves().getMoves(board, myPosition, color)) {
            ChessPiece piece = board.getPiece(move.getEndPosition());
            if (piece == null) {
                continue;
            }

            if (piece.getPieceType() != ChessPiece.PieceType.QUEEN && piece.getPieceType() != ChessPiece.PieceType.ROOK) {
                continue;
            }

            return false;
        }
        return true;
    }
    public boolean validMoveBishop(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        ChessGame.TeamColor opColor = color == ChessGame.TeamColor.BLACK? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        for (ChessMove move: new RookMoves().getMoves(board, myPosition, color)) {
            ChessPiece piece = board.getPiece(move.getEndPosition());
            if (piece == null) {
                continue;
            }

            if (piece.getPieceType() != ChessPiece.PieceType.QUEEN &&
                piece.getPieceType() != ChessPiece.PieceType.BISHOP &&
                piece.getPieceType() != ChessPiece.PieceType.PAWN) {
                continue;
            }

            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                for (ChessMove pawnMove: new RookMoves().getMoves(board, myPosition, opColor)) {
                    if (pawnMove.getEndPosition() == move.getStartPosition()) {
                        return false;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public boolean validMove(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        return validMoveKnight(board, myPosition, color) && validMoveRook(board, myPosition, color) && validMoveBishop(board, myPosition, color);
    }

    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {

        // King Moves collection
        int[][] Offsets = {
                {1, 0}, {1, 1}, {0, 1},
                {-1, 1}, {-1, 0}, {-1, -1},
                {1, -1}, {0, -1}
        };

        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int[] Offset : Offsets) {
            int newRow = row + Offset[0];
            int newCol = col + Offset[1];
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                continue;
            }
            ChessPiece currentSquare = board.getPiece(new ChessPosition(newRow, newCol));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(newRow, newCol), null));
                }
                continue;
            }
            moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(newRow, newCol), null));
        }

//        Collection<ChessMove> newMoves = new ArrayList<ChessMove>();
//
//        for (ChessMove move: moves) {
//            if (!validMove(board, move.getEndPosition(), color)) {
//                continue;
//            }
//            newMoves.add(move);
//        }

        return moves;
    }
}
