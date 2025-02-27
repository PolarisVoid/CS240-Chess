package responses;

public class RegisterResponse {
    @Override
    public String toString() {
        return "{ \"username\":\"" + this.username + "\", authToken\": \"" + this.authToken + "\" }";
    }

    String username;
    String authToken;
    RegisterResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }
}
