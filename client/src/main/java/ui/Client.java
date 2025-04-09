package ui;

import interfaces.Interface;
import interfaces.PreLoginInterface;


public class Client {

    private Interface anInterface;
    private final ServerFacade serverFacade;
    private String authToken;

    public Client(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
        anInterface = new PreLoginInterface(this);
        anInterface.help();
        String command = "";
        while (!command.equals("Quit")) {
            command = anInterface.ui();
            processCommand(command);
        }
    }

    private void processCommand(String command) {
        switch (command) {
            case "Help"         -> anInterface.help();
            case "Quit"         -> {}
            case "Login"        -> anInterface.login();
            case "Register"     -> anInterface.register();
            case "Logout"       -> anInterface.logout();
            case "Create"       -> anInterface.createGame();
            case "List"         -> anInterface.listGame();
            case "Join"         -> anInterface.joinGame();
            case "Observe"      -> anInterface.observeGame();
            case "Redraw"       -> anInterface.redrawChessBoard();
            case "Leave"        -> anInterface.leaveGame();
            case "Move"         -> anInterface.makeMove();
            case "Resign"       -> anInterface.resign();
            case "Legal Moves"  -> anInterface.highlightLegalMoves();
            case null, default  -> System.out.println("Invalid Command");
        }
    }

    public ServerFacade getServerFacade() {
        return this.serverFacade;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setInterface(Interface anInterface) {
        this.anInterface = anInterface;
    }
}
