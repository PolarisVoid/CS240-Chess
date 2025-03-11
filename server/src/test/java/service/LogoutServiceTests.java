package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import requests.LogoutRequest;

public class LogoutServiceTests extends TestUtils {
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
