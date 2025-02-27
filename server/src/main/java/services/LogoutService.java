package services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.UnathorizedException;
import requests.LogoutRequest;

public class LogoutService extends BaseService {

    public static void logout(LogoutRequest logoutRequest) throws UnathorizedException, DataAccessException {
        authenticate(logoutRequest.getAuthToken());
        new MemoryAuthDAO().deleteAuth(logoutRequest.getAuthToken());
    }
}
