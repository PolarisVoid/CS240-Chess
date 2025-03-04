package responses;

public class RegisterResponse {
    @Override
    public String toString() {
        return "{ \"username\":\"" + this.username + "\", \"authToken\": \"" + this.authToken + "\" }";
    }

    final String username;
    final String authToken;
    public RegisterResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }
}
