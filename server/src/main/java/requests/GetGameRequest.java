package requests;

public class GetGameRequest {

    String authToken;
    public GetGameRequest(String authToken) {
        this.authToken = authToken;
    }

    String getAuthToken() {return this.authToken;}
}
