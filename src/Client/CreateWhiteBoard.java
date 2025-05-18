// CreateWhiteBoard.java
package Client;

import Client.ClientUI.MainClientUI;
import RMI.RemoteWhiteBoards;
import Server.WhiteBoards;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CreateWhiteBoard {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java CreateWhiteBoard <serverIP> <serverPort> <username>");
            return;
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        try {
            LocateRegistry.createRegistry(port);
            RemoteWhiteBoards whiteBoards = new WhiteBoards();
            Naming.rebind("rmi://" + ip + ":" + port + "/WhiteBoards", whiteBoards);

            System.out.println("WhiteBoard Manager '" + username + "' started at " + ip + ":" + port);
            Thread.sleep(500);
            MainClientUI.launchUI(ip, port, username, true);  // isManager = true
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
