package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return Row == that.Row && Col == that.Col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Row, Col);
    }

    @Override
    public String toString() {
        return  "Row=" + Row +
                ", Col=" + Col;
    }

    private int Row;
    private int Col;
    public ChessPosition(int row, int col) {
        this.Row = row;
        this.Col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.Row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.Col;
    }
}
