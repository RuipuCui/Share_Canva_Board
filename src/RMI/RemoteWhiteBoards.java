package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface RemoteWhiteBoards extends Remote {
    public void newWhiteBoard() throws RemoteException;
    public void removeWhiteBoard(RemoteWhiteBoard board) throws RemoteException;
    public List<RemoteWhiteBoard> getWhiteBoards() throws RemoteException;
    public int getWhiteBoardNum() throws RemoteException;
    public RemoteWhiteBoard getOneWhiteBoard(int index) throws RemoteException;
    public void sendChatMessage(String name, String message) throws RemoteException;
    public List<String> getChatMessages() throws RemoteException;
    public abstract void addUser(String username) throws RemoteException;
    public abstract List<String> getUsers() throws RemoteException;
    public abstract void removeUser(String username) throws RemoteException;
    public abstract void sendGroupMessage(String message) throws RemoteException;
    public abstract boolean addUserToWaiting(String username) throws RemoteException;
    public abstract List<String> getWaitingUsers() throws RemoteException;
    public abstract void removeWaitingUser(String username) throws RemoteException;
    public abstract void approveUser(String username) throws RemoteException;
    public abstract void kickUser(String username) throws RemoteException;
    public abstract boolean checkPermission(String username) throws RemoteException;
}
