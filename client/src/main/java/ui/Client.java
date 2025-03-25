package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.AlreadyTaken;
import exception.Unauthorized;
import model.AuthData;
import model.GameData;

import java.util.*;

public class Client {

    private int ui = 0;
    private final ServerFacade serverFacade;
    private final Scanner scanner = new Scanner(System.in);
    private final String[] preLoginCommands = {"Help", "Quit", "Login", "Register"};
    private final String[] postLoginCommands = {"Help", "Logout", "Create", "List", "Join", "Observe"};

    private String authToken;
    private ArrayList<GameData> games;
    private GameData currentGame;
    private ChessGame.TeamColor color;
    private boolean observer;
    public Client(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
        color = null;
        currentGame = null;
        observer = false;
        help();
        String command = "";
        while (!command.equals("Quit")) {
            command = switch (ui) {
                case 0 -> preLoginUI();
                case 1 -> postLoginUI();
                case 2 -> gameplayUI();
                default -> throw new IllegalStateException("Unexpected value: " + ui);
            };

            processCommand(command);
        }
    }

    private void processCommand(String command) {
        switch (command) {
            case "Help"         -> help();
            case "Quit"         -> {}
            case "Login"        -> login();
            case "Register"     -> register();
            case "Logout"       -> logout();
            case "Create"       -> createGame();
            case "List"         -> listGame();
            case "Join"         -> joinGame();
            case "Observe"      -> observerGame();
            case null, default  -> System.out.println("Invalid Command");
        }
    }

    private String preLoginUI() {
        while (true) {
            String command = promptString("");
            if (Arrays.asList(preLoginCommands).contains(command)) {
                return command;
            }
            System.out.println("Invalid Command");
        }
    }

    private String postLoginUI() {
        while (true) {
            String command = promptString("");
            if (Arrays.asList(postLoginCommands).contains(command)) {
                return command;
            }
            System.out.println("Invalid Command");
        }
    }

    private String gameplayUI() {
        printBoard();
        observer = false;
        currentGame = null;
        color = null;
        ui = 1;
        return "Help";
    }

    private String promptString(String prompt) {
        System.out.println(prompt);
        System.out.print(">>> ");
        return scanner.nextLine();
    }

    private int promptInt() {
        System.out.println("What is the number next to the game you want to join?");
        System.out.print(">>> ");
        return scanner.nextInt();
    }

    private void printBoard() {
        ChessBoard board = new ChessGame().getBoard();
        if (observer || color == ChessGame.TeamColor.WHITE) {
            printBoardWhite(board);
        } else {
            printBoardBlack(board);
        }
    }

    private void printBoardWhite(ChessBoard board) {
        boolean light = true;
        for (int i = 1; i <= 8; i++) {
            light = !light;
            StringBuilder string = new StringBuilder();
            for (int j = 1; j <= 8; j++) {
                light = drawRow(board, i, j, light, string);
            }
            string.append(EscapeSequences.RESET_BG_COLOR);
            System.out.println(string);
        }
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

    private void printBoardBlack(ChessBoard board) {
        boolean light = false;
        for (int i = 8; i >= 1; i--) {
            light = !light;
            StringBuilder string = new StringBuilder();
            for (int j = 8; j >= 1; j--) {
                light = drawRow(board, i, j, light, string);
            }
            string.append(EscapeSequences.RESET_BG_COLOR);
            System.out.println(string);
        }
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

    private void printGames() {
        if (games == null || games.isEmpty()) {
            System.out.println("No Games Available");
            return;
        }

        int gameCounter = 1;
        for (GameData game : games) {
            String gameName = game.gameName();
            String white = game.whiteUsername();
            String black = game.blackUsername();
            String output = String.format("%d -- Game: %s, White: %s, Black: %s", gameCounter, gameName, white, black);
            System.out.println(output);
            gameCounter += 1;
        }
    }

    private GameData getGameIDByNumber(int gameNumber) {
        if (games == null || games.isEmpty()) {
            return null;
        }

        if (gameNumber < 1 || gameNumber > games.size()) {
            System.out.println("Invalid game number");
            return null;
        }

        return games.get(gameNumber - 1);
    }

    private GameData getGame() {
        while (true) {
            int gameNum = promptInt();

            GameData game = getGameIDByNumber(gameNum);

            if (game != null) {
                return game;
            }
            System.out.println("Invalid Game Number.");
        }
    }

    public ChessGame.TeamColor getTeamColor() {
        while (true) {
            String response = promptString("What Color do your want to Join? WHITE or BLACK?");

            if (Objects.equals(response, "WHITE")) {
                return ChessGame.TeamColor.WHITE;
            }

            if (Objects.equals(response, "BLACK")) {
                return ChessGame.TeamColor.BLACK;
            }

            System.out.println("Invalid Color.");
        }
    }

    private void help() {
        System.out.println("Help - Displays commands the user can run");
        switch (ui) {
            case 0 -> {
                System.out.println("Quit - Exits the program");
                System.out.println("Login - Allows for signing in");
                System.out.println("Register - Allows for creating a new account");}
            case 1 -> {
                System.out.println("Logout - Allows for signing out");
                System.out.println("Create - Allows for creating a new Chess game.");
                System.out.println("List - Will list all Chess Games");
                System.out.println("Join - Allows for joining a Chess Game");
                System.out.println("Observe - Allows for watching a Chess Game");
            }
        }
    }

    private void login() {
        String username = promptString("Enter Your Username:");
        String password = promptString("Enter Your Password:");

        try {
            AuthData authData = serverFacade.login(username, password);
            authToken = authData.authToken();
        } catch (Unauthorized e) {
            System.out.println("Invalid Login");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        ui = 1;
    }

    private void register() {
        String username = promptString("Enter Your Desired Username:");
        String password = promptString("Enter Your Desired Password:");
        String email = promptString("Enter Your Email Address:");

        try {
            AuthData authData = serverFacade.register(username, password, email);
            authToken = authData.authToken();
        } catch (AlreadyTaken e) {
            System.out.println("Username Already Taken.");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        ui = 1;
    }

    private void logout() {
        try {
            serverFacade.logout(authToken);
            authToken = "";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        ui = 0;
    }

    private void createGame() {
        String gameName = promptString("What is the name of your game?");

        try {
            serverFacade.createGame(authToken, gameName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void listGame() {
        try {
            this.games = serverFacade.listGames(authToken);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printGames();
    }

    private void joinGame() {
        listGame();
        if (games == null || games.isEmpty()) {
            return;
        }
        GameData game = getGame();
        ChessGame.TeamColor color = getTeamColor();

        try {
            serverFacade.joinGame(authToken, color, game.gameID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        currentGame = game;
        this.color = color;
        ui = 2;
    }

    private void observerGame() {
        listGame();
        if (games == null || games.isEmpty()) {
            return;
        }
        GameData game = getGame();

        try {
            serverFacade.observeGame(authToken, game.gameID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        currentGame = game;
        color = ChessGame.TeamColor.WHITE;
        observer = true;
        ui = 2;
    }
}
