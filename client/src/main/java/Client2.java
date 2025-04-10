import facades.ServerFacade;
import ui.Client;

public class Client2 {
    public static void main(String[] args) {
        String port = "8080";
        if (args.length == 1) {
            port = args[0];
        }
        int portNum = Integer.parseInt(port);

        new Client(new ServerFacade(portNum));
    }
}
