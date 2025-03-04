package responses;

public class CreateGameResponse {

    @Override
    public String toString() {
        return "{\"gameID\": " + gameID + " }";
    }

    final int gameID;
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }
}
