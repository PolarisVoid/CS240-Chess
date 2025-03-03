package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


public class MemoryAuthDAO implements AuthDAO {
    public static ArrayList<AuthData> Database = new ArrayList<>();

    @Override
    public void clear() {
        Database.clear();
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try {
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, username);
            Database.add(authData);
            return authData;
        } catch (Exception e) {
            throw new DataAccessException("AuthToken could not be created.");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        try {
            for (AuthData row : Database) {
                if (Objects.equals(row.authToken(), authToken)) {
                    return row;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("AuthToken not found");
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        try {
            for (AuthData row : Database) {
                if (Objects.equals(row.authToken(), authToken)) {
                    Database.remove(row);
                    return;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Auth Token Not Found");
        }
    }
}
