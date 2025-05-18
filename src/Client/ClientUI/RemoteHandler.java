package Client.ClientUI;

import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteHandler {
    public static RemoteWhiteBoards getRemoteWhiteBoards(String ip, int port) throws Exception {
        String url = "rmi://" + ip + ":" + port + "/WhiteBoards";

        int retries = 5;
        for (int i = 0; i < retries; i++) {
            try {
                return (RemoteWhiteBoards) Naming.lookup(url);
            } catch (Exception e) {
                if (i == retries - 1) throw e;
                Thread.sleep(200);
            }
        }
        throw new RuntimeException("Could not connect to " + url);
    }
}

