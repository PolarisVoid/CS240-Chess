package service;

import dataaccess.*;
import exceptions.UnathorizedException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import requests.RegisterRequest;
import responses.RegisterResponse;
import services.BaseService;
import services.RegisterService;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseServiceTests {

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
        }
    }

    @Test
    @Order(1)
    @DisplayName("Valid AuthToken")
    public void validAuth() {
        try {
            AuthData authData = BaseService.authenticate(userResponse.getAuthToken());
            Assertions.assertEquals(authData.username(), user.username());

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Invalid AuthToken")
    public void invalidAuth() {
        try {
            BaseService.authenticate("101");
            assert false;
        } catch (UnathorizedException e) {
            assert true;
        } catch (DataAccessException e) {
            assert false;
        }
    }
}
