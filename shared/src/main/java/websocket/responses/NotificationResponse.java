package websocket.responses;

import websocket.messages.ServerMessage;

public class NotificationResponse {
    ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.NOTIFICATION;
    String message;

    @Override
    public String toString() {
        return String.format("{\"serverMessageType\": \"%s\", \"message\": \"%s\"}", serverMessageType, message);
    }

    public NotificationResponse(String message) {
        this.message = message;
    }
    public String getMessage() {return message;}
}
