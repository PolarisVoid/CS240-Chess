package websocket.requests;

import chess.ChessGame;
import websocket.commands.UserGameCommand;

public class ConnectionRequest {
    private final UserGameCommand.CommandType commandType;
    private final String authToken;
    private final int gameID;
    private final boolean observer;
    private final ChessGame.TeamColor color;

    public ConnectionRequest(
            UserGameCommand.CommandType commandType,
            String authToken,
            int gameID,
            boolean observer,
            ChessGame.TeamColor color
    ) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.observer = observer;
        this.color = color;
    }

    public UserGameCommand.CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public boolean isObserver() {
        return observer;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
