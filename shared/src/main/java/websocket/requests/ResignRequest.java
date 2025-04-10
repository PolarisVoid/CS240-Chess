package websocket.requests;

import websocket.commands.UserGameCommand;

public record ResignRequest(
        UserGameCommand.CommandType commandType,
        String authToken,
        int gameID) {
}
