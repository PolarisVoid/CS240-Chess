package service;

import org.junit.jupiter.api.*;
import requests.LoginRequest;
import responses.LoginResponse;

import java.util.Objects;

public class LoginServiceTests extends TestUtils {
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
