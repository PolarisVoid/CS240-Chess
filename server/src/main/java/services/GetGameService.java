package services;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import exceptions.UnathorizedException;
import model.GameData;
import requests.GetGameRequest;
import responses.GetGameResponse;

import java.util.ArrayList;

public class GetGameService extends BaseService {

    public static GetGameResponse getGames(GetGameRequest getGameRequest) throws UnathorizedException, DataAccessException {
        authenticate(getGameRequest.getAuthToken());

        ArrayList<GameData> games = new MemoryGameDAO().listGames();
        GetGameResponse response = new GetGameResponse();
        for (GameData game: games) {
            response.insertGame(game);
        }
        return response;
    }
}
