package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestUtilsDAO {
    public static AuthDAO authDatabase;
    public static GameDAO gameDatabase;
    public static UserDAO userDatabase;

    public static UserData user;
    public static GameData game;
    public static AuthData auth;


    @BeforeAll
    public static void init() {
        authDatabase = new DatabaseAuthDAO();
        gameDatabase = new DatabaseGameDAO();
        userDatabase = new DatabaseUserDAO();
    }

    @BeforeEach
    public void setup() {
        userDatabase.clear();
        authDatabase.clear();
        gameDatabase.clear();

        try {
            user = userDatabase.createUser("TestUser", "password", "test@gmail.com");
            game = gameDatabase.createGame("TestGame");
            auth = authDatabase.createAuth("TestUser");
        } catch (Exception e) {
            System.out.println("Error");
            assert false;
        }
    }
}
