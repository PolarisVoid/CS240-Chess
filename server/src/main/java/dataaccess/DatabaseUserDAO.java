package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO {

    @Override
    public void clear() {
        try {
            DatabaseManager.truncateTable("USER");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private String createUserQuery() {
        return  "INSERT INTO USER (USERNAME, PASSWORD, EMAIL) VALUES (?, ?, ?)";
    }

    private String getUserQuery() {
        return "SELECT * FROM USER WHERE USERNAME = ?";
    }

    public UserData processGetUser(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new UserData(
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD"),
                    rs.getString("EMAIL")
            );
        }
        return null;
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        int user = DatabaseManager.executeUpdate(createUserQuery(), username, hashPassword(password), email);
        return (user > 0) ? getUser(username) : null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return DatabaseManager.executeQuery(getUserQuery(), this::processGetUser, username);
    }
}
