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

    String getAuthToken() {return this.authToken;}
    String getPlayerColor() {return this.playerColor;}
    int getGameID() {return this.gameID;}
}
