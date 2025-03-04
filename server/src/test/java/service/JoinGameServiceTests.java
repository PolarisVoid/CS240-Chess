package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.RegisterResponse;
import services.CreateGameService;
import services.JoinGameService;
import services.RegisterService;

import java.util.Objects;

public class JoinGameServiceTests {
    private static AuthDAO authDatabase;
    private static GameDAO gameDatabase;
    private static UserDAO userDatabase;

    private static UserData user;
    private static RegisterResponse userResponse;
    private static CreateGameResponse gameResponse;


    @BeforeAll
    public static void init() {
        authDatabase = new MemoryAuthDAO();
        gameDatabase = new MemoryGameDAO();
        userDatabase = new MemoryUserDAO();

        user = new UserData("TestUser", "password", "test@test.com");
    }

    @BeforeEach
    public void setup() {
        authDatabase.clear();
        gameDatabase.clear();
        userDatabase.clear();

        try {
            userResponse = RegisterService.register(new RegisterRequest(user.username(), user.password(), user.email()));
            CreateGameRequest createGameRequest = new CreateGameRequest("Game");
            createGameRequest.setAuthToken(userResponse.getAuthToken());
            gameResponse = CreateGameService.createGame(createGameRequest);
        } catch (Exception e) {
            System.out.println("Error");
            assert false;
        }
    }

    @Test
    @Order(1)
    @DisplayName("Valid Join Game")
    public void validJoinGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameResponse.getGameID());
        joinGameRequest.setAuthToken(userResponse.getAuthToken());

        try {
            JoinGameService.joinGame(joinGameRequest);
            GameData gamedata = gameDatabase.listGames().getFirst();
            assert Objects.equals(gamedata.whiteUsername(), user.username());
            assert gamedata.blackUsername() == null;
            assert gamedata.gameID() == gameResponse.getGameID();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Invalid Join Game")
    public void invalidJoinGame() {
        try {
            JoinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
