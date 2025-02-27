package requests;

public class LogoutRequest {

    String authToken;
    public LogoutRequest(String authToken) {
        this.authToken = authToken;
    }

    String getAuthToken() {return this.authToken;}
}
