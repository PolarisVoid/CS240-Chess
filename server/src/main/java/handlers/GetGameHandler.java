package handlers;

import exceptions.InvalidRequestException;
import exceptions.UnathorizedException;
import requests.GetGameRequest;
import service.GetGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetGameHandler implements Route {

    private GetGameRequest createRequest(Request request) throws InvalidRequestException {
        try {
            String authToken = request.headers("authorization");
            return new GetGameRequest(authToken);
        } catch (Exception e) {
            throw new InvalidRequestException(e.toString());
        }
    }
    @Override
    public Object handle(Request request, Response response) {
        try {
            return GetGameService.getGames(createRequest(request)).toString();
        } catch (UnathorizedException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            response.status(500);
            return "{\"message\": \"Error: " + e + "\" }";
        }
    }
}
