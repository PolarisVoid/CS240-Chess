package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {

    private static Collection<ChessMove> generateMoves(ChessPosition startPosition,ChessPosition endPosition,Collection<ChessPiece.PieceType> promotionPieces) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        if (promotionPieces.isEmpty()) {
            moves.add(new ChessMove(startPosition, endPosition, null));
            return moves;
        }
        for (ChessPiece.PieceType promotionPiece: promotionPieces) {
            moves.add(new ChessMove(startPosition, endPosition, promotionPiece));
        }
        return moves;
    }

    private static Collection<ChessMove> getPawnMoves(int startRow, int promotionRow, int[][] Offsets, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int[] Offset: Offsets) {
            int newRow = row + Offset[0];
            int newCol = col + Offset[1];
            if (newRow > 8 || newRow < 1 || newCol > 8 || newCol < 1) {
                continue;
            }

            Collection<ChessPiece.PieceType> promotionPieces = new ArrayList<ChessPiece.PieceType>();
            if (newRow == promotionRow) {
                promotionPieces.add(ChessPiece.PieceType.QUEEN);
                promotionPieces.add(ChessPiece.PieceType.BISHOP);
                promotionPieces.add(ChessPiece.PieceType.ROOK);
                promotionPieces.add(ChessPiece.PieceType.KNIGHT);
            }
            ChessPiece proposedMove = board.getPiece(new ChessPosition(newRow, newCol));
            if (proposedMove != null) {
                if (proposedMove.getTeamColor() != color && Offset[1] != 0) {
                    moves.addAll(generateMoves(myPosition, new ChessPosition(newRow, newCol), promotionPieces));
                }
                continue;
            }

            if (Offset[1] != 0) {
                continue;
            }

            if(Offset[0] == Offsets[1][0]) {
                if (startRow != row) {
                    continue;
                }
                if (board.getPiece(new ChessPosition(row + Offsets[0][0], col)) == null) {
                    moves.addAll(generateMoves(myPosition, new ChessPosition(newRow, newCol), promotionPieces));
                }
                continue;
            }
            moves.addAll(generateMoves(myPosition, new ChessPosition(newRow, newCol), promotionPieces));
        }
        return moves;
    }

    private static Collection<ChessMove> getBlackPawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int startRow = 7;
        int promotionRow = 1;

        int[][] Offsets = {
                {-1, 0}, {-2, 0},
                {-1, -1}, {-1, 1}
        };
        return getPawnMoves(startRow, promotionRow, Offsets, board, myPosition, color);
    }

    private static Collection<ChessMove> getWhitePawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int startRow = 2;
        int promotionRow = 8;

        int[][] Offsets = {
                {1, 0}, {2, 0},
                {1, -1}, {1, 1}
        };
        return getPawnMoves(startRow, promotionRow, Offsets, board, myPosition, color);
    }

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        return switch (color) {
            case BLACK -> getBlackPawnMoves(board, myPosition, color);
            case WHITE -> getWhitePawnMoves(board, myPosition, color);
        };
    }
}
