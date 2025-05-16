package Server;

import RMI.RemoteWhiteBoards;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            RemoteWhiteBoards server = new WhiteBoards();
            Naming.rebind("WhiteBoards", (Remote) server);
            System.out.println("RMI Server started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
