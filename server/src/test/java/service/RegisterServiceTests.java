package service;

import model.UserData;
import org.junit.jupiter.api.*;
import requests.RegisterRequest;
import services.RegisterService;


public class RegisterServiceTests extends TestUtils {

    @Test
    @Order(1)
    @DisplayName("Valid Registration")
    public void validRegistration() {
        try {
            UserData user1 = new UserData("TestUser2", "Password2", "test@test.com");
            RegisterService.register(new RegisterRequest(user1.username(), user1.password(), user1.password()));
            assert true;
            assert userDatabase.getUser(user1.username()) != null;
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
