package Server;
import RMI.RemoteWhiteBoards;
import Server.WhiteBoards;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RMIServer <serverIP> <serverPort>");
            return;
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try {
            // Start RMI registry on specified port
            LocateRegistry.createRegistry(serverPort);
            System.out.println("RMI Registry started on port " + serverPort);

            // Create and bind the RemoteWhiteBoards instance
            RemoteWhiteBoards server = new WhiteBoards();
            String bindAddress = "rmi://" + serverIP + ":" + serverPort + "/WhiteBoards";
            Naming.rebind(bindAddress, (Remote) server);
            System.out.println("WhiteBoards bound at " + bindAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

