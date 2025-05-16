package RMI;

import WhiteBoard.DrawableShapes.DrawableShape;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;


public interface RemoteWhiteBoard extends Remote {
    public void addShape(DrawableShape shape) throws RemoteException;
    public List<DrawableShape> getShapes() throws RemoteException;

    public void saveCanvasToFile(String path) throws RemoteException;
    public void loadCanvasFromFile(String path)throws RemoteException;
    public void exportAsImage(String path, String format) throws RemoteException;

}
