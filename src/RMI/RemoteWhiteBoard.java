package RMI;

import WhiteBoard.DrawingWhiteBoard;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteWhiteBoard extends Remote {
    public void setTool(String tool) throws RemoteException;
    public void paintComponent(Graphics g) throws RemoteException;
    public void saveCanvasToFile(String path) throws RemoteException;
    public void loadCanvasFromFile(String path) throws RemoteException;
    public void exportAsImage(String path, String format) throws RemoteException;

}
