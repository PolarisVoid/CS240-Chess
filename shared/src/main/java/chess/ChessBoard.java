package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(Board, that.Board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(Board);
    }

    @Override
    public String toString() {
        String boardString = "";
        for (int i = 0; i < 8; i++) {
            boardString = boardString + " {";
            for (int j = 0; j < 8; j++) {
                boardString = boardString + " " + Board[i][j];
            }
            boardString = boardString + " }\n";
        }


        return "ChessBoard{\n" +
                "Board=" + boardString +
                '}';
    }

    public ChessBoard copy() {
        return new ChessBoard(Board);
    }

    private ChessPiece[][] Board;

    public ChessBoard() {
        this.Board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.Board[i][j] = null;
            }
        }
    }

    public ChessBoard(ChessPiece[][] board) {
        this.Board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                if (piece == null) {
                    continue;
                }
                this.Board[i][j] = board[i][j].copy();
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (1 > position.getRow() || 8 < position.getRow() || 1 > position.getColumn() || 8 < position.getColumn()){
            return;
        }
        this.Board[8-position.getRow()][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (1 > position.getRow() || 8 < position.getRow() || 1 > position.getColumn() || 8 < position.getColumn()){
            return null;
        }
        return this.Board[8-position.getRow()][position.getColumn()-1];
    }

    public void makeMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece promotion;

        if (move.getPromotionPiece() == null) {
            promotion = getPiece(start);
        }
        else {
            ChessPiece piece = getPiece(start);
            if (piece == null) {
                return;
            }
            promotion = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        addPiece(start, null);
        addPiece(end, promotion);
    }

    private void SetupRow(int row, ChessPiece.PieceType[] Pieces, ChessGame.TeamColor color) {
        for (int i = 0; i < 8; ++i) {
            this.Board[row][i] = new ChessPiece(color, Pieces[i]);
        }
    }

    private void SetupEmptyRow(int row) {
        for (int i = 0; i < 8; ++i) {
            this.Board[row][i] = null;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] MainRow = {
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.KING,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.ROOK
        };
        ChessPiece.PieceType[] PawnRow = {
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
        };

        this.SetupRow(0, MainRow, ChessGame.TeamColor.BLACK);
        this.SetupRow(1, PawnRow, ChessGame.TeamColor.BLACK);
        this.SetupEmptyRow(2);
        this.SetupEmptyRow(3);
        this.SetupEmptyRow(4);
        this.SetupEmptyRow(5);
        this.SetupRow(6, PawnRow, ChessGame.TeamColor.WHITE);
        this.SetupRow(7, MainRow, ChessGame.TeamColor.WHITE);
    }
}
