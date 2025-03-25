package ui;

import model.GameData;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    private boolean loggedIn = false;
    private final ServerFacade SERVER_FACADE;
    private final Scanner SCANNER = new Scanner(System.in);
    private final String[] PRE_LOGIN_COMMANDS = {"Help", "Quit", "Login", "Register"};
    private final String[] POST_LOGIN_COMMANDS = {"Help", "Logout", "Create", "List", "Join", "Observe"};

    private String authToken;
    private String username;
    private Collection<GameData> games;
    public Client(ServerFacade serverFacade) {
        this.SERVER_FACADE = serverFacade;
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
            case "Quit"         -> Quit();
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
            System.out.print(">>> ");
            String command = SCANNER.nextLine();
            if (Arrays.stream(PRE_LOGIN_COMMANDS).anyMatch(n -> Objects.equals(n, command))) {
                return command;
            }
            System.out.println("Invalid Command");
        }
    }

    private String PostLoginUI() {
        while (true) {
            System.out.print(">>> ");
            String command = SCANNER.nextLine();
            if (Arrays.stream(POST_LOGIN_COMMANDS).anyMatch(n -> Objects.equals(n, command))) {
                return command;
            }
            System.out.println("Invalid Command");
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

    private void Quit() {
        System.out.println("Quit");
    }

    private void Login() {
        System.out.println("Login");
        loggedIn = true;
    }

    private void Register() {
        System.out.println("Register");
    }

    private void Logout() {
        System.out.println("Logout");
        loggedIn = false;
    }

    private void CreateGame() {
        System.out.println("Create Game");
    }

    private void ListGame() {
        System.out.println("List Games");
    }

    private void JoinGame() {
        System.out.println("Join Game");
    }

    private void ObserverGame() {
        System.out.println("Observe Game");
    }
}
