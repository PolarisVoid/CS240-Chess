package dataaccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO {
    @Override
    public void clear() {
        try {
            DatabaseManager.truncateTable("AUTH");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String createAuthQuery() {
        return "INSERT INTO AUTH (AUTHTOKEN, USERNAME) VALUES (?, ?)";
    }

    private String getAuthQuery() {
        return "SELECT * FROM AUTH WHERE AUTHTOKEN = ?";
    }

    private String deleteAuthQuery() {
        return "DELETE FROM AUTH WHERE AUTHTOKEN = ?";
    }

    public AuthData processGetAuth(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new AuthData(
                    rs.getString("AUTHTOKEN"),
                    rs.getString("USERNAME")
            );
        }
        return null;
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        DatabaseManager.executeInsert(createAuthQuery(), authToken, username);
        return getAuth(authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return DatabaseManager.executeQuery(getAuthQuery(), this::processGetAuth, authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        DatabaseManager.executeUpdate(deleteAuthQuery(), authToken);
    }
}
