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
        Spark.delete("/db", ClearDatabaseHandler::request); // Clear Database
        Spark.post("/user", RegisterHandler::request); // Register
        Spark.post("/session", LoginHandler::request); // Login
        Spark.delete("/session", LogoutHandler::request); // Logout
        Spark.get("/game", GetGameHandler::request); // Get Games
        Spark.post("/game", CreateGameHandler::request); // Create Game
        Spark.put("/game", JoinGameHandler::request); // Join Game
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
