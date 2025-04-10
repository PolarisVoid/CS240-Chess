package websocket.requests;

import websocket.commands.UserGameCommand;

public record LeaveRequest(
        UserGameCommand.CommandType commandType,
        String authToken,
        int gameID) {
}
