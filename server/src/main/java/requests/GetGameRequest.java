package requests;

public class GetGameRequest {

    final String authToken;
    public GetGameRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {return this.authToken;}
}
