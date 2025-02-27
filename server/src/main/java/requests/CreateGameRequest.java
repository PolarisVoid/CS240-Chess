package requests;

public class CreateGameRequest {

    String authToken;
    String gameName;
    public CreateGameRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    String getAuthToken() {return this.authToken;}
    String getGameName() {return this.gameName;}
}
