package dataaccess;

import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO {
    @Override
    public void clear() {
        try {
            DatabaseManager.truncateTable("AUTH");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
