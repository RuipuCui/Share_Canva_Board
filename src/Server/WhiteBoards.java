package Server;

import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;
import WhiteBoard.WhiteBoard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WhiteBoards extends UnicastRemoteObject implements RemoteWhiteBoards{
    private List<String> chat = new ArrayList<>();
    private List<RemoteWhiteBoard> whiteBoards = new ArrayList<>();
    private int whiteBoardNum = 0;
    private List<String> users = new ArrayList<>();
    private List<String> waitingUsers = new ArrayList<>();
    private String managerName = null;
    public WhiteBoards() throws RemoteException {
        newWhiteBoard();
    }

    public synchronized void newWhiteBoard() throws RemoteException {
        WhiteBoard whiteBoard = new WhiteBoard();
        this.whiteBoards.add(whiteBoard);
        whiteBoardNum ++;
    }

    public synchronized void removeWhiteBoard(RemoteWhiteBoard board){
        whiteBoards.removeIf(s -> {
            try {
                whiteBoardNum --;
                return s.getId().equals(board.getId());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public synchronized List<RemoteWhiteBoard> getWhiteBoards(){
        return this.whiteBoards;
    }

    public synchronized int getWhiteBoardNum(){
        return this.whiteBoards.size();
    }

    public synchronized RemoteWhiteBoard getOneWhiteBoard(int index){
        return this.whiteBoards.get(index);
    }

    public synchronized void sendChatMessage(String name, String message){
        this.chat.add(name + ": " + message);
    }

    public synchronized void sendGroupMessage(String message){
        this.chat.add("System Message: " + message);
    }

    public synchronized List<String> getChatMessages(){
        return this.chat;
    }

    public synchronized void addUser(String username){
        if(managerName == null){
            managerName = username;
        }
        users.add(username);
    }

    public synchronized void removeUser(String username){
        users.remove(username);
    }

    public synchronized List<String> getUsers(){
        return users;
    }

    public synchronized boolean addUserToWaiting(String username){
        if(users.contains(username) || waitingUsers.contains(username)){
            return false;
        }
        waitingUsers.add(username);
        return true;
    }

    public synchronized List<String> getWaitingUsers(){
        return waitingUsers;
    }

    public synchronized void removeWaitingUser(String username){
        waitingUsers.remove(username);
    }

    public synchronized void approveUser(String username){
        addUser(username);
        removeWaitingUser(username);
    }

    public synchronized void kickUser(String username){
        removeUser(username);
    }

    public boolean checkPermission(String username){
        return managerName.equals(username);
    }


}
