package websocket.responses;

import websocket.messages.ServerMessage;

public class ErrorResponse {

    ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.ERROR;
    String errorMessage;

    @Override
    public String toString() {
        return String.format("{\"serverMessageType\": \"%s\", \"errorMessage\": \"%s\"}", serverMessageType, errorMessage);
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
