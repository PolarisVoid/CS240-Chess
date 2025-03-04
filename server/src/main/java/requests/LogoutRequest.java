package requests;

public class LogoutRequest {

    final String authToken;
    public LogoutRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {return this.authToken;}
}
