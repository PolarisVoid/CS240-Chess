package service;

import dataaccess.*;
import exceptions.UnathorizedException;
import model.AuthData;
import org.junit.jupiter.api.*;

public class BaseServiceTests extends TestUtils {
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
