package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;

public class Moves {
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
        Collection<ChessMove> moves = new ArrayList<>();

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

    public static Collection<ChessMove> getOffsetMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, int[][] offsets) {
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
