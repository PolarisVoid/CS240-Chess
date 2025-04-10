package interfaces;

import chess.ChessGame;
import exception.AlreadyTaken;
import model.GameData;
import ui.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PostLoginInterface extends Interface {
    private static final String[] COMMANDS = {"Help", "Logout", "Create", "List", "Join", "Observe"};
    private ArrayList<GameData> games = new ArrayList<>();

    public PostLoginInterface(Client client) {
        super(client, COMMANDS);
        help();
    }

    public void help() {
        System.out.println("Help - Displays commands the user can run");
        System.out.println("Logout - Allows for signing out");
        System.out.println("Create - Allows for creating a new Chess game.");
        System.out.println("List - Will list all Chess Games");
        System.out.println("Join - Allows for joining a Chess Game");
        System.out.println("Observe - Allows for watching a Chess Game");
    }

    public void logout() {
        try {
            serverFacade.logout(authToken);
            client.setAuthToken("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        client.setInterface(new PreLoginInterface(client));
    }

    public void createGame() {
        String gameName = promptString("What is the name of your game?");

        try {
            serverFacade.createGame(authToken, gameName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void listGame() {
        try {
            this.games = serverFacade.listGames(authToken);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printGames();
    }

    public void joinGame() {
        listGame();
        if (games == null || games.isEmpty()) {
            return;
        }
        GameData game = getGame();
        ChessGame.TeamColor color = getTeamColor();

        try {
            serverFacade.joinGame(authToken, color, game.gameID());
            serverFacade.connect(authToken, game.gameID());
        } catch (AlreadyTaken e) {
            System.out.println("Already Taken");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        client.setInterface(new GameInterface(client, game, color, false));
    }

    public void observeGame() {
        listGame();
        if (games == null || games.isEmpty()) {
            return;
        }
        GameData game = getGame();

        try {
            serverFacade.connect(authToken, game.gameID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        client.setInterface(new GameInterface(client, game, ChessGame.TeamColor.WHITE, true));
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

    private GameData getGame() {
        while (true) {
            int gameNum;
            try {
                gameNum = promptInt("What is the number next to the game you want to join?");
            } catch (Exception e) {
                System.out.println("Invalid Game Number.");
                SCANNER.nextLine();
                continue;
            }
            GameData game = getGameIDByNumber(gameNum);

            if (game != null) {
                return game;
            }
            System.out.println("Invalid Game Number.");
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

    static ChessGame.TeamColor getTeamColor() {
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
}
