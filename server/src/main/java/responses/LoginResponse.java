package responses;

public class LoginResponse {
    @Override
    public String toString() {
        return "{ \"username\":\"" + this.username + "\", \"authToken\": \"" + this.authToken + "\" }";
    }

    final String username;
    final String authToken;
    public LoginResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }
}
