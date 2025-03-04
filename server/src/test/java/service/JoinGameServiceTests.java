package service;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import requests.JoinGameRequest;
import services.JoinGameService;


import java.util.Objects;

public class JoinGameServiceTests extends TestUtils {
    @Test
    @Order(1)
    @DisplayName("Valid Join Game")
    public void validJoinGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameResponse.getGameID());
        joinGameRequest.setAuthToken(userResponse.getAuthToken());

        try {
            JoinGameService.joinGame(joinGameRequest);
            GameData gamedata = gameDatabase.listGames().getFirst();
            assert Objects.equals(gamedata.whiteUsername(), user.username());
            assert gamedata.blackUsername() == null;
            assert gamedata.gameID() == gameResponse.getGameID();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Invalid Join Game")
    public void invalidJoinGame() {
        try {
            JoinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1));
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
