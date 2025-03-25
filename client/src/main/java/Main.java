import server.Server;
import ui.Client;
import ui.ServerFacade;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(8080);
        new Client(new ServerFacade(port));
        server.stop();
    }
}