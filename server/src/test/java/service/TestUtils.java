package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import requests.CreateGameRequest;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.RegisterResponse;

public class TestUtils {
    public static AuthDAO authDatabase;
    public static GameDAO gameDatabase;
    public static UserDAO userDatabase;

    public static UserData user;
    public static RegisterResponse userResponse;
    public static CreateGameResponse gameResponse;

    @BeforeAll
    public static void init() {
        authDatabase = new DatabaseAuthDAO();
        gameDatabase = new DatabaseGameDAO();
        userDatabase = new DatabaseUserDAO();

        user = new UserData("TestUser", "password", "test@test.com");
    }

    @BeforeEach
    public void setup() {
        userDatabase.clear();
        authDatabase.clear();
        gameDatabase.clear();

        try {
            userResponse = RegisterService.register(new RegisterRequest(user.username(), user.password(), user.email()));
            CreateGameRequest createGameRequest = new CreateGameRequest("Game");
            createGameRequest.setAuthToken(userResponse.getAuthToken());
            gameResponse = CreateGameService.createGame(createGameRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
}
