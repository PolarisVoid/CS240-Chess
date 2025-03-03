package services;

import dataaccess.MemoryGameDAO;
import exceptions.AlreadyTakenException;
import exceptions.InvalidRequestException;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;

import java.util.Objects;

public class JoinGameService extends BaseService {

    private static boolean validJoin(String playerColor, GameData game) {
        switch (playerColor) {
            case "WHITE" -> {
                System.out.println(game.whiteUsername());
                if (Objects.equals(game.whiteUsername(), "")) {
                    return true;
                }
            }
            case "BLACK" -> {
                System.out.println(game.blackUsername());
                if (Objects.equals(game.blackUsername(), "")) {
                    return true;
                }
            }
            default -> {
                return false;
            }
        }
        return false;
    }

    public static void joinGame(JoinGameRequest joinGameRequest) throws Exception {
        AuthData authData = authenticate(joinGameRequest.getAuthToken());

        GameData game = new MemoryGameDAO().getGame(joinGameRequest.getGameID());

        if (game == null) {
            throw new InvalidRequestException("Game does not Exists");
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
