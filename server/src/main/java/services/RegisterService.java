package services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.AlreadyTakenException;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;
import responses.RegisterResponse;

import javax.xml.crypto.Data;

public class RegisterService {
    public static RegisterResponse register(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException, Exception {
        UserData user = new MemoryUserDAO().getUser(registerRequest.getUsername());

        if (user != null) {
            throw new AlreadyTakenException("This username is already taken");
        }

        user = new MemoryUserDAO().createUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());

        if (user == null) {
            throw new Exception("User wasn't created");
        }

        AuthData authData = new MemoryAuthDAO().createAuth(user.username());

        if (authData == null) {
            throw new Exception("AuthData not created.");
        }

        return new RegisterResponse(user.username(), authData.authToken());
    }
}
