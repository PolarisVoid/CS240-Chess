package service;

import chess.ChessGame;
import exceptions.InvalidRequestException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.WSServer;
import websocket.requests.ConnectionRequest;
import websocket.requests.LeaveRequest;
import websocket.requests.MakeMoveRequest;
import websocket.requests.ResignRequest;
import websocket.responses.LoadGameResponse;
import websocket.responses.NotificationResponse;

import java.util.ArrayList;
import java.util.HashMap;

public class WebSocketService extends BaseService {
    static HashMap<Integer, ArrayList<Session>> gameSessions = new HashMap<>();

    public static void handleConnect(Session session, ConnectionRequest request) throws Exception {
        AuthData authdata = authenticate(request.getAuthToken());
        GameData gameData = GAME_DAO.getGame(request.getGameID());
        if (gameData == null) {
            throw new InvalidRequestException("Bad GameID");
        }


        ArrayList<Session> sessions = gameSessions.getOrDefault(request.getGameID(), new ArrayList<>());
        for (Session gameSession : sessions) {
            if (session.equals(gameSession)) {
                return;
            }
        }
        sendMessages(request.getGameID(), new NotificationResponse("Joined").toString());
        sessions.add(session);
        gameSessions.put(request.getGameID(), sessions);
        sendMessage(session, new LoadGameResponse(null).toString());
    }

    public static void handleMakeMove(Session session, MakeMoveRequest request) throws Exception {
        AuthData authdata = authenticate(request.getAuthToken());
        GameData gameData = GAME_DAO.getGame(request.getGameID());
        if (gameData == null) {
            throw new InvalidRequestException("Bad GameID");
        }

        ChessGame game = gameData.game();
        game.makeMove(request.getMove());
        GAME_DAO.updateGame(gameData.gameID(), gameData.gameName(), gameData.whiteUsername(), gameData.blackUsername(), game);

        sendMessages(request.getGameID(), new LoadGameResponse(null).toString());
    }

    public static void handleLeave(Session session, LeaveRequest request) throws Exception {
        AuthData authdata = authenticate(request.getAuthToken());

        ArrayList<Session> sessions = gameSessions.get(request.getGameID());
        if (sessions == null) {
            return;
        }

        sessions.remove(session);
        sendMessages(request.getGameID(), new NotificationResponse("Leave").toString());
    }

    public static void handleResign(Session session, ResignRequest request) throws Exception {
        AuthData authdata = authenticate(request.getAuthToken());

        sendMessages(request.getGameID(), new NotificationResponse("Resigned").toString());
    }

    public static void sendMessages(Integer gameID, String message) {
        ArrayList<Session> sessions = gameSessions.get(gameID);
        if (sessions == null) {
            return;
        }

        for (Session session : sessions) {
            sendMessage(session, message);
        }
    }

    public static void sendMessage(Session session, String message) {
        try {
            session.getRemote().sendString(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
