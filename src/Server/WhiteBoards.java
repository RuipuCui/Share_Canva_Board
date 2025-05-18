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
    public WhiteBoards() throws RemoteException {
        newWhiteBoard();
    }

    public synchronized void newWhiteBoard() throws RemoteException {
        WhiteBoard whiteBoard = new WhiteBoard();
        this.whiteBoards.add(whiteBoard);
        whiteBoardNum ++;
    }

    public synchronized void removeWhiteBoard(){
    }

    public synchronized List<RemoteWhiteBoard> getWhiteBoards(){
        return this.whiteBoards;
    }

    public synchronized int getWhiteBoardNum(){
        return this.whiteBoardNum;
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

    public synchronized boolean addUser(String username){
        if(users.contains(username)){
            return false;
        }
        users.add(username);
        return true;
    }

    public synchronized List<String> getUsers(){
        return users;
    }


}
