package chess;

import java.util.Collection;

public class KnightMoves extends Moves {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int[][] offsets = {
                {1, 2}, {-1, 2}, {1, -2},
                {-1, -2}, {2, -1}, {-2, -1},
                {-2, 1}, {2, 1}
        };

        return getOffsetMoves(board, myPosition, color, offsets);
    }
}
