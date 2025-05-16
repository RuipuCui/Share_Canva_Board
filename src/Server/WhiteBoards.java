package Server;

import Client.WhiteBoardUI;
import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;
import WhiteBoard.WhiteBoard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class WhiteBoards extends UnicastRemoteObject implements RemoteWhiteBoards{

    private List<RemoteWhiteBoard> whiteBoards = new ArrayList<>();
    private int whiteBoardNum = 0;
    public WhiteBoards() throws RemoteException {
        newWhiteBoard();
    }

    public void newWhiteBoard() throws RemoteException {
        WhiteBoard whiteBoard = new WhiteBoard();
        this.whiteBoards.add(whiteBoard);
        whiteBoardNum ++;
    }

    public void removeWhiteBoard(){

    }

    public List<RemoteWhiteBoard> getWhiteBoards(){
        return this.whiteBoards;
    }

    public int getWhiteBoardNum(){
        return this.whiteBoardNum;
    }

    public RemoteWhiteBoard getOneWhiteBoard(int index){
        return this.whiteBoards.get(index);
    }



}
