package handlers;

import exceptions.InvalidRequestException;
import exceptions.UnathorizedException;
import requests.LogoutRequest;
import service.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {

    private LogoutRequest createRequest(Request request) throws InvalidRequestException {
        try {
            String authToken = request.headers("authorization");
            return new LogoutRequest(authToken);
        } catch (Exception e) {
            throw new InvalidRequestException(e.toString());
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            LogoutService.logout(createRequest(request));
            return "{}";
        } catch (UnathorizedException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            response.status(500);
            return "{\"message\": \"Error: " + e + "\" }";
        }
    }
}
