package interfaces;

import exception.AlreadyTaken;
import exception.BadRequest;
import exception.Unauthorized;
import model.AuthData;
import ui.Client;

import java.util.Arrays;

public class PreLoginInterface extends Interface {
    private static final String[] COMMANDS = {"Help", "Quit", "Login", "Register"};

    public PreLoginInterface(Client client) {
        super(client, COMMANDS);
        help();
    }

    public void help() {
        System.out.println("Help - Displays commands the user can run");
        System.out.println("Quit - Exits the program");
        System.out.println("Login - Allows for signing in");
        System.out.println("Register - Allows for creating a new account");
    }

    public void login() {
        String username = promptString("Enter Your Username:");
        String password = promptString("Enter Your Password:");

        try {
            AuthData authData = serverFacade.login(username, password);
            client.setAuthToken(authData.authToken());
        } catch (Unauthorized e) {
            System.out.println("Invalid Login");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        client.setInterface(new PostLoginInterface(client));
    }

    public void register() {
        String username = promptString("Enter Your Desired Username:");
        String password = promptString("Enter Your Desired Password:");
        String email = promptString("Enter Your Email Address:");

        try {
            AuthData authData = serverFacade.register(username, password, email);
            client.setAuthToken(authData.authToken());
        } catch (AlreadyTaken e) {
            System.out.println("Username Already Taken.");
            return;
        } catch (BadRequest e) {
            System.out.println("Bad User Input. Please fill in all fields with valid values.");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        client.setInterface(new PostLoginInterface(client));
    }

}
