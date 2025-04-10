package facades;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import interfaces.Interface;
import ui.Client;
import websocket.responses.ErrorResponse;
import websocket.responses.LoadGameResponse;
import websocket.responses.NotificationResponse;

import javax.websocket.*;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class WSClient {
    private static CountDownLatch latch;
    private Session userSession = null;
    private Client client = null;

    public WSClient(URI endpointURI) {
        latch = new CountDownLatch(1);
        try {
            ContainerProvider.getWebSocketContainer().connectToServer(this, endpointURI);
            latch.await();
        } catch (Exception e) {
            System.out.println("Couldn't establish connection");
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.userSession = session;
        latch.countDown(); // Signal that the connection is established
    }

    @OnMessage
    public void onMessage(String message) {
        String command = getCommand(message);
        switch (command) {
            case "LOAD_GAME"        -> loadGame(message);
            case "ERROR"            -> errorMessage(message);
            case "NOTIFICATION"     -> notification(message);
            case null, default      -> {}
        }
    }

    private String getCommand(String message) {
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        return json.has("serverMessageType") ? json.get("serverMessageType").getAsString() : null;
    }

    public void sendMessage(String message) {
        try {
            if (userSession != null && userSession.isOpen()) {
                userSession.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadGame(String message) {
        LoadGameResponse loadGameResponse = new Gson().fromJson(message, LoadGameResponse.class);
        Interface anInterface = client.getAnInterface();
        anInterface.setChessGame(loadGameResponse.getGame());
    }

    private void errorMessage(String message) {
        ErrorResponse errorResponse = new Gson().fromJson(message, ErrorResponse.class);
        System.out.println();
        System.out.println(errorResponse.getErrorMessage());
        System.out.print(">>> ");
        System.out.flush();

    }

    private void notification(String message) {
        NotificationResponse notificationResponse = new Gson().fromJson(message, NotificationResponse.class);
        System.out.println();
        System.out.println(notificationResponse.getMessage());
        System.out.print(">>> ");
        System.out.flush();
    }
}
