package facades;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.InvalidRequestException;

import javax.websocket.*;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class WSClient {
    private static CountDownLatch latch;
    private Session userSession = null;

    public WSClient(URI endpointURI) {
        latch = new CountDownLatch(1);
        try {
            ContainerProvider.getWebSocketContainer().connectToServer(this, endpointURI);
            latch.await();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket Connected");
        this.userSession = session;
        latch.countDown(); // Signal that the connection is established
    }

    @OnMessage
    public void onMessage(String message) throws Exception {
        String command = getCommand(message);
        switch (command) {
            case "LOAD_GAME"        -> {}
            case "ERROR"            -> {}
            case "NOTIFICATION"     -> {}
            case null, default      -> {}
        }
    }

    private String getCommand(String message) {
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        return json.has("commandType") ? json.get("commandType").getAsString() : null;
    }

    @OnClose
    public void onClose() {
        System.out.println("WebSocket Closed");
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
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

    public void closeConnection() {
        try {
            if (userSession != null && userSession.isOpen()) {
                userSession.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
