package Server;

import Client.WhiteBoardUI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class WhiteBoards {

    private List<WhiteBoardUI> whiteBoards = new ArrayList<>();
    private int whiteBoardNum = 0;
    public WhiteBoards() {
        newWhiteBoard();
    }

    public void newWhiteBoard() {
        WhiteBoardUI whiteBoard = new WhiteBoardUI();
        this.whiteBoards.add(whiteBoard);
        whiteBoardNum ++;
    }

    public void removeWhiteBoard(){

    }

    public List<WhiteBoardUI> getWhiteBoards(){
        return this.whiteBoards;
    }

    public int getWhiteBoardNum(){
        return this.whiteBoardNum;
    }



}
