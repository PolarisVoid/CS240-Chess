package requests;

public class JoinGameRequest {

    String authToken;
    String playerColor;
    int gameID;
    public JoinGameRequest(String authToken, String playerColor, int gameID) {
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getAuthToken() {return this.authToken;}
    public String getPlayerColor() {return this.playerColor;}
    public int getGameID() {return this.gameID;}
}
