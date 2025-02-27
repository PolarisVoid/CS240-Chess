package requests;

public class RegisterRequest {

    String username;
    String password;
    String email;
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    String getUsername() {return this.username;}
    String getPassword() {return this.password;}
    String getEmail() {return this.email;}
}
