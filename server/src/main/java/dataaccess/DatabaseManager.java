package dataaccess;

import java.sql.*;
import java.util.Properties;
import java.util.function.Function;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);

                createDatabase();
                createTables();
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static void createTables() throws DataAccessException {
        try {
            var userTable = """
                    CREATE TABLE IF NOT EXISTS User (
                        Username VARCHAR(50) PRIMARY KEY,
                        Password VARCHAR(255) NOT NULL,
                        Email VARCHAR(100) UNIQUE NOT NULL
                    )""";
            var authTable = """
                    CREATE TABLE IF NOT EXISTS Auth (
                        AuthToken VARCHAR(100) PRIMARY KEY,
                        Username VARCHAR(50) NOT NULL,
                        FOREIGN KEY (Username) REFERENCES User(Username) ON DELETE CASCADE
                    )
                    """;
            var gameTable = """
                    CREATE TABLE IF NOT EXISTS Game (
                        GAMEID INT AUTO_INCREMENT PRIMARY KEY,
                        GameName VARCHAR(100) NOT NULL,
                        WhiteUsername VARCHAR(50),
                        BlackUsername VARCHAR(50),
                        Game JSON
                    )
                    """;
            var conn = getConnection();
            try (var preparedStatement = conn.prepareStatement(userTable)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(authTable)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(gameTable)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static void truncateTable(String tableName) throws DataAccessException {
        executeUpdate("DELETE FROM " + tableName.toUpperCase());
        executeUpdate("ALTER TABLE " + tableName.toUpperCase() + " AUTO_INCREMENT = 0");
    }

    static int executeUpdate(String query, Object... params) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                if (params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        preparedStatement.setObject(i + 1, params[i]);
                    }
                }
                return preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static int executeInsert(String query, Object... params) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                if (params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        preparedStatement.setObject(i + 1, params[i]);
                    }
                }
                preparedStatement.executeUpdate();
                try (var keys = preparedStatement.getGeneratedKeys()) {
                    return keys.next() ? keys.getInt(1) : -1;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static <T> T executeQuery(String query, Function<ResultSet, T> handler, Object... params) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                if (params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        preparedStatement.setObject(i + 1, params[i]);
                    }
                }

                var rs = preparedStatement.executeQuery();
                return handler.apply(rs);
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
