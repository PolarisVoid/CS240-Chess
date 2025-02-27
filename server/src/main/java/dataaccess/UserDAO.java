package dataaccess;

import model.UserData;

public interface UserDAO {

    void clear();
    UserData createUser(String username, String password, String email);
    UserData getUser(String username);
}
