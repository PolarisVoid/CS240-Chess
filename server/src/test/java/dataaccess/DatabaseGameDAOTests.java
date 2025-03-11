package dataaccess;

import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

public class DatabaseGameDAOTests extends TestUtilsDAO {
    @Test
    @Order(1)
    @DisplayName("Valid Clearing of Game Table")
    public void clear() {
        try {
            gameDatabase.clear();
            assert gameDatabase.getGame(game.gameID()) == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Valid creation of Game")
    public void validCreateGame() {
        try {
            GameData response = gameDatabase.createGame("NewGame");
            assert Objects.equals(response.gameName(), "NewGame");
            assert response.blackUsername() == null;
            assert response.whiteUsername() == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(3)
    @DisplayName("Empty Game Name")
    public void invalidCreateGame() {
        try {
            gameDatabase.createGame(null);
            assert false;
        } catch (DataAccessException e) {
            assert true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @Order(4)
    @DisplayName("Valid Game in Database")
    public void validGetGame() {
        try {
            GameData response = gameDatabase.getGame(game.gameID());
            assert Objects.equals(response.gameID(), game.gameID());
            assert Objects.equals(response.gameName(), game.gameName());
            assert Objects.equals(response.whiteUsername(), game.whiteUsername());
            assert Objects.equals(response.blackUsername(), game.blackUsername());
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(5)
    @DisplayName("Game not in Database")
    public void invalidGetGame() {
        try {
            GameData response = gameDatabase.getGame(1000);
            assert response == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(6)
    @DisplayName("Should return the games in the database.")
    public void validListGames() {
        try {
            ArrayList<GameData> response = gameDatabase.listGames();
            assert !response.isEmpty();
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(6)
    @DisplayName("Games should be cleared when database is cleared")
    public void invalidListGames() {
        try {
            gameDatabase.clear();
            ArrayList<GameData> response = gameDatabase.listGames();
            assert response.isEmpty();
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(7)
    @DisplayName("Valid Update to the game.")
    public void validUpdateGame() {
        try {
            gameDatabase.updateGame(game.gameID(), game.gameName(), user.username(), null, game.game());
            GameData response = gameDatabase.getGame(game.gameID());
            assert Objects.equals(response.gameName(), game.gameName());
            assert Objects.equals(response.whiteUsername(), user.username());
            assert response.blackUsername() == null;
        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    @Order(8)
    @DisplayName("Empty Update Game")
    public void invalidUpdateGame() {
        try {
            gameDatabase.updateGame(game.gameID(), null, null, null, null);
            assert false;
        } catch (DataAccessException e) {
            assert true;
        }
    }
}
