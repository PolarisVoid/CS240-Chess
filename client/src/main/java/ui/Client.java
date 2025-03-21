package ui;

public class Client {

    private boolean loggedIn;
    public Client() {
        String command = "";
        while (!command.equals("Quit")) {
            if (!loggedIn) {
                command = PreLoginUI();
            } else {
                command = PostLoginUI();
            }

            ProcessCommand(command);
        }
    }

    private void ProcessCommand(String command) {
        switch (command) {
            case "Help"         -> {}
            case "Quit"         -> {}
            case "Login"        -> {}
            case "Register"     -> {}
            case "Logout"       -> {}
            case "Create"       -> {}
            case "List"         -> {}
            case "Join"         -> {}
            case "Observe"      -> {}
            case null, default  -> {}
        }
    }

    private String PreLoginUI() {
        return "";
    }

    private String PostLoginUI() {
        return "";
    }
}
