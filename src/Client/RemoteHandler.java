package Client;

import RMI.RemoteWhiteBoard;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteHandler {
    public RemoteHandler() {
    }

    public RemoteWhiteBoard getRemoteWhiteBoards() throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry("localhost");
        RemoteWhiteBoard remoteWhiteBoards = (RemoteWhiteBoard) registry.lookup("WhiteBoards");
        System.out.println("Client: calling remote methods");
        return remoteWhiteBoards;

    }
}
