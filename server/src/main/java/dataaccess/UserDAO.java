package dataaccess;

import model.UserData;

public interface UserDAO {

    void clear() throws DataAccessException;
    UserData createUser(String username, String password, String email) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
