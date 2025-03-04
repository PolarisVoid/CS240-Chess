package services;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.UnathorizedException;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import responses.LoginResponse;

import java.util.Objects;

public class LoginService extends BaseService {

    public static LoginResponse login(LoginRequest loginRequest) throws Exception {
        UserData user;
        user = new MemoryUserDAO().getUser(loginRequest.getUsername());

        if (user == null) {
            throw new UnathorizedException("User not found");
        }

        if (!Objects.equals(user.password(), loginRequest.getPassword())) {
            throw new UnathorizedException("Incorrect Password");
        }

        AuthData authData = new MemoryAuthDAO().createAuth(user.username());
        return new LoginResponse(user.username(), authData.authToken());
    }
}
