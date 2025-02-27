package services;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class ClearDatabaseService {

    public static void clearDatabase() throws DataAccessException {
        new MemoryAuthDAO().clear();
        new MemoryGameDAO().clear();
        new MemoryUserDAO().clear();
    }
}
