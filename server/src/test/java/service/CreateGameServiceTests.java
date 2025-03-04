package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.RegisterResponse;
import services.CreateGameService;
import services.RegisterService;

public class CreateGameServiceTests {

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
        } catch (Exception e) {
            System.out.println("Error");
            assert false;
        }
    }

    @Test
    @Order(1)
    @DisplayName("Create Valid Game")
    public void createValidGame() {
        CreateGameRequest createGameRequest = new CreateGameRequest("TestGame");
        createGameRequest.setAuthToken(userResponse.getAuthToken());
        try {
            CreateGameResponse createGameResponse = CreateGameService.createGame(createGameRequest);
            assert createGameResponse.getGameID() == 1;
            assert !gameDatabase.listGames().isEmpty();
            assert gameDatabase.getGame(createGameResponse.getGameID()) != null;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Create Invalid Game")
    public void createInvalidGame() {
        try {
            CreateGameService.createGame(new CreateGameRequest("TestGame"));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
