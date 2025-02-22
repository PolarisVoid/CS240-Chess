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
        Spark.delete("/db", (req, res) -> ClearDatabaseHandler.request(req, res)); // Clear Database
        Spark.post("/user", (req, res) -> RegisterHandler.request(req, res)); // Register
        Spark.post("/session", (req, res) -> LoginHandler.request(req, res)); // Login
        Spark.delete("/session", (req, res) -> LogoutHandler.request(req, res)); // Logout
        Spark.get("/game", (req, res) -> GetGameHandler.request(req, res)); // Get Games
        Spark.post("/game", (req, res) -> CreateGameHandler.request(req, res)); // Create Game
        Spark.put("/game", (req, res) -> JoinGameHandler.request(req, res)); // Join Game
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
