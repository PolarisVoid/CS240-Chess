package requests;

public class RegisterRequest {

    final String username;
    final String password;
    final String email;
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public String getEmail() {return this.email;}
}
