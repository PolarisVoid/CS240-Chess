package service;

import exceptions.UnathorizedException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requests.LoginRequest;
import responses.LoginResponse;

public class LoginService extends BaseService {

    public static LoginResponse login(LoginRequest loginRequest) throws Exception {
        UserData user;
        user = userDAO.getUser(loginRequest.getUsername());

        if (user == null) {
            throw new UnathorizedException("User not found");
        }

        if (!BCrypt.checkpw(loginRequest.getPassword(), user.password())) {
            throw new UnathorizedException("Incorrect Password");
        }

        AuthData authData = authDAO.createAuth(user.username());
        return new LoginResponse(user.username(), authData.authToken());
    }
}
