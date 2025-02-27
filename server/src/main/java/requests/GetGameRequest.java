package requests;

public class GetGameRequest {

    String authToken;
    public GetGameRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {return this.authToken;}
}
