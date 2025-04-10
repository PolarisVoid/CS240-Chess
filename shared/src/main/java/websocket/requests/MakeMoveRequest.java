package websocket.requests;

import chess.ChessMove;
import websocket.commands.UserGameCommand;

public record MakeMoveRequest(
        UserGameCommand.CommandType commandType,
        String authToken,
        int gameID,
        ChessMove move) {
}
