package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves extends Moves{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(interationMove(1, 0, thresholdConstructor(9, 9, 3), myPosition, board, color));
        moves.addAll(interationMove(-1, 0, thresholdConstructor(0, 0, 0), myPosition, board, color));
        moves.addAll(interationMove(0, 1, thresholdConstructor(9, 9, 3), myPosition, board, color));
        moves.addAll(interationMove(0, -1, thresholdConstructor(0, 0, 0), myPosition, board, color));

        return moves;
    }
}
