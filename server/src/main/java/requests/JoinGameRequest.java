package requests;

public class JoinGameRequest {

    String authToken;
    String playerColor;
    int gameID;

    public JoinGameRequest(String playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getAuthToken() {return this.authToken;}
    public String getPlayerColor() {return this.playerColor;}
    public int getGameID() {return this.gameID;}

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
