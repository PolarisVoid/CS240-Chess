package chess;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return TeamColor == that.TeamColor && Type == that.Type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(TeamColor, Type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "TeamColor=" + TeamColor +
                ", Type=" + Type +
                '}';
    }

    private ChessGame.TeamColor TeamColor;
    private ChessPiece.PieceType Type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.TeamColor = pieceColor;
        this.Type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.TeamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.Type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (this.Type) {
            case KING -> new KingMoves().getMoves(board, myPosition, TeamColor);
            case QUEEN -> new QueenMoves().getMoves(board, myPosition, TeamColor);
            case BISHOP -> new BishopMoves().getMoves(board, myPosition, TeamColor);
            case ROOK -> new RookMoves().getMoves(board, myPosition, TeamColor);
            case KNIGHT -> new KnightMoves().getMoves(board, myPosition, TeamColor);
            case PAWN -> new PawnMoves().getMoves(board, myPosition, TeamColor);
        };
    }
}
