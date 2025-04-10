package handlers;

import com.google.gson.Gson;
import exceptions.UnathorizedException;
import org.eclipse.jetty.websocket.api.Session;
import service.WebSocketService;
import websocket.requests.ConnectionRequest;
import websocket.requests.LeaveRequest;
import websocket.requests.MakeMoveRequest;
import websocket.requests.ResignRequest;
import websocket.responses.ErrorResponse;

import java.io.IOException;

public class WebSocketHandler {
    public static void handleConnect(Session session, String message) throws IOException {
        try {
            WebSocketService.handleConnect(session, new Gson().fromJson(message, ConnectionRequest.class));
        } catch (UnathorizedException e) {
            session.getRemote().sendString(new ErrorResponse("Unauthorized").toString());
        } catch (Exception e) {
            session.getRemote().sendString(new ErrorResponse(e.getMessage()).toString());
        }
    }

    public static void handleMakeMove(Session session, String message) throws IOException {
        try {
            WebSocketService.handleMakeMove(session, new Gson().fromJson(message, MakeMoveRequest.class));
        } catch (UnathorizedException e) {
            session.getRemote().sendString(new ErrorResponse("Unauthorized").toString());
        } catch (Exception e) {
            session.getRemote().sendString(new ErrorResponse(e.getMessage()).toString());
        }
    }

    public static void handleLeave(Session session, String message) throws IOException {
        try {
            WebSocketService.handleLeave(session, new Gson().fromJson(message, LeaveRequest.class));
        } catch (UnathorizedException e) {
            session.getRemote().sendString(new ErrorResponse("Unauthorized").toString());
        } catch (Exception e) {
            session.getRemote().sendString(new ErrorResponse(e.getMessage()).toString());
        }
    }

    public static void handleResign(Session session, String message) throws IOException {
        try {
            WebSocketService.handleResign(session, new Gson().fromJson(message, ResignRequest.class));
        } catch (UnathorizedException e) {
            session.getRemote().sendString(new ErrorResponse("Unauthorized").toString());
        } catch (Exception e) {
            session.getRemote().sendString(new ErrorResponse(e.getMessage()).toString());
        }
    }
}
