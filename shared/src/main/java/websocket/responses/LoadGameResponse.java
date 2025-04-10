package websocket.responses;

import chess.ChessGame;
import websocket.messages.ServerMessage;

public class LoadGameResponse {
    ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.LOAD_GAME;
    ChessGame game;

    public String toString() {
        return String.format("{\"serverMessageType\": \"%s\", \"game\": \"%s\"}", serverMessageType, "value");
    }

    public LoadGameResponse (ChessGame game) {
        this.game = game;
    }
}
