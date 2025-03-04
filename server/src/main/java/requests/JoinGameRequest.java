package requests;

import chess.ChessGame;

public class JoinGameRequest {

    String authToken;
    final ChessGame.TeamColor playerColor;
    final int gameID;

    public JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getAuthToken() {return this.authToken;}
    public ChessGame.TeamColor getPlayerColor() {return this.playerColor;}
    public int getGameID() {return this.gameID;}

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
