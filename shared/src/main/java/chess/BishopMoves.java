package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int row = myPosition.getRow() - 1;
        int col = myPosition.getColumn() - 1;
        while (row > 0 && col > 0) {
            ChessPiece currentSquare = board.getPiece(new ChessPosition(row, col));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
            row--;
            col--;
        }

        row = myPosition.getRow() - 1;
        col = myPosition.getColumn() + 1;
        while (row > 0 && col < 9) {
            ChessPiece currentSquare = board.getPiece(new ChessPosition(row, col));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
            row--;
            col++;
        }

        row = myPosition.getRow() + 1;
        col = myPosition.getColumn() - 1;
        while (row < 9 && col > 0) {
            ChessPiece currentSquare = board.getPiece(new ChessPosition(row, col));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
            row++;
            col--;
        }

        row = myPosition.getRow() + 1;
        col = myPosition.getColumn() + 1;
        while (row < 9 && col < 9) {
            ChessPiece currentSquare = board.getPiece(new ChessPosition(row, col));
            if (currentSquare != null) {
                if (currentSquare.getTeamColor() != color) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
            row++;
            col++;
        }

        return moves;
    }
}
