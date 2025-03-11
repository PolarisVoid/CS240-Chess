package service;

import exceptions.AlreadyTakenException;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;
import responses.RegisterResponse;


public class RegisterService extends BaseService {
    public static RegisterResponse register(RegisterRequest registerRequest) throws Exception {
        UserData user = userDAO.getUser(registerRequest.getUsername());

        if (user != null) {
            throw new AlreadyTakenException("This username is already taken");
        }

        user = userDAO.createUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
        AuthData authData = authDAO.createAuth(user.username());
        return new RegisterResponse(user.username(), authData.authToken());
    }
}
