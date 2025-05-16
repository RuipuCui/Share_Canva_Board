package Client;

import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteHandler {

    public static RemoteWhiteBoards getRemoteWhiteBoards() throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry("localhost");
        RemoteWhiteBoards remoteWhiteBoards = (RemoteWhiteBoards) registry.lookup("WhiteBoards");
        System.out.println("Client: calling remote methods");
        return remoteWhiteBoards;

    }
}
