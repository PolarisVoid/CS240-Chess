package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(BishopMoves.pieceMoves(board, myPosition, color));
        moves.addAll(RookMoves.pieceMoves(board, myPosition, color));

        return moves;
    }
}
