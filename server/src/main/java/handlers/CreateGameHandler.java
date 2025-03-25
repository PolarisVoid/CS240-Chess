package handlers;

import com.google.gson.Gson;
import exceptions.InvalidRequestException;
import exceptions.UnathorizedException;
import requests.CreateGameRequest;
import service.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class CreateGameHandler implements Route {

    private CreateGameRequest createRequest(Request request) throws InvalidRequestException {
        try {
            String authToken = request.headers("authorization");
            CreateGameRequest createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
            if (createGameRequest.getGameName() == null || Objects.equals(createGameRequest.getGameName(), "")) {
                throw new InvalidRequestException("Missing Information");
            }
            createGameRequest.setAuthToken(authToken);
            return createGameRequest;
        } catch (Exception e) {
            throw new InvalidRequestException(e.toString());
        }
    }
    @Override
    public Object handle(Request request, Response response) {
        try {
            return CreateGameService.createGame(createRequest(request));
        } catch (InvalidRequestException e) {
            response.status(400);
            return "{\"message\": \"Error: bad request\" }";
        } catch (UnathorizedException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            response.status(500);
            return "{\"message\": \"Error: " + e + "\" }";
        }
    }
}
