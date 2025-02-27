package responses;

public class CreateGameResponse {

    @Override
    public String toString() {
        return "{\"gameID\": " + gameID + " }";
    }

    int gameID;
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }
}
