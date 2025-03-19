import server.Server;
import ui.Client;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(0);
        new Client();
    }
}