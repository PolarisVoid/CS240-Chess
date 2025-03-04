package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.RegisterRequest;
import responses.RegisterResponse;
import services.ClearDatabaseService;
import services.CreateGameService;
import services.RegisterService;

public class ClearDatabaseServiceTests {

    private static AuthDAO authDatabase;
    private static GameDAO gameDatabase;
    private static UserDAO userDatabase;

    private static UserData user;


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
            RegisterResponse userResponse = RegisterService.register(new RegisterRequest(user.username(), user.password(), user.email()));
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
    @DisplayName("Clear Database")
    public void ClearDatabase() {
        try {
            ClearDatabaseService.clearDatabase();
        } catch (Exception e) {
            assert false;
        }
        assert MemoryGameDAO.Database.isEmpty();
        assert MemoryGameDAO.counter == 1;
        assert MemoryAuthDAO.Database.isEmpty();
        assert MemoryUserDAO.Database.isEmpty();
    }
}
