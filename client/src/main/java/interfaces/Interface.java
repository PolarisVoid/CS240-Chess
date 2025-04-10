package interfaces;

import chess.ChessGame;
import model.GameData;
import ui.Client;
import facades.ServerFacade;

import java.io.Console;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Interface{
    static final Scanner scanner = new Scanner(System.in);
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
        String value = scanner.nextLine();
        System.out.flush();
        return value;
    }

    static int promptInt(String prompt) {
        System.out.println(prompt);
        System.out.print(">>> ");
        int value = scanner.nextInt();
        scanner.nextLine();
        System.out.flush();
        return value;
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
    public void setChessGame(ChessGame chessGame) {throw new RuntimeException("Not Implemented");}
}
