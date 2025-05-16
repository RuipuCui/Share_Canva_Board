package Server;

import WhiteBoard.DrawingWhiteBoard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteWhiteBoards {

    private List<DrawingWhiteBoard> whiteBoards = new ArrayList<>();
    private int whiteBoardNum = 0;
    public RemoteWhiteBoards() throws RemoteException {
        newWhiteBoard();
    }

    public void newWhiteBoard() throws RemoteException {
        DrawingWhiteBoard whiteBoard = new DrawingWhiteBoard();
        this.whiteBoards.add(whiteBoard);
        whiteBoardNum ++;
    }

    public void removeWhiteBoard(){

    }

    public List<DrawingWhiteBoard> getWhiteBoards(){
        return this.whiteBoards;
    }

    public int getWhiteBoardNum(){
        return this.whiteBoardNum;
    }



}
