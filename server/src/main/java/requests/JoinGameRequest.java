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
}
