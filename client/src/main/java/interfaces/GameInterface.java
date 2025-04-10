package interfaces;

import chess.*;
import facades.WSClient;
import model.GameData;
import ui.Client;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GameInterface extends Interface {
    private static final String[] commands = {"Help", "Redraw", "Leave", "Move", "Resign", "Legal Moves"};
    private GameData game;
    private final ChessGame.TeamColor color;
    private final boolean observer;
    private ArrayList<ChessMove> highlightMoves = new ArrayList<>();
    private ChessPosition chessPiecePosition = null;

    public GameInterface(Client client, GameData game, ChessGame.TeamColor color, boolean observer) {
        super(client);
        this.game = game;
        this.color = color;
        this.observer = observer;
        help();
    }

    public String ui() {
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

    public void setChessGame(ChessGame chessGame) {
        this.game = new GameData(game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername(), chessGame);
        System.out.println();
        help();
        printBoard();
        System.out.print(">>> ");
        System.out.flush();
    }

    public void redrawChessBoard() {printBoard();}

    public void leaveGame() {
        ChessGame chessGame = game.game();
        if (chessGame == null) {
            System.out.println("Game board doesn't Exist");
            serverFacade.leave(authToken, game.gameID());
            client.setInterface(new PostLoginInterface(this.client));
            return;
        }

        if (chessGame.getResigned() != null) {
            serverFacade.resign(authToken, game.gameID());
        }

        serverFacade.leave(authToken, game.gameID());
        client.setInterface(new PostLoginInterface(this.client));
    }

    public void makeMove() {
        System.out.println("What is the starting position?");
        int startRow = getRow();
        int startCol = getCol();
        ChessPosition startPosition = new ChessPosition(startRow, startCol);

        System.out.println("What is the ending Position?");
        int endRow = getRow();
        int endCol = getCol();
        ChessPosition endPosition = new ChessPosition(endRow, endCol);

        ChessPiece.PieceType chessPiece = null;
        if ((color == ChessGame.TeamColor.BLACK && endRow == 1)
         || (color == ChessGame.TeamColor.WHITE && endRow == 8)) {
            chessPiece = getChessPiece();
        }

        ChessMove chessMove = new ChessMove(startPosition, endPosition, chessPiece);
        ChessGame chessGame = game.game();
        ChessBoard board = chessGame.getBoard();
        ArrayList<ChessMove> chessMoves = (ArrayList<ChessMove>) chessGame.getAllValidMoves(color, board);
        if (!chessMoves.contains(chessMove)) {
            System.out.println("Move is not valid");
            return;
        }
        serverFacade.makeMove(authToken, game.gameID(), chessMove);
    }

    private ChessPiece.PieceType getChessPiece() {
        while (true) {
            String piece = promptString("Piece: ");

            switch (piece) {
                case "KNIGHT" -> {return ChessPiece.PieceType.KNIGHT;}
                case "BISHOP" -> {return ChessPiece.PieceType.BISHOP;}
                case "ROOK" -> {return ChessPiece.PieceType.ROOK;}
                case "QUEEN" -> {return ChessPiece.PieceType.QUEEN;}
            }
            System.out.println("Invalid Piece. Pieces: KNIGHT, BISHOP, ROOK, QUEEN");
        }
    }

    public void resign() {
        serverFacade.resign(authToken, game.gameID());
    }

    public void highlightLegalMoves() {
        System.out.println("What is the position of the Piece?");
        int row = getRow();
        int col = getCol();
        ChessPosition chessPosition = new ChessPosition(row, col);

        ChessGame chessGame = game.game();
        ChessBoard board = chessGame.getBoard();
        ChessPiece piece = board.getPiece(chessPosition);
        highlightMoves = (ArrayList<ChessMove>) piece.pieceMoves(board, chessPosition);
        chessPiecePosition = chessPosition;
        printBoard();
        highlightMoves = new ArrayList<>();
    }

    private boolean containsMove(ChessPosition chessPosition) {
        for (ChessMove chessMove : highlightMoves) {
            if (Objects.equals(chessPosition, chessMove.getEndPosition())) {
                return true;
            }
        }
        return false;
    }

    private void printBoard() {
        ChessGame chessGame = game.game();
        if (chessGame == null) {
            System.out.println("Game board hasn't been retried");
            return;
        }
        ChessBoard board = chessGame.getBoard();
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
        ChessPosition chessPosition = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(chessPosition);
        String lightString;
        if (chessPosition == chessPiecePosition) {
            lightString = EscapeSequences.SET_BG_COLOR_YELLOW;
        } else if (containsMove(chessPosition)) {
            lightString = light ? EscapeSequences.SET_BG_COLOR_GREEN : EscapeSequences.SET_BG_COLOR_DARK_GREEN;
        } else {
            lightString = light ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
        }

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

    private int getCol() {
        while (true) {
            String col = promptString("Column Letter: ");

            if (col.length() != 1) {
                System.out.println("Please enter a valid single column letter.");
                continue;
            }
            char columnChar = col.toLowerCase().charAt(0);

            switch (columnChar) {
                case 'a': return 1;
                case 'b': return 2;
                case 'c': return 3;
                case 'd': return 4;
                case 'e': return 5;
                case 'f': return 6;
                case 'g': return 7;
                case 'h': return 8;
            }
            System.out.println("Invalid column letter. Please enter a valid column letter (a-h).");
        }
    }

    private int getRow() {
        while (true) {
            int row;
            try {
                row = promptInt("Row Number: ");
            } catch (Exception e) {
                System.out.println("Invalid Row. Please enter a number between 1 and 8");
                scanner.nextLine();
                continue;
            }

            if (0 < row && row < 9) {
                return row;
            }
            System.out.println("Invalid Row. Please enter a number between 1 and 8");
        }
    }
}
