package ui;

import chess.ChessGame;
import exceptions.AlreadyTaken;
import exceptions.Unauthorized;
import model.AuthData;
import model.GameData;

import java.util.*;

public class Client {

    private boolean loggedIn = false;
    private final ServerFacade SERVER_FACADE;
    private final Scanner SCANNER = new Scanner(System.in);
    private final String[] PRE_LOGIN_COMMANDS = {"Help", "Quit", "Login", "Register"};
    private final String[] POST_LOGIN_COMMANDS = {"Help", "Logout", "Create", "List", "Join", "Observe"};

    private String authToken;
    private ArrayList<GameData> games;
    public Client(ServerFacade serverFacade) {
        this.SERVER_FACADE = serverFacade;
        Help();
        String command = "";
        while (!command.equals("Quit")) {
            if (!loggedIn) {
                command = PreLoginUI();
            } else {
                command = PostLoginUI();
            }

            ProcessCommand(command);
        }
    }

    private void ProcessCommand(String command) {
        switch (command) {
            case "Help"         -> Help();
            case "Quit"         -> { return; }
            case "Login"        -> Login();
            case "Register"     -> Register();
            case "Logout"       -> Logout();
            case "Create"       -> CreateGame();
            case "List"         -> ListGame();
            case "Join"         -> JoinGame();
            case "Observe"      -> ObserverGame();
            case null, default  -> System.out.println("Invalid Command");
        }
    }

    private String PreLoginUI() {
        while (true) {
            String command = promptString("");
            if (Arrays.stream(PRE_LOGIN_COMMANDS).anyMatch(n -> Objects.equals(n, command))) {
                return command;
            }
            System.out.println("Invalid Command");
        }
    }

    private String PostLoginUI() {
        while (true) {
            String command = promptString("");
            if (Arrays.stream(POST_LOGIN_COMMANDS).anyMatch(n -> Objects.equals(n, command))) {
                return command;
            }
            System.out.println("Invalid Command");
        }
    }

    private String promptString(String prompt) {
        System.out.println(prompt);
        System.out.print(">>> ");
        return SCANNER.nextLine();
    }

    private int promptInt(String prompt) {
        System.out.println(prompt);
        System.out.print(">>> ");
        return SCANNER.nextInt();
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

    private int getGame() {
        while (true) {
            int gameNum = promptInt("What is the number next to the game you want to join?");

            GameData game = getGameIDByNumber(gameNum);

            if (game != null) {
                return game.gameID();
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

    private void Help() {
        System.out.println("Help - Displays commands the user can run");
        if (!loggedIn) {
            System.out.println("Quit - Exits the program");
            System.out.println("Login - Allows for signing in");
            System.out.println("Register - Allows for creating a new account");
        } else {
            System.out.println("Logout - Allows for signing out");
            System.out.println("Create - Allows for creating a new Chess game.");
            System.out.println("List - Will list all Chess Games");
            System.out.println("Join - Allows for joining a Chess Game");
            System.out.println("Observe - Allows for watching a Chess Game");
        }
    }

    private void Login() {
        String username = promptString("Enter Your Username:");
        String password = promptString("Enter Your Password:");

        try {
            AuthData authData = SERVER_FACADE.Login(username, password);
            authToken = authData.authToken();
        } catch (Unauthorized e) {
            System.out.println("Invalid Login");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        loggedIn = true;
    }

    private void Register() {
        String username = promptString("Enter Your Desired Username:");
        String password = promptString("Enter Your Desired Password:");
        String email = promptString("Enter Your Email Address:");

        try {
            AuthData authData = SERVER_FACADE.Register(username, password, email);
            authToken = authData.authToken();
        } catch (AlreadyTaken e) {
            System.out.println("Username Already Taken.");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        loggedIn = true;
    }

    private void Logout() {
        try {
            SERVER_FACADE.Logout(authToken);
            authToken = "";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        loggedIn = false;
    }

    private void CreateGame() {
        String gameName = promptString("What is the name of your game?");

        try {
            SERVER_FACADE.CreateGame(authToken, gameName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void ListGame() {
        try {
            this.games = SERVER_FACADE.ListGames(authToken);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printGames();
    }

    private void JoinGame() {
        ListGame();
        if (games == null || games.isEmpty()) {
            return;
        }
        int gameID = getGame();
        ChessGame.TeamColor color = getTeamColor();

        try {
            SERVER_FACADE.JoinGame(authToken, color, gameID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void ObserverGame() {
        ListGame();
        if (games == null || games.isEmpty()) {
            return;
        }
        int gameID = getGame();

        try {
            SERVER_FACADE.ObserveGame(authToken, gameID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
