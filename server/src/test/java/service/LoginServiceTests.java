package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import services.LoginService;
import services.RegisterService;

import java.util.Objects;

public class LoginServiceTests {
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
            RegisterService.register(new RegisterRequest(user.username(), user.password(), user.email()));
        } catch (Exception e) {
            System.out.println("Error");
            assert false;
        }
    }

    @Test
    @Order(1)
    @DisplayName("Valid Login")
    public void validLogin() {
        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        try {
            LoginResponse loginResponse = LoginService.login(loginRequest);
            assert loginResponse.getAuthToken() != null;
            assert Objects.equals(loginResponse.getUsername(), user.username());
            assert authDatabase.getAuth(loginResponse.getAuthToken()) != null;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Invalid Login")
    public void invalidLogin() {
        try {
            LoginService.login(new LoginRequest("fakeUser", "fakePassword"));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
