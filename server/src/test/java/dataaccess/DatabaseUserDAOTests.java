package dataaccess;

import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class DatabaseUserDAOTests extends TestUtilsDAO {
    @Test
    @Order(1)
    @DisplayName("Valid Clearing of User Table")
    public void clear() {
        try {
            userDatabase.clear();
            assert userDatabase.getUser(user.username()) == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Valid User in Database")
    public void validGetUser() {
        try {
            UserData response = userDatabase.getUser(user.username());
            assert Objects.equals(response.username(), user.username());
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(3)
    @DisplayName("User not in Database")
    public void invalidGetUser() {
        try {
            UserData response = userDatabase.getUser("NotAUser");
            assert response == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(4)
    @DisplayName("Valid Create User")
    public void validCreateUser() {
        try {
            UserData response = userDatabase.createUser("New User", "password", "test@test.com");
            assert Objects.equals(response.username(), "New User");
            assert !Objects.equals(response.password(), "password");
            assert Objects.equals(response.email(), "test@test.com");
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(5)
    @DisplayName("Empty Password and Email")
    public void invalidCreateUser() {
        try {
            userDatabase.createUser("New User", null, null);
            assert false;
        } catch (DataAccessException e) {
            assert true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
}
