package service;

import dataaccess.*;
import exceptions.UnathorizedException;
import model.AuthData;

public class BaseService {
    static final AuthDAO AUTH_DAO = new DatabaseAuthDAO();
    static final GameDAO GAME_DAO = new DatabaseGameDAO();
    static final UserDAO USER_DAO = new DatabaseUserDAO();

    public static AuthData authenticate(String authToken) throws UnathorizedException, DataAccessException {
        AuthData authData = AUTH_DAO.getAuth(authToken);

        if (authData == null) {
            throw new UnathorizedException("Authentication is Unauthorized");
        }

        return authData;
    }
}
