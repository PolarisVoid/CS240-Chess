package services;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import exceptions.AlreadyTakenException;
import exceptions.InvalidRequestException;
import exceptions.UnathorizedException;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;

import java.util.Objects;

public class JoinGameService extends BaseService {

    private static boolean validJoin(String playerColor, GameData game) {
        switch (playerColor) {
            case "WHITE" -> {
                if (!Objects.equals(game.whiteUsername(), "")) {
                    return false;
                }
            }
            case "BLACK" -> {
                if (!Objects.equals(game.blackUsername(), "")) {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    public static void joinGame(JoinGameRequest joinGameRequest) throws Exception {
        AuthData authData = authenticate(joinGameRequest.getAuthToken());

        GameData game = new MemoryGameDAO().getGame(joinGameRequest.getGameID());

        if (game == null) {
            throw new Exception("Game was not created");
        }

        if (!validJoin(joinGameRequest.getPlayerColor(), game)) {
            throw new AlreadyTakenException("Color already taken");
        }

        switch (joinGameRequest.getPlayerColor()) {
            case "WHITE" -> new MemoryGameDAO().updateGame(game.gameID(), game.gameName(), authData.username(), game.blackUsername(), game.game());
            case "BLACK" -> new MemoryGameDAO().updateGame(game.gameID(), game.gameName(), game.whiteUsername(), authData.username(), game.game());
            default -> throw new InvalidRequestException("Couldn't Find Color");
        }
    }
}
