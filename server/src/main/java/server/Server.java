package server;

import handlers.*;
import spark.*;

public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        this.registerEndpoints();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void registerEndpoints() {
        Spark.delete("/db", new ClearDatabaseHandler()); // Clear Database
        Spark.post("/user", new RegisterHandler()); // Register
        Spark.post("/session", new LoginHandler()); // Login
        Spark.delete("/session", new LogoutHandler()); // Logout
        Spark.get("/game", new GetGameHandler()); // Get Games
        Spark.post("/game", new CreateGameHandler()); // Create Game
        Spark.put("/game", new JoinGameHandler()); // Join Game
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
