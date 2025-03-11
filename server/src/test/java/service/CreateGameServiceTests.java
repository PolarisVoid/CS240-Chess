package service;

import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import responses.CreateGameResponse;

public class CreateGameServiceTests extends TestUtils {
    @Test
    @Order(1)
    @DisplayName("Create Valid Game")
    public void createValidGame() {
        CreateGameRequest createGameRequest = new CreateGameRequest("TestGame");
        createGameRequest.setAuthToken(userResponse.getAuthToken());
        try {
            CreateGameResponse createGameResponse = CreateGameService.createGame(createGameRequest);
            assert createGameResponse.getGameID() == 2;
            assert !gameDatabase.listGames().isEmpty();
            assert gameDatabase.getGame(createGameResponse.getGameID()) != null;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Create Invalid Game")
    public void createInvalidGame() {
        try {
            CreateGameService.createGame(new CreateGameRequest("TestGame"));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
