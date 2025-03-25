package ui;

import model.AuthData;
import model.GameData;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collection;

public class ServerFacade {

    private String serverURL;
    public ServerFacade(int port) {
        serverURL = "http://localhost:" + port;
    }

    public AuthData Login() {
        String url = serverURL + "/login";
        try {
            URI uri = new URI(url);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
        } catch (Exception e) {

        }
        return null;
    }

    public AuthData Register() {
        return null;
    }

    public void Logout() {

    }

    public int CreateGame() {
        return 0;
    }

    public Collection<GameData> ListGames() {
        return null;
    }

    public void JoinGame() {

    }

    public void ObserveGame() {

    }
}
