package chess;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board;
    TeamColor nextTurn;
    TeamColor resigned;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        nextTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.nextTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.nextTurn = team;
    }

    public void setResigned(TeamColor resigned) {
        this.resigned = resigned;
    }

    public TeamColor getResigned() {
        return resigned;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private boolean isKingAttacked(ChessPosition kingLocation, ChessBoard board) {
        ChessPiece king = board.getPiece(kingLocation);
        TeamColor oppColor = (king.getTeamColor() == TeamColor.WHITE) ? TeamColor.BLACK: TeamColor.WHITE;

        Collection<ChessMove> moves = RookMoves.pieceMoves(board, kingLocation, king.getTeamColor());
        for (ChessMove move: moves) {
            ChessPiece piece = board.getPiece(move.getEndPosition());
            if (piece != null && (piece.getPieceType() == ChessPiece.PieceType.ROOK || piece.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                return true;
            }
        }

        moves = BishopMoves.pieceMoves(board, kingLocation, king.getTeamColor());
        for (ChessMove move: moves) {
            ChessPiece piece = board.getPiece(move.getEndPosition());
            if (piece != null && (piece.getPieceType() == ChessPiece.PieceType.BISHOP || piece.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                return true;
            }
        }

        moves = KnightMoves.pieceMoves(board, kingLocation, king.getTeamColor());
        for (ChessMove move: moves) {
            ChessPiece piece = board.getPiece(move.getEndPosition());
            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        int direction = (oppColor == TeamColor.WHITE) ? -1: 1;
        int[][] offsets = {{direction, -1}, {direction, 1}};

        for (int[] offset: offsets) {
            ChessPiece piece = board.getPiece(new ChessPosition(kingLocation.getRow() + offset[0], kingLocation.getColumn() + offset[1]));
            if (piece == null) {
                continue;
            }
            if (piece.getTeamColor() != oppColor) {
                continue;
            }
            if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
                continue;
            }
            return true;
        }

        offsets = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] offset: offsets) {
            ChessPiece piece = board.getPiece(new ChessPosition(kingLocation.getRow() + offset[0], kingLocation.getColumn() + offset[1]));
            if (piece == null) {
                continue;
            }
            if (piece.getPieceType() != ChessPiece.PieceType.KING) {
                continue;
            }
            return true;
        }
        return false;
    }

    private ChessPosition findKingPosition(TeamColor color) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getTeamColor() == color && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return new ChessPosition(i, j);
                }
            }
        }
        System.out.println(board);
        return null;
    }

    private boolean inCheck(TeamColor color, ChessBoard simulatedBoard) {
        TeamColor oppColor = (color == TeamColor.WHITE) ? TeamColor.BLACK: TeamColor.WHITE;
        Collection<ChessMove> oppMoves = getAllValidMoves(oppColor, simulatedBoard);

        for (ChessMove oppMove: oppMoves) {
            ChessPiece attackedPiece = simulatedBoard.getPiece(oppMove.getEndPosition());
            if (attackedPiece != null && attackedPiece.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        return false;
    }

    public Collection<ChessMove> getAllValidMoves(TeamColor color, ChessBoard board) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == color) {
                    moves.addAll(getValidMoves(position, board));
                }
            }
        }
        return moves;
    }

    public boolean validMove(ChessMove move, TeamColor color, ChessBoard board) {
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());
        ChessPosition kingPosition;
        if (movingPiece.getPieceType() == ChessPiece.PieceType.KING) {
            kingPosition = move.getEndPosition();
        } else {
            kingPosition = findKingPosition(color);
        }

        ChessBoard simulatedBoard = board.copy();
        simulatedBoard.makeMove(move);
        return !isKingAttacked(kingPosition, simulatedBoard);
    }

    private Collection<ChessMove> getValidMoves(ChessPosition startPosition, ChessBoard board) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return new ArrayList<>();
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoveList = new ArrayList<>();
        for (ChessMove move: moves) {
            if (validMove(move, piece.getTeamColor(), board)) {
                validMoveList.add(move);
            }
        }
        return validMoveList;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return getValidMoves(startPosition, board);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece == null) {
            throw new InvalidMoveException("Can't Move an invalid piece");
        }
        if (piece.getTeamColor() != nextTurn) {
            throw new InvalidMoveException("It is not your turn");
        }
        if (isInCheckmate(TeamColor.WHITE) || isInCheckmate(TeamColor.BLACK)) {
            throw new InvalidMoveException("Game has ended. Moves can't be made.");
        }
        if (isInStalemate(TeamColor.WHITE) || isInStalemate(TeamColor.BLACK)) {
            throw new InvalidMoveException("Game has ended in a draw. Moves can't be made.");
        }
        if (resigned != null) {
            throw new InvalidMoveException("User " + resigned + " has resigned. Game Over.");
        }

        Collection<ChessMove> validPositionMoves = validMoves(move.getStartPosition());

        if (validPositionMoves.contains(move)) {
            board.makeMove(move);
            setTeamTurn((nextTurn == TeamColor.WHITE) ? TeamColor.BLACK: TeamColor.WHITE);
            return;
        }
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return inCheck(teamColor, board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return getAllValidMoves(teamColor, board).isEmpty() && isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return getAllValidMoves(teamColor, board).isEmpty() && !isInCheck(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
