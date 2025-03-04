package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {

    private static Collection<ChessMove> generateMoves(
            ChessPosition startPos,
            ChessPosition endPos,
            Collection<ChessPiece.PieceType> promoPieces) {
        Collection<ChessMove> moves = new ArrayList<>();
        if (promoPieces.isEmpty()) {
            moves.add(new ChessMove(startPos, endPos, null));
            return moves;
        }
        for (ChessPiece.PieceType promotionPiece: promoPieces) {
            moves.add(new ChessMove(startPos, endPos, promotionPiece));
        }
        return moves;
    }

    private static Collection<ChessMove> getPawnMoves(
            int startRow,
            int promoRow,
            int[][] offsets,
            ChessBoard board,
            ChessPosition myPos,
            ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<>();

        int row = myPos.getRow();
        int col = myPos.getColumn();
        for (int[] offset: offsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            if (newRow > 8 || newRow < 1 || newCol > 8 || newCol < 1) {
                continue;
            }

            Collection<ChessPiece.PieceType> promotionPieces = new ArrayList<>();
            if (newRow == promoRow) {
                promotionPieces.add(ChessPiece.PieceType.QUEEN);
                promotionPieces.add(ChessPiece.PieceType.BISHOP);
                promotionPieces.add(ChessPiece.PieceType.ROOK);
                promotionPieces.add(ChessPiece.PieceType.KNIGHT);
            }
            ChessPiece proposedMove = board.getPiece(new ChessPosition(newRow, newCol));
            if (proposedMove != null) {
                if (proposedMove.getTeamColor() != color && offset[1] != 0) {
                    moves.addAll(generateMoves(myPos, new ChessPosition(newRow, newCol), promotionPieces));
                }
                continue;
            }

            if (offset[1] != 0) {
                continue;
            }

            if(offset[0] == offsets[1][0]) {
                if (startRow != row) {
                    continue;
                }
                if (board.getPiece(new ChessPosition(row + offsets[0][0], col)) == null) {
                    moves.addAll(generateMoves(myPos, new ChessPosition(newRow, newCol), promotionPieces));
                }
                continue;
            }
            moves.addAll(generateMoves(myPos, new ChessPosition(newRow, newCol), promotionPieces));
        }
        return moves;
    }

    private static Collection<ChessMove> getBlackPawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int startRow = 7;
        int promotionRow = 1;

        int[][] offsets = {
                {-1, 0}, {-2, 0},
                {-1, -1}, {-1, 1}
        };
        return getPawnMoves(startRow, promotionRow, offsets, board, myPosition, color);
    }

    private static Collection<ChessMove> getWhitePawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int startRow = 2;
        int promotionRow = 8;

        int[][] offsets = {
                {1, 0}, {2, 0},
                {1, -1}, {1, 1}
        };
        return getPawnMoves(startRow, promotionRow, offsets, board, myPosition, color);
    }

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        return switch (color) {
            case BLACK -> getBlackPawnMoves(board, myPosition, color);
            case WHITE -> getWhitePawnMoves(board, myPosition, color);
        };
    }
}
