package handlers;

import exceptions.InvalidRequestException;
import exceptions.UnathorizedException;
import requests.LoginRequest;
import services.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class LoginHandler implements Route {

    private LoginRequest createRequest(Request request) throws InvalidRequestException {
        try {
            return new Gson().fromJson(request.body(), LoginRequest.class);
        } catch (Exception e) {
            System.out.println(e);
            throw new InvalidRequestException(e.toString());
        }
    }
    @Override
    public Object handle(Request request, Response response) {
        try {
            return LoginService.login(createRequest(request)).toString();
        } catch (UnathorizedException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            response.status(500);
            return "{\"message\": \"Error: " + e + "\" }";
        }
    }
}
