package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {

    public Collection<ChessMove> generatePromotion(int startRow, int startCol, int endRow, int endCol, ArrayList<ChessPiece.PieceType> promotions) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        if (promotions.isEmpty()) {
            moves.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), null));
            return moves;
        }
        for (ChessPiece.PieceType promotion: promotions) {
            moves.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), promotion));
        }
        return moves;
    }

    public Collection<ChessMove> getPawnMoves(int[][] Offsets, int promotionRow, int startRow, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int[] Offset: Offsets) {
            int newRow = row + Offset[0];
            int newCol = col + Offset[1];
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                continue;
            }
            if (row != startRow && Offset[0] == Offsets[1][0]) {
                continue;
            }
            ArrayList<ChessPiece.PieceType> promotionPiece = new ArrayList<ChessPiece.PieceType>();
            if (newRow == promotionRow) {
                promotionPiece.add(ChessPiece.PieceType.QUEEN);
                promotionPiece.add(ChessPiece.PieceType.ROOK);
                promotionPiece.add(ChessPiece.PieceType.BISHOP);
                promotionPiece.add(ChessPiece.PieceType.KNIGHT);
            }

            ChessPiece currentSquare = board.getPiece(new ChessPosition(newRow, newCol));
            if (currentSquare != null) {
                if (col == newCol) {
                    continue;
                }
                if (currentSquare.getTeamColor() != color) {
                    moves.addAll(generatePromotion(myPosition.getRow(), myPosition.getColumn(), newRow, newCol, promotionPiece));
                }
                continue;
            }

            if (Offset[1] != 0) {
                continue;
            }

            if (Offset[0] == Offsets[1][0] && board.getPiece(new ChessPosition(row + Offsets[0][0], newCol)) != null) {
                continue;
            }

            moves.addAll(generatePromotion(myPosition.getRow(), myPosition.getColumn(), newRow, newCol, promotionPiece));
        }

        return moves;
    }

    public Collection<ChessMove> pawnBlackMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int[][] Offsets = {
                {-1, 0}, {-2, 0},
                {-1, 1}, {-1, -1}
        };

        int promotionRow = 1;
        int startRow = 7;
        return getPawnMoves(Offsets, promotionRow, startRow, board, myPosition, color);
    }

    public Collection<ChessMove> pawnWhiteMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int[][] Offsets = {
                {1, 0}, {2, 0},
                {1, 1}, {1, -1}
        };

        int promotionRow = 8;
        int startRow = 2;
        return getPawnMoves(Offsets, promotionRow, startRow, board, myPosition, color);
    }

    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        return switch (color) {
            case BLACK -> pawnBlackMoves(board, myPosition, color);
            case WHITE -> pawnWhiteMoves(board, myPosition, color);
        };
    }
}
