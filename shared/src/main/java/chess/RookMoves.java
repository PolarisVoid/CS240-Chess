package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        // Left
        int row = myPosition.getRow() - 1;
        while (row > 0) {
//            System.out.print("Left " + row);
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

        // Right
        row = myPosition.getRow() + 1;
        while (row < 9) {
//            System.out.print("Right " + row);
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

        // Down
        int col = myPosition.getColumn() - 1;
        while (col > 0) {
//            System.out.print("Down" + col);
            ChessPiece currentSquare = board.getPiece(new ChessPosition(myPosition.getRow(), col));
            if (currentSquare != null) {
                System.out.print(currentSquare);
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(myPosition.getRow(), col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(myPosition.getRow(), col), null));
            col--;
        }

        // Up
        col = myPosition.getColumn() + 1;
        while (col < 9) {
//            System.out.print("Up" + col);
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
