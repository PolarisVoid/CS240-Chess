package service;

import org.junit.jupiter.api.*;
import requests.GetGameRequest;
import responses.GetGameResponse;

public class GetGameServiceTests extends TestUtils {
    @Test
    @Order(1)
    @DisplayName("Valid Get Game")
    public void validGetGame() {
        try {
            GetGameResponse getGameResponse = GetGameService.getGames(new GetGameRequest(userResponse.getAuthToken()));
            assert getGameResponse.getGames() != null;
            assert !getGameResponse.getGames().isEmpty();
        } catch (Exception e) {
            assert false;
        }

    }

    @Test
    @Order(2)
    @DisplayName("Invalid Get Game")
    public void invalidGetGame() {
        try {
            GetGameService.getGames(new GetGameRequest("101"));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
