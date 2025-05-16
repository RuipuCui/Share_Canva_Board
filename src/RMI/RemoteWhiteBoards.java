package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteWhiteBoards extends Remote {
    public void newWhiteBoard() throws RemoteException;
    public void removeWhiteBoard() throws RemoteException;
    public List<RemoteWhiteBoard> getWhiteBoards() throws RemoteException;
    public int getWhiteBoardNum() throws RemoteException;
    public RemoteWhiteBoard getOneWhiteBoard(int index) throws RemoteException;
}
