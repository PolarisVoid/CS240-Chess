package services;

import dataaccess.MemoryGameDAO;
import model.GameData;
import requests.CreateGameRequest;
import responses.CreateGameResponse;

public class CreateGameService extends BaseService {

    public static CreateGameResponse createGame(CreateGameRequest createGameRequest) throws Exception {
        authenticate(createGameRequest.getAuthToken());

        GameData game = new MemoryGameDAO().createGame(createGameRequest.getGameName());

        if (game == null) {
            throw new Exception("Game was not created");
        }

        return new CreateGameResponse(game.gameID());
    }
}
