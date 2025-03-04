package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


public class MemoryAuthDAO implements AuthDAO {
    public static final ArrayList<AuthData> database = new ArrayList<>();

    @Override
    public void clear() {
        database.clear();
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try {
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, username);
            database.add(authData);
            return authData;
        } catch (Exception e) {
            throw new DataAccessException("AuthToken could not be created.");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        try {
            for (AuthData row : database) {
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
            for (AuthData row : database) {
                if (Objects.equals(row.authToken(), authToken)) {
                    database.remove(row);
                    return;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Auth Token Not Found");
        }
    }
}
