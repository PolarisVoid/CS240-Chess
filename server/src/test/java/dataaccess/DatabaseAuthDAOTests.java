package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class DatabaseAuthDAOTests extends TestUtilsDAO {
    @Test
    @Order(1)
    @DisplayName("Valid Clearing of Auth Table")
    public void clear() {
        try {
            authDatabase.clear();
            assert authDatabase.getAuth(auth.authToken()) == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(1)
    @DisplayName("Valid creation of Auth")
    public void validCreateAuth() {
        try {
            AuthData response = authDatabase.createAuth(user.username());
            assert response.authToken() != null;
            assert Objects.equals(response.username(), user.username());
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("User not in Database")
    public void invalidCreateAuth() {
        try {
            authDatabase.createAuth("userNotInDatabase");
            assert false;
        } catch (DataAccessException e) {
            assert true;
        }
    }

    @Test
    @Order(3)
    @DisplayName("Valid Auth token")
    public void validGetAuth() {
        try {
            AuthData response = authDatabase.getAuth(auth.authToken());
            assert Objects.equals(response.authToken(), auth.authToken());
            assert Objects.equals(response.username(), auth.username());
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(4)
    @DisplayName("Auth token not in database")
    public void invalidGetAuth() {
        try {
            AuthData response = authDatabase.getAuth("209876545678");
            assert response == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(5)
    @DisplayName("Deleting auth token in database")
    public void validDeleteAuth() {
        try {
            authDatabase.deleteAuth(auth.authToken());
            assert authDatabase.getAuth(auth.authToken()) == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(6)
    @DisplayName("Deleting auth token not in database")
    public void invalidDeleteAuth() {
        try {
            authDatabase.deleteAuth("1");
            assert authDatabase.getAuth(auth.authToken()) != null;
        } catch (DataAccessException e) {
            assert false;
        }
    }
}
