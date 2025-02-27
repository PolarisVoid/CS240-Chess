package handlers;

import exceptions.AlreadyTakenException;
import exceptions.InvalidRequestException;
import requests.RegisterRequest;
import services.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {

    private RegisterRequest createRequest(Request request) throws InvalidRequestException {
        try {
            String username = request.attribute("username");
            String password = request.attribute("password");
            String email = request.attribute("email");
            return new RegisterRequest(username, password, email);
        } catch (Exception e) {
            throw new InvalidRequestException(e.toString());
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            return RegisterService.register(createRequest(request)).toString();
        } catch (InvalidRequestException e) {
            response.status(400);
            return "{\"message\": \"Error: bad request\" }";
        } catch (AlreadyTakenException e) {
            response.status(403);
            return "{\"message\": \"Error: already taken\" }";
        } catch (Exception e) {
            response.status(500);
            return "{\"message\": \"Error: " + e + "\" }";
        }
    }
}
