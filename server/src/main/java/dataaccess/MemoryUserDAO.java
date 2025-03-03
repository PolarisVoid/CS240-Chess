package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    public static ArrayList<UserData> Database = new ArrayList<>();

    @Override
    public void clear() throws DataAccessException {
        Database.clear();
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException{
        try {
            UserData user = new UserData(username, password, email);
            Database.add(user);
            return user;
        } catch (Exception e) {
            throw new DataAccessException("User not Created");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try {
            for (UserData row : Database) {
                if (Objects.equals(row.username(), username)) {
                    return row;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("User not found");
        }
        return null;
    }
}
