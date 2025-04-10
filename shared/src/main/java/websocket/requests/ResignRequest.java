package websocket.requests;

import chess.ChessGame;
import websocket.commands.UserGameCommand;

public class ResignRequest {
    private final UserGameCommand.CommandType commandType;
    private final String authToken;
    private final int gameID;
    private final ChessGame.TeamColor color;

    public ResignRequest(
            UserGameCommand.CommandType commandType,
            String authToken,
            int gameID,
            ChessGame.TeamColor color) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
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

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
