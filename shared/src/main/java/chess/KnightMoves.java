package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int[][] offsets = {
                {1, 2}, {-1, 2}, {1, -2},
                {-1, -2}, {2, -1}, {-2, -1},
                {-2, 1}, {2, 1}
        };

        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int[] offset: offsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            if (newRow > 8 || newRow < 1 || newCol > 8 || newCol < 1) {
                continue;
            }
            ChessPiece proposedMove = board.getPiece(new ChessPosition(newRow, newCol));
            if (proposedMove != null) {
                if (proposedMove.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                }
                continue;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
        }
        return moves;
    }
}
