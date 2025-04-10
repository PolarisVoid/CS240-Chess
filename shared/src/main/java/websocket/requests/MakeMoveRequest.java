package websocket.requests;

import chess.ChessGame;
import chess.ChessMove;
import websocket.commands.UserGameCommand;

public class MakeMoveRequest {
    private final UserGameCommand.CommandType commandType;
    private final String authToken;
    private final int gameID;
    private final ChessGame.TeamColor color;
    private final ChessMove move;

    public MakeMoveRequest(
            UserGameCommand.CommandType commandType,
            String authToken,
            int gameID,
            ChessGame.TeamColor color,
            ChessMove move) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;
        this.move = move;
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

    public ChessMove getMove() {
        return move;
    }
}
