package requests;

public class JoinGameRequest {

    String authToken;
    public JoinGameRequest(String authToken) {
        this.authToken = authToken;
    }
}
