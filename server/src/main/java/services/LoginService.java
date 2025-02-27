package services;

import com.mysql.cj.log.Log;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.UnathorizedException;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import responses.LoginResponse;

import java.util.Objects;

public class LoginService extends BaseService {

    public static LoginResponse login(LoginRequest loginRequest) throws UnathorizedException, DataAccessException, Exception {
        UserData user = new MemoryUserDAO().getUser(loginRequest.getUsername());

        if (user == null) {
            throw new Exception("User does not exsist");
        }

        if (!Objects.equals(user.password(), loginRequest.getPassword())) {
            throw new UnathorizedException("Incorrect Password");
        }

        AuthData authData = new MemoryAuthDAO().createAuth(user.username());

        if (authData == null) {
            throw new Exception("Authdata wasn't created");
        }

        return new LoginResponse(user.username(), authData.authToken());
    }
}
