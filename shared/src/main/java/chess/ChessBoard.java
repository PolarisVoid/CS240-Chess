package chess;

import java.util.Arrays;
import java.util.Objects;
import com.google.gson.Gson;

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
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ChessBoard copy() {
        return new ChessBoard(board);
    }

    private final ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = null;
            }
        }
    }

    public ChessBoard(ChessPiece[][] board) {
        this.board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                if (piece == null) {
                    continue;
                }
                this.board[i][j] = board[i][j].copy();
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
        this.board[8-position.getRow()][position.getColumn()-1] = piece;
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
        return this.board[8-position.getRow()][position.getColumn()-1];
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

    private void setupRow(int row, ChessPiece.PieceType[] pieces, ChessGame.TeamColor color) {
        for (int i = 0; i < 8; ++i) {
            this.board[row][i] = new ChessPiece(color, pieces[i]);
        }
    }

    private void setupEmptyRow(int row) {
        for (int i = 0; i < 8; ++i) {
            this.board[row][i] = null;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] mainRow = {
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.KING,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.ROOK
        };
        ChessPiece.PieceType[] pawnRow = {
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
        };

        this.setupRow(0, mainRow, ChessGame.TeamColor.BLACK);
        this.setupRow(1, pawnRow, ChessGame.TeamColor.BLACK);
        this.setupEmptyRow(2);
        this.setupEmptyRow(3);
        this.setupEmptyRow(4);
        this.setupEmptyRow(5);
        this.setupRow(6, pawnRow, ChessGame.TeamColor.WHITE);
        this.setupRow(7, mainRow, ChessGame.TeamColor.WHITE);
    }
}
