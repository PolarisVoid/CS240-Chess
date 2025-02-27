package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear();
    AuthData createAuth(String username);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
