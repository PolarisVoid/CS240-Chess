package chess;

import java.util.Collection;

public class KingMoves extends Moves {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        int[][] offsets = {
            {1, 0}, {0, 1}, {1, 1},
            {-1, 0}, {0, -1}, {-1, -1},
            {-1, 1}, {1, -1}
        };

        return getOffsetMoves(board, myPosition, color, offsets);
    }
}
