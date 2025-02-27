package handlers;

import exceptions.AlreadyTakenException;
import exceptions.InvalidRequestException;
import exceptions.UnathorizedException;
import requests.JoinGameRequest;
import services.JoinGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {

    private JoinGameRequest createRequest(Request request) throws InvalidRequestException {
        try {
            String authToken = request.attribute("authorization");
            return new JoinGameRequest(authToken);
        } catch (Exception e) {
            throw new InvalidRequestException(e.toString());
        }
    }
    @Override
    public Object handle(Request request, Response response) {
        try {
            JoinGameService.joinGame(createRequest(request));
            return "{}";
        } catch (InvalidRequestException e) {
            response.status(400);
            return "{\"message\": \"Error: bad request\" }";
        } catch (UnathorizedException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\" }";
        } catch (AlreadyTakenException e) {
            response.status(403);
            return "{\"message\": \"Error: already taken\" }";
        } catch (Exception e) {
            response.status(500);
            return "{\"message\": \"Error: " + e + "\" }";
        }
    }
}
