package service;

import chess.ChessGame;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.WSServer;
import websocket.requests.ConnectionRequest;
import websocket.requests.LeaveRequest;
import websocket.requests.MakeMoveRequest;
import websocket.requests.ResignRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class WebSocketService extends BaseService {
    static HashMap<Integer, ArrayList<Session>> gameSessions = new HashMap<>();

    public static void handleConnect(Session session, ConnectionRequest request) throws Exception {
        authenticate(request.getAuthToken());

        ArrayList<Session> sessions = gameSessions.getOrDefault(request.getGameID(), new ArrayList<>());
        for (Session gameSession : sessions) {
            if (session.equals(gameSession)) {
                return;
            }
        }
        sessions.add(session);
        gameSessions.put(request.getGameID(), sessions);
//        sendMessage(request.getGameID(), "Someone Joined");
    }

    public static void handleMakeMove(Session session, MakeMoveRequest request) throws Exception {
        authenticate(request.getAuthToken());
        GameData gameData = GAME_DAO.getGame(request.getGameID());
        ChessGame game = gameData.game();
        game.makeMove(request.getMove());
        GAME_DAO.updateGame(gameData.gameID(), gameData.gameName(), gameData.whiteUsername(), gameData.blackUsername(), game);

//        sendMessage(request.getGameID(), "Move was made");
    }

    public static void handleLeave(Session session, LeaveRequest request) throws Exception {
        authenticate(request.getAuthToken());

        ArrayList<Session> sessions = gameSessions.get(request.getGameID());
        if (sessions == null) {
            return;
        }

        sessions.remove(session);
//        sendMessage(request.getGameID(), "Someone Left");
    }

    public static void handleResign(Session session, ResignRequest request) throws Exception {
        authenticate(request.getAuthToken());
    }

    public static void sendMessage(Integer gameID, String message) {
        ArrayList<Session> sessions = gameSessions.get(gameID);
        if (sessions == null) {
            return;
        }

        for (Session session : sessions) {
            try {
                session.getRemote().sendString(message);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
