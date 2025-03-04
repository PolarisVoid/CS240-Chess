package requests;

public class CreateGameRequest {

    String authToken;
    final String gameName;
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public String getAuthToken() {return this.authToken;}
    public String getGameName() {return this.gameName;}

    public void setAuthToken(String authToken) {this.authToken = authToken;}
}
