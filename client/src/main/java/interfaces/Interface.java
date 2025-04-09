package interfaces;

import ui.Client;
import ui.ServerFacade;

import java.util.Scanner;

public class Interface{
    private static final Scanner scanner = new Scanner(System.in);
    final Client client;
    final String authToken;
    final ServerFacade serverFacade;

    public Interface(Client client) {
        this.client = client;
        this.serverFacade = this.client.getServerFacade();
        this.authToken = client.getAuthToken();
    }

    static String promptString(String prompt) {
        System.out.println(prompt);
        System.out.print(">>> ");
        return scanner.nextLine();
    }

    static int promptInt() {
        System.out.println("What is the number next to the game you want to join?");
        System.out.print(">>> ");
        return scanner.nextInt();
    }

    public String ui() {throw new RuntimeException("Not Implemented");}
    public void help() {throw new RuntimeException("Not Implemented");}
    public void login() {throw new RuntimeException("Not Implemented");}
    public void register() {throw new RuntimeException("Not Implemented");}
    public void logout() {throw new RuntimeException("Not Implemented");}
    public void createGame() {throw new RuntimeException("Not Implemented");}
    public void listGame() {throw new RuntimeException("Not Implemented");}
    public void joinGame() {throw new RuntimeException("Not Implemented");}
    public void observeGame() {throw new RuntimeException("Not Implemented");}
    public void redrawChessBoard() {throw new RuntimeException("Not Implemented");}
    public void leaveGame() {throw new RuntimeException("Not Implemented");}
    public void makeMove() {throw new RuntimeException("Not Implemented");}
    public void resign() {throw new RuntimeException("Not Implemented");}
    public void highlightLegalMoves() {throw new RuntimeException("Not Implemented");}
}
