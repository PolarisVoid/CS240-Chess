package facades;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.*;
import exception.AlreadyTaken;
import exception.BadRequest;
import exception.ServerError;
import exception.Unauthorized;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;
import websocket.requests.LeaveRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {

    private final String serverURL;
    private WSClient wsClient;
    public ServerFacade(int port) {
        serverURL = "http://localhost:" + port;
        try {
            URI uri = new URI(serverURL + "/ws");
            wsClient = new WSClient(uri);
        } catch (Exception e) {
            System.out.println("Failed To connect");
        }

    }

    private HttpURLConnection sendRequest(String url, String method, JsonObject header, String body) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        writeRequestHeader(header, http);
        writeRequestBody(body, http);
        http.connect();
        return http;
    }

    private void writeRequestHeader(JsonObject header, HttpURLConnection http) {
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, JsonElement> entry: header.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }

    private void writeRequestBody(String body, HttpURLConnection http) throws IOException{
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }
    
    private void getError(int statusCode) {
        switch (statusCode) {
            case 400 -> throw new BadRequest("Invalid Request.");
            case 401 -> throw new Unauthorized("You are Unauthorized to do that.");
            case 403 -> throw new AlreadyTaken("This is Already Taken.");
            default -> throw new ServerError("An Internal Server Error happened.");
        }
    }

    private JsonObject getDataJson(HttpURLConnection http) throws IOException {
        try (InputStream in = http.getInputStream()) {
            return JsonParser.parseReader(new InputStreamReader(in)).getAsJsonObject();
        }
    }

    private AuthData getData(HttpURLConnection http) throws IOException {
        try (InputStream in = http.getInputStream()) {
            return new Gson().fromJson(new InputStreamReader(in), AuthData.class);
        }
    }

    public AuthData login(String username, String password) throws Exception {
        String url = serverURL + "/session";
        String method = "POST";
        String format = "{\"username\":\"%s\", \"password\":\"%s\"}";
        String body = String.format(format, username, password);
        HttpURLConnection http = sendRequest(url, method, new JsonObject(), body);
        int status = http.getResponseCode();
        if (status >= 200 && status < 300) {
            return getData(http);
        } else {
            getError(status);
            return null;
        }
    }

    public AuthData register(String username, String password, String email) throws Exception {
        String url = serverURL + "/user";
        String method = "POST";
        String format = "{\"username\":\"%s\", \"password\":\"%s\", \"email\":\"%s\"}";
        String body = String.format(format, username, password, email);
        HttpURLConnection http = sendRequest(url, method, new JsonObject(), body);
        int status = http.getResponseCode();
        if (status >= 200 && status < 300) {
            return getData(http);
        } else {
            getError(status);
            return null;
        }
    }

    public void logout(String authToken) throws Exception {
        String url = serverURL + "/session";
        String method = "DELETE";
        JsonObject header = new JsonObject();
        header.addProperty("authorization", authToken);
        HttpURLConnection http = sendRequest(url, method, header, "");
        int status = http.getResponseCode();
        if (!(status >= 200 && status < 300)) {
            getError(status);
        }
    }

    public int createGame(String authToken, String gameName) throws Exception {
        String url = serverURL + "/game";
        String method = "POST";
        JsonObject header = new JsonObject();
        header.addProperty("authorization", authToken);
        String format = "{\"gameName\":\"%s\"}";
        String body = String.format(format, gameName);
        HttpURLConnection http = sendRequest(url, method, header, body);
        int status = http.getResponseCode();
        if (!(status >= 200 && status < 300)) {
            getError(status);
        }
        JsonObject response = getDataJson(http);
        return response.get("gameID").getAsInt();
    }

    public ArrayList<GameData> listGames(String authToken) throws Exception {
        String url = serverURL + "/game";
        String method = "GET";
        JsonObject header = new JsonObject();
        header.addProperty("authorization", authToken);
        HttpURLConnection http = sendRequest(url, method, header, "");
        int status = http.getResponseCode();
        if (!(status >= 200 && status < 300)) {
            getError(status);
        }
        JsonObject response = getDataJson(http);
        JsonArray games = response.get("games").getAsJsonArray();
        ArrayList<GameData> gamesData = new ArrayList<>();
        for (var game : games) {
            GameData gameData = new Gson().fromJson(game, GameData.class);
            gamesData.add(gameData);
        }
        return gamesData;
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws Exception {
        String url = serverURL + "/game";
        String method = "PUT";
        JsonObject header = new JsonObject();
        header.addProperty("authorization", authToken);
        String format = "{\"playerColor\":\"%s\", \"gameID\":\"%d\"}";
        String body = String.format(format, color, gameID);
        HttpURLConnection http = sendRequest(url, method, header, body);
        int status = http.getResponseCode();
        if (!(status >= 200 && status < 300)) {
            getError(status);
        }
    }

    public void observeGame(String authToken, int gameID) throws Exception {
        JsonObject message = new JsonObject();
        message.addProperty("commandType", "CONNECT");
        message.addProperty("authToken", authToken);
        message.addProperty("gameID", gameID);
        message.addProperty("observer", true);
        message.add("color", null); // You can skip this if not needed

        wsClient.sendMessage(new Gson().toJson(message));
    }


    public void connect(String authToken, int gameID, ChessGame.TeamColor color) throws Exception {
        JsonObject message = new JsonObject();
        message.addProperty("commandType", "CONNECT");
        message.addProperty("authToken", authToken);
        message.addProperty("gameID", gameID);
        message.addProperty("observer", false);
        message.addProperty("color", color.toString());

        wsClient.sendMessage(new Gson().toJson(message));
    }


    public void makeMove(String authToken, int gameID, ChessGame.TeamColor color, ChessMove move) throws Exception {
        JsonObject message = new JsonObject();
        message.addProperty("commandType", "MAKE_MOVE");
        message.addProperty("authToken", authToken);
        message.addProperty("gameID", gameID);
        message.addProperty("color", color.toString());
        message.addProperty("move", move.toString());

        wsClient.sendMessage(new Gson().toJson(message));
    }


    public void leave(String authToken, int gameID, boolean observer, ChessGame.TeamColor color) throws Exception {
        LeaveRequest request = new LeaveRequest(UserGameCommand.CommandType.LEAVE, authToken, gameID, observer, color);

        JsonObject message = new JsonObject();
        message.addProperty("commandType", "LEAVE");
        message.addProperty("authToken", authToken);
        message.addProperty("gameID", gameID);
        message.addProperty("observer", observer);
        message.addProperty("color", color.toString());

        wsClient.sendMessage(new Gson().toJson(message));
    }

    public void resign(String authToken, int gameID, ChessGame.TeamColor color) throws Exception {
        JsonObject message = new JsonObject();
        message.addProperty("commandType", "RESIGN");
        message.addProperty("authToken", authToken);
        message.addProperty("gameID", gameID);
        message.addProperty("color", color.toString());

        wsClient.sendMessage(new Gson().toJson(message));
    }


}
