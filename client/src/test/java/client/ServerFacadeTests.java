package client;

import chess.ChessGame;
import exception.Unauthorized;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import facades.ServerFacade;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static int port;
    private static ServerFacade serverFacade;
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static AuthData authData;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void createUser() {
        try {
            String url = "http://localhost:" + port + "/db";
            URI uri = new URI(url);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("DELETE");
            http.connect();
            System.out.println(http.getResponseCode());
        } catch (Exception ignore) {}

        try {
            String email = "test@email.com";
            authData = serverFacade.register(USERNAME, PASSWORD, email);
        } catch (Exception ignore) {}
    }

    @Test
    @DisplayName("Valid Login")
    public void positiveLogin() {
        try {
            AuthData authData = serverFacade.login(USERNAME, PASSWORD);
            assert true;
            assert authData.authToken() != null;
            assert authData.username() != null;
        } catch (Exception ignore) {
            assert false;
        }
    }

    @Test
    @DisplayName("Invalid Login")
    public void negativeLogin() {
        try {
            serverFacade.login("Fake user", PASSWORD);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Valid Register")
    public void positiveRegister() {
        try {
            AuthData authData = serverFacade.register("Random User", "fake", "test");
            assert true;
            assert authData != null;
            assert authData.username() != null;
            assert authData.authToken() != null;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Invalid Register")
    public void negativeRegister() {
        try {
            serverFacade.register(USERNAME, "", "");
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Valid Logout")
    public void positiveLogout() {
        try {
            serverFacade.logout(authData.authToken());
            assert true;
        } catch (Exception e) {
            assert false;
        }

        try {
            serverFacade.listGames(authData.authToken());
            assert false;
        } catch (Unauthorized e) {
            assert true;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Invalid Logout")
    public void negativeLogout() {
        try {
            serverFacade.logout("1");
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Valid Create Game")
    public void positiveCreateGame() {
        try {
            int gameID = serverFacade.createGame(authData.authToken(), "New Game");
            assert gameID >= 1;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Invalid Create Game")
    public void negativeCreateGame() {
        try {
            serverFacade.createGame("12", "New Game");
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Valid List Games")
    public void positiveListGames() {
        try {
            serverFacade.createGame(authData.authToken(), "New Game");
            ArrayList<GameData> games = serverFacade.listGames(authData.authToken());
            assert !games.isEmpty();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Invalid List Games")
    public void negativeListGames() {
        try {
            serverFacade.listGames("1");
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Valid Join Game")
    public void positiveJoinGame() {
        try {
            int gameID = serverFacade.createGame(authData.authToken(), "New Game");
            serverFacade.joinGame(authData.authToken(), ChessGame.TeamColor.WHITE, gameID);
            assert true;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Invalid Join Game")
    public void negativeJoinGame() {
        try {
            serverFacade.joinGame(authData.authToken(), ChessGame.TeamColor.WHITE, 1000);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Valid Observe Game -- Implemented in next phase")
    public void positiveObserveGame() {
        assert true;
    }
}
