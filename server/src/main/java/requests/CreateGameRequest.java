package requests;

public class CreateGameRequest {

    String authToken;
    String gameName;
    public CreateGameRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }
}
