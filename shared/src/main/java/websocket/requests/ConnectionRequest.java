package websocket.requests;

import websocket.commands.UserGameCommand;

public record ConnectionRequest(
        UserGameCommand.CommandType commandType,
        String authToken,
        int gameID) {
}
