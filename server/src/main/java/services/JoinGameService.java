package services;

import chess.ChessGame;
import dataaccess.MemoryGameDAO;
import exceptions.AlreadyTakenException;
import exceptions.InvalidRequestException;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;

public class JoinGameService extends BaseService {

    private static boolean validJoin(ChessGame.TeamColor playerColor, GameData game) {
        switch (playerColor) {
            case ChessGame.TeamColor.WHITE -> {
                if (game.whiteUsername() == null) {
                    return true;
                }
            }
            case ChessGame.TeamColor.BLACK -> {
                if (game.blackUsername() == null) {
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
            case ChessGame.TeamColor.WHITE -> new MemoryGameDAO().updateGame(game.gameID(), game.gameName(), authData.username(), game.blackUsername(), game.game());
            case ChessGame.TeamColor.BLACK -> new MemoryGameDAO().updateGame(game.gameID(), game.gameName(), game.whiteUsername(), authData.username(), game.game());
            default -> throw new InvalidRequestException("Couldn't Find Color");
        }
    }
}
