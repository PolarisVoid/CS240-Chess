package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int[][] Offsets = {
                {2, 1}, {2, -1},
                {-2, 1}, {-2, -1},
                {1, 2}, {1, -2},
                {-1, 2}, {-1, -2}
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
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(newRow, newCol), null));
                }
                continue;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(newRow, newCol), null));
        }
        return moves;
    }
}
