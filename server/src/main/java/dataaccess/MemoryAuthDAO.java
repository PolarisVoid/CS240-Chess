package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException{
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException{

    }
}
