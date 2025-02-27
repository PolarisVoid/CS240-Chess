package handlers;

import services.ClearDatabaseService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearDatabaseHandler implements Route {

    @Override
    public Object handle(Request request, Response response) {
        try {
            ClearDatabaseService.clearDatabase();
            return "{}";
        } catch (Exception e) {
            response.status(500);
            return "{\"message\": \"Error: " + e.toString() + "\" }";
        }
    }
}
