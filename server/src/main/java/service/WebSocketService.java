package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.DataAccessException;
import exceptions.InvalidRequestException;
import exceptions.UnathorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.requests.ConnectionRequest;
import websocket.requests.LeaveRequest;
import websocket.requests.MakeMoveRequest;
import websocket.requests.ResignRequest;
import websocket.responses.LoadGameResponse;
import websocket.responses.NotificationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WebSocketService extends BaseService {
    static HashMap<Integer, ArrayList<Session>> gameSessions = new HashMap<>();

    private static void validateGame(GameData gameData) throws Exception {
        if (gameData == null) {
            throw new InvalidRequestException("Bad GameID");
        }
        if (gameData.game().getResigned() != null) {
            throw new UnathorizedException("This Game Has Ended");
        }
    }

    private static void validateUser(AuthData authData, GameData gameData) throws Exception {
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        String user = authData.username();
        if (!Objects.equals(white, user) && !Objects.equals(black, user)) {
            throw new UnathorizedException("You are not a player in this game");
        }
    }

    private static void updateGame(GameData gameData) throws DataAccessException {
        GAME_DAO.updateGame(
                gameData.gameID(),
                gameData.gameName(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.game()
        );
    }

    private static void updateChessGame(GameData gameData, ChessGame game) throws DataAccessException {
        int gameID = gameData.gameID();
        String gameName = gameData.gameName();
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        updateGame(new GameData(gameID, gameName, white, black, game));
    }

    private static void removeUser(GameData gameData, ChessGame.TeamColor color) throws DataAccessException {
        GameData newGameData;
        int gameID = gameData.gameID();
        String gameName = gameData.gameName();
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        ChessGame game = gameData.game();
        switch (color) {
            case BLACK -> newGameData = new GameData(gameID, gameName, white, null, game);
            case WHITE -> newGameData = new GameData(gameID, gameName, null, black, game);
            case null, default -> newGameData = null;
        }
        if (newGameData == null) {
            return;
        }
        updateGame(newGameData);
    }

    private static ChessGame.TeamColor getUserColor(AuthData authData, GameData gameData) {
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        String user = authData.username();
        if (Objects.equals(white, user)) {
            return ChessGame.TeamColor.WHITE;
        } else if(Objects.equals(black, user)) {
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }

    private static char convertCol(int col) {
        switch (col) {
            case 1 -> {
                return 'a';
            }
            case 2 -> {
                return 'b';
            }
            case 3 -> {
                return 'c';
            }
            case 4 -> {
                return 'd';
            }
            case 5 -> {
                return 'e';
            }
            case 6 -> {
                return 'f';
            }
            case 7 -> {
                return 'g';
            }
            case 8 -> {
                return 'h';
            }
        }
        return ' ';
    }

    private static String getMoveMessage(String user, ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        return user + " made a move." +
                convertCol(start.getColumn()) +
                start.getRow() + " to " + convertCol(end.getColumn()) +
                end.getRow();
    }

    public static void handleConnect(Session session, ConnectionRequest request) throws Exception {
        AuthData authData = authenticate(request.authToken());
        GameData gameData = GAME_DAO.getGame(request.gameID());
        validateGame(gameData);

        ArrayList<Session> sessions = gameSessions.getOrDefault(request.gameID(), new ArrayList<>());
        for (Session gameSession : sessions) {
            if (session.equals(gameSession)) {
                return;
            }
        }

        ChessGame.TeamColor color = getUserColor(authData, gameData);
        String message = authData.username() + " joined the game";
        if (color != null) {
            message += " as " + color;
        }

        notifyAllExceptMe(request.gameID(), message, session);
        sessions.add(session);
        gameSessions.put(request.gameID(), sessions);
        sendMessage(session, new LoadGameResponse(gameData.game()).toString());
    }

    public static void handleMakeMove(Session session, MakeMoveRequest request) throws Exception {
        AuthData authData = authenticate(request.authToken());
        GameData gameData = GAME_DAO.getGame(request.gameID());
        validateGame(gameData);
        validateUser(authData, gameData);
        ChessGame.TeamColor color = getUserColor(authData, gameData);
        ChessGame game = gameData.game();
        if (color != game.getTeamTurn()) {
            throw new UnathorizedException("It is not your turn");
        }
        game.makeMove(request.move());
        updateChessGame(gameData, game);
        sendMessages(request.gameID(), new LoadGameResponse(game).toString(), null);

        ChessGame.TeamColor opponent = (color == ChessGame.TeamColor.WHITE)
                ? ChessGame.TeamColor.BLACK
                : ChessGame.TeamColor.WHITE;

        if (game.isInCheckmate(opponent)) {
            notifyAllExceptMe(request.gameID(), "Checkmate! " + authData.username() + " wins! Game over.", session);
        } else if (game.isInStalemate(opponent)) {
            notifyAllExceptMe(request.gameID(), "Stalemate! It's a draw. Game over.", session);
        } else if (game.isInCheck(opponent)) {
            notifyAllExceptMe(request.gameID(), "Check! " + authData.username() + " has your king under threat.", session);
        }
        notifyAllExceptMe(request.gameID(), getMoveMessage(authData.username(), request.move()), session);
    }

    public static void handleLeave(Session session, LeaveRequest request) throws Exception {
        AuthData authData = authenticate(request.authToken());
        GameData gameData = GAME_DAO.getGame(request.gameID());
        validateGame(gameData);

        ArrayList<Session> sessions = gameSessions.get(request.gameID());
        if (sessions == null) {
            return;
        }

        removeUser(gameData, getUserColor(authData, gameData));
        sessions.remove(session);
        notifyAllExceptMe(request.gameID(), authData.username() + " has Left the Game", session);
    }

    public static void handleResign(ResignRequest request) throws Exception {
        AuthData authData = authenticate(request.authToken());
        GameData gameData = GAME_DAO.getGame(request.gameID());
        validateGame(gameData);
        validateUser(authData, gameData);

        ChessGame game = gameData.game();

        if (game.getResigned() != null) {
            throw new UnathorizedException("Opponent already resigned.");
        }

        game.setResigned(getUserColor(authData, gameData));
        updateChessGame(gameData, game);

        notifyAll(request.gameID(), authData.username() + " has Resigned");
    }

    private static void notifyAll(Integer gameID, String message) {
        NotificationResponse notificationResponse = new NotificationResponse(message);
        sendMessages(gameID, notificationResponse.toString(), null);
    }

    private static void notifyAllExceptMe(Integer gameID, String message, Session excludeSession) {
        NotificationResponse notificationResponse = new NotificationResponse(message);
        sendMessages(gameID, notificationResponse.toString(), excludeSession);
    }

    public static void sendMessages(Integer gameID, String message, Session excludeSession) {
        ArrayList<Session> sessions = gameSessions.get(gameID);
        if (sessions == null) {
            return;
        }

        for (Session session : sessions) {
            if (session == excludeSession) {
                continue;
            }
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
