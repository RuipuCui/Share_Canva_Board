package Server;

import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;
import WhiteBoard.WhiteBoard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class WhiteBoards extends UnicastRemoteObject implements RemoteWhiteBoards{
    private List<String> chat = new ArrayList<>();
    private List<RemoteWhiteBoard> whiteBoards = new ArrayList<>();
    private int whiteBoardNum = 0;
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

    public void sendChatMessage(String name, String message){
        this.chat.add(name + ": " + message);
    }

    public List<String> getChatMessages(){
        return this.chat;
    }

}
