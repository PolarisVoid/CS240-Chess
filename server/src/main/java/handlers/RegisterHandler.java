package handlers;

import com.google.gson.Gson;
import exceptions.AlreadyTakenException;
import exceptions.InvalidRequestException;
import requests.RegisterRequest;
import service.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class RegisterHandler implements Route {

    private RegisterRequest createRequest(Request request) throws InvalidRequestException {
        try {
            RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);
            if (registerRequest.getPassword() == null || Objects.equals(registerRequest.getPassword(), "")) {
                throw new InvalidRequestException("Missing Information");
            }
            if (registerRequest.getUsername() == null || Objects.equals(registerRequest.getUsername(), "")) {
                throw new InvalidRequestException("Missing Information");
            }
            if (registerRequest.getEmail() == null || Objects.equals(registerRequest.getEmail(), "")) {
                throw new InvalidRequestException("Missing Information");
            }
            return registerRequest;
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
