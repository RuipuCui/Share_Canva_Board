// CreateWhiteBoard.java
package Client;

import Client.ClientUI.MainClientUI;
import Client.ClientUI.RemoteHandler;
import RMI.RemoteWhiteBoards;
import Server.WhiteBoards;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.UUID;

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
            RemoteWhiteBoards remote = RemoteHandler.getRemoteWhiteBoards(ip, port);
            remote.addUser(username + "(manager)");
            MainClientUI.launchUI(ip, port, username, true, remote);  // isManager = true
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
