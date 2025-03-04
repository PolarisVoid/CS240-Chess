package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.GetGameRequest;
import requests.RegisterRequest;
import responses.GetGameResponse;
import responses.RegisterResponse;
import services.CreateGameService;
import services.GetGameService;
import services.RegisterService;

public class GetGameServiceTests {
    private static AuthDAO authDatabase;
    private static GameDAO gameDatabase;
    private static UserDAO userDatabase;

    private static UserData user;
    private static RegisterResponse userResponse;


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
            CreateGameService.createGame(createGameRequest);
        } catch (Exception e) {
            System.out.println("Error");
            assert false;
        }
    }

    @Test
    @Order(1)
    @DisplayName("Valid Get Game")
    public void validGetGame() {
        try {
            GetGameResponse getGameResponse = GetGameService.getGames(new GetGameRequest(userResponse.getAuthToken()));
            assert getGameResponse.getGames() != null;
            assert !getGameResponse.getGames().isEmpty();
        } catch (Exception e) {
            assert false;
        }

    }

    @Test
    @Order(2)
    @DisplayName("Invalid Get Game")
    public void invalidGetGame() {
        try {
            GetGameService.getGames(new GetGameRequest("101"));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
