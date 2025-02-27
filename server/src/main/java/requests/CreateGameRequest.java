package requests;

public class CreateGameRequest {

    String authToken;
    String gameName;
    public CreateGameRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public String getAuthToken() {return this.authToken;}
    public String getGameName() {return this.gameName;}
}
