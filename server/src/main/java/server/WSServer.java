package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import handlers.WebSocketHandler;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.responses.ErrorResponse;



@WebSocket
public class WSServer {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            String command = getCommand(message);
            switch (command) {
                case "CONNECT"    -> WebSocketHandler.handleConnect(session, message);
                case "MAKE_MOVE"  -> WebSocketHandler.handleMakeMove(session, message);
                case "LEAVE"      -> WebSocketHandler.handleLeave(session, message);
                case "RESIGN"     -> WebSocketHandler.handleResign(session, message);
                case null, default -> {
                    String error = new ErrorResponse("Invalid Command").toString();
                    session.getRemote().sendString(error);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String getCommand(String message) {
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        return json.has("commandType") ? json.get("commandType").getAsString() : null;
    }
}
