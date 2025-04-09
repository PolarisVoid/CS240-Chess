package interfaces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.Client;
import ui.EscapeSequences;

import java.util.Arrays;

public class GameInterface extends Interface {
    private static final String[] commands = {"Help", "Redraw", "Leave", "Move", "Resign", "Legal Moves"};
    private final GameData game;
    private final ChessGame.TeamColor color;
    private final boolean observer;

    public GameInterface(Client client, GameData game, ChessGame.TeamColor color, boolean observer) {
        super(client);
        this.game = game;
        this.color = color;
        this.observer = observer;
    }

    public String ui() {
        printBoard();
        while (true) {
            String command = promptString("");
            if (Arrays.asList(commands).contains(command)) {
                return command;
            }
            System.out.println("Invalid Command");
        }
    }

    public void help() {
        System.out.println("Help - Displays commands the user can run");
        System.out.println("Redraw - Displays commands the user can run");
        System.out.println("Leave - Displays commands the user can run");
        System.out.println("Move - Displays commands the user can run");
        System.out.println("Resign - Displays commands the user can run");
        System.out.println("Legal Moves - Displays commands the user can run");
    }

    public void redrawChessBoard() {printBoard();}
    public void leaveGame() {}
    public void makeMove() {}
    public void resign() {}
    public void highlightLegalMoves() {}

    private void printBoard() {
        ChessBoard board = game.game().getBoard();
        if (observer || color == ChessGame.TeamColor.WHITE) {
            printBoardWhite(board);
        } else {
            printBoardBlack(board);
        }
    }

    private void printBoardWhite(ChessBoard board) {
        boolean light = false;
        System.out.println(createHeader(color));
        for (int i = 8; i >= 1; i--) {
            light = !light;
            StringBuilder string = new StringBuilder();
            string.append(EscapeSequences.SET_BG_COLOR_BLACK).append(String.format(" %d ", i));
            for (int j = 1; j <= 8; j++) {
                light = drawRow(board, i, j, light, string);
            }
            string.append(EscapeSequences.SET_BG_COLOR_BLACK).append(String.format(" %d ", i));
            string.append(EscapeSequences.RESET_BG_COLOR);
            System.out.println(string);
        }
        System.out.println(createHeader(color));
    }

    private void printBoardBlack(ChessBoard board) {
        boolean light = false;
        System.out.println(createHeader(color));
        for (int i = 1; i <= 8; i++) {
            light = !light;
            StringBuilder string = new StringBuilder();
            string.append(EscapeSequences.SET_BG_COLOR_BLACK).append(String.format(" %d ", i));
            for (int j = 8; j >= 1; j--) {
                light = drawRow(board, i, j, light, string);
            }
            string.append(EscapeSequences.SET_BG_COLOR_BLACK).append(String.format(" %d ", i));
            string.append(EscapeSequences.RESET_BG_COLOR);
            System.out.println(string);
        }
        System.out.println(createHeader(color));
    }

    private String createHeader(ChessGame.TeamColor color) {
        return switch (color) {
            case WHITE -> {
                StringBuilder string = new StringBuilder();
                string.append(EscapeSequences.SET_BG_COLOR_BLACK);
                string.append("    a   b   c  d   e  f   g  h    ");
                string.append(EscapeSequences.RESET_BG_COLOR);
                yield string.toString();
            }
            case BLACK -> {
                StringBuilder string = new StringBuilder();
                string.append(EscapeSequences.SET_BG_COLOR_BLACK);
                string.append("    h   g   f  e   d  c   b  a    ");
                string.append(EscapeSequences.RESET_BG_COLOR);
                yield string.toString();
            }
            case null -> "";
        };
    }

    private boolean drawRow(ChessBoard board, int i, int j, boolean light, StringBuilder string) {
        ChessPiece piece = board.getPiece(new ChessPosition(i, j));
        String lightString = light ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
        if (piece == null) {
            string.append(lightString).append(EscapeSequences.EMPTY);
            return !light;
        }

        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType pieceType = piece.getPieceType();

        string.append(lightString).append(drawPiece(color, pieceType, light));
        return !light;
    }

    private String drawPiece(ChessGame.TeamColor color, ChessPiece.PieceType pieceType, boolean light) {
        String piece;
        if (color == ChessGame.TeamColor.WHITE) {
            piece = switch (pieceType) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        } else {
            piece = switch (pieceType) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
        return piece;
    }
}
