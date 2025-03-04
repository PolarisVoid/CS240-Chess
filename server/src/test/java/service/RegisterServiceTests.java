package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.RegisterRequest;
import services.RegisterService;


public class RegisterServiceTests {
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
    }

    @Test
    @Order(1)
    @DisplayName("Valid Registration")
    public void validRegistration() {
        try {
            RegisterService.register(new RegisterRequest(user.username(), user.password(), user.password()));
            assert true;
            assert userDatabase.getUser(user.username()) != null;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Invalid Registration")
    public void invalidRegistration() {
        try {
            RegisterService.register(new RegisterRequest(user.username(), user.password(), user.password()));
            RegisterService.register(new RegisterRequest(user.username(), user.password(), user.password()));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
