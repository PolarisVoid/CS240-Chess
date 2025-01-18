package chess;

import java.util.Collection;
import java.util.Iterator;

public class RookMoves {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new Collection<ChessMove>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<ChessMove> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(ChessMove chessMove) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends ChessMove> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
        int row = myPosition.getRow();
        while (row > 0) {
            ChessPiece currentSquare = board.getPiece(new ChessPosition(row, myPosition.getColumn()));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, myPosition.getColumn()), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, myPosition.getColumn()), null));
            row--;
        }
        row = myPosition.getRow();
        while (row < 8) {
            ChessPiece currentSquare = board.getPiece(new ChessPosition(row, myPosition.getColumn()));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, myPosition.getColumn()), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, myPosition.getColumn()), null));
            row++;
        }
        int col = myPosition.getColumn();
        while (col > 0) {
            ChessPiece currentSquare = board.getPiece(new ChessPosition(myPosition.getRow(), col));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(myPosition.getRow(), col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(myPosition.getRow(), col), null));
            col--;
        }
        col = myPosition.getColumn();
        while (col < 8) {
            ChessPiece currentSquare = board.getPiece(new ChessPosition(myPosition.getRow(), col));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(myPosition.getRow(), col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(myPosition.getRow(), col), null));
            col++;
        }
        return moves;
    }
}
