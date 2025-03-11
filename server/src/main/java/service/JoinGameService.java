package service;

import chess.ChessGame;
import chess.ChessGame.TeamColor;
import exceptions.AlreadyTakenException;
import exceptions.InvalidRequestException;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;

public class JoinGameService extends BaseService {

    private static boolean validJoin(TeamColor playerColor, GameData game) {
        switch (playerColor) {
            case TeamColor.WHITE -> {
                if (game.whiteUsername() == null) {
                    return true;
                }
            }
            case TeamColor.BLACK -> {
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

        GameData game = gameDAO.getGame(joinGameRequest.getGameID());

        if (game == null) {
            throw new InvalidRequestException("Game does not Exists");
        }

        if (!validJoin(joinGameRequest.getPlayerColor(), game)) {
            throw new AlreadyTakenException("Color already taken");
        }

        int gameID = game.gameID();
        String gameName = game.gameName();
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        ChessGame chessGame = game.game();

        switch (joinGameRequest.getPlayerColor()) {
            case TeamColor.WHITE -> gameDAO.updateGame(gameID, gameName, authData.username(), blackUsername, chessGame);
            case TeamColor.BLACK -> gameDAO.updateGame(gameID, gameName, whiteUsername, authData.username(), chessGame);
            default -> throw new InvalidRequestException("Couldn't Find Color");
        }
    }
}
