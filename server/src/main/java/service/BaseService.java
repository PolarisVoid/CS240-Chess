package service;

import dataaccess.*;
import exceptions.UnathorizedException;
import model.AuthData;

public class BaseService {
    static final AuthDAO authDAO = new DatabaseAuthDAO();
    static final GameDAO gameDAO = new DatabaseGameDAO();
    static final UserDAO userDAO = new DatabaseUserDAO();

    public static AuthData authenticate(String authToken) throws UnathorizedException, DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);

        if (authData == null) {
            throw new UnathorizedException("Authentication is Unauthorized");
        }

        return authData;
    }
}
