package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;

public class BishopMoves {

    public static BiPredicate<Integer, Integer> thresholdConstructor(int rowThreshold, int colThreshold, int type) {
        return switch (type) {
            case 0 -> (row, col) -> row > rowThreshold && col > colThreshold;
            case 1 -> (row, col) -> row < rowThreshold && col > colThreshold;
            case 2 -> (row, col) -> row > rowThreshold && col < colThreshold;
            case 3 -> (row, col) -> row < rowThreshold && col < colThreshold;
            default -> throw new IllegalArgumentException("Invalid type");
        };
    }

    public static Collection<ChessMove> interationMove(
            int rowChange, int colChange,
            BiPredicate<Integer, Integer> outOfBounds,
            ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int row = myPosition.getRow() + rowChange;
        int col = myPosition.getColumn() + colChange;

        while (outOfBounds.test(row, col)) {
            int newRow = row;
            int newCol = col;
            ChessPiece proposedMove = board.getPiece(new ChessPosition(newRow, newCol));
            if (proposedMove != null) {
                if (proposedMove.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            row = row + rowChange;
            col = col + colChange;
        }
        return moves;
    }

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        moves.addAll(interationMove(1, 1, thresholdConstructor(9, 9, 3), myPosition, board, color));
        moves.addAll(interationMove(-1, 1, thresholdConstructor(0, 9, 2), myPosition, board, color));
        moves.addAll(interationMove(1, -1, thresholdConstructor(9, 0, 1), myPosition, board, color));
        moves.addAll(interationMove(-1, -1, thresholdConstructor(0, 0, 0), myPosition, board, color));

        return moves;
    }
}
