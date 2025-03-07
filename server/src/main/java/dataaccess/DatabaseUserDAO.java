package dataaccess;

import model.UserData;

public class DatabaseUserDAO implements UserDAO {
    @Override
    public void clear() {

    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
