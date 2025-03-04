package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.RegisterResponse;
import services.CreateGameService;
import services.LogoutService;
import services.RegisterService;

public class LogoutServiceTests {
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
    @DisplayName("Valid Logout")
    public void validLogout() {
        try {
            LogoutService.logout(new LogoutRequest(userResponse.getAuthToken()));
            assert authDatabase.getAuth(userResponse.getAuthToken()) == null;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Invalid Logout")
    public void invalidLogout() {
        try {
            LogoutService.logout(new LogoutRequest("101"));
            assert false;
        } catch (Exception e) {
            assert true;
            try {
                assert authDatabase.getAuth(userResponse.getAuthToken()) != null;
            } catch (DataAccessException ex) {
                assert false;
            }
        }
    }
}
