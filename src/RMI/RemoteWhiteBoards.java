package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface RemoteWhiteBoards extends Remote {
    public void newWhiteBoard() throws RemoteException;
    public void removeWhiteBoard() throws RemoteException;
    public List<RemoteWhiteBoard> getWhiteBoards() throws RemoteException;
    public int getWhiteBoardNum() throws RemoteException;
    public RemoteWhiteBoard getOneWhiteBoard(int index) throws RemoteException;
    public void sendChatMessage(String name, String message) throws RemoteException;
    public List<String> getChatMessages() throws RemoteException;
    public abstract boolean addUser(String username) throws RemoteException;
    public abstract List<String> getUsers() throws RemoteException;
    public abstract void sendGroupMessage(String message) throws RemoteException;
}
