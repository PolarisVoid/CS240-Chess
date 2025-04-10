package handlers;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import service.WebSocketService;
import websocket.requests.ConnectionRequest;
import websocket.requests.LeaveRequest;
import websocket.requests.MakeMoveRequest;
import websocket.requests.ResignRequest;

public class WebSocketHandler {
    public static void handleConnect(Session session, String message) {
        try {
            WebSocketService.handleConnect(session, new Gson().fromJson(message, ConnectionRequest.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void handleMakeMove(Session session, String message) {
        try {
            WebSocketService.handleMakeMove(session, new Gson().fromJson(message, MakeMoveRequest.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void handleLeave(Session session, String message) {
        try {
            WebSocketService.handleLeave(session, new Gson().fromJson(message, LeaveRequest.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void handleResign(Session session, String message) {
        try {
            WebSocketService.handleResign(session, new Gson().fromJson(message, ResignRequest.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
