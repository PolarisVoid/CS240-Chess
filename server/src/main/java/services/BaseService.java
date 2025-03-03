package services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.UnathorizedException;
import model.AuthData;

public class BaseService {

    public static AuthData authenticate(String authToken) throws UnathorizedException, DataAccessException {
        AuthData authData = new MemoryAuthDAO().getAuth(authToken);

        if (authData == null) {
            throw new UnathorizedException("Authentication is Unauthorized");
        }

        return authData;
    }
}
