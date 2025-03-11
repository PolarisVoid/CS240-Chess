package service;

import dataaccess.DataAccessException;
import exceptions.UnathorizedException;
import requests.LogoutRequest;

public class LogoutService extends BaseService {

    public static void logout(LogoutRequest logoutRequest) throws UnathorizedException, DataAccessException {
        authenticate(logoutRequest.getAuthToken());
        authDAO.deleteAuth(logoutRequest.getAuthToken());
    }
}
