package responses;

public class LoginResponse {
    @Override
    public String toString() {
        return "{ \"username\":\"" + this.username + "\", authToken\": \"" + this.authToken + "\" }";
    }

    String username;
    String authToken;
    public LoginResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }
}
