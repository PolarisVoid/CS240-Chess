package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        moves.addAll(new RookMoves().getMoves(board, myPosition, color));
        moves.addAll(new BishopMoves().getMoves(board, myPosition, color));

        return moves;
    }
}
