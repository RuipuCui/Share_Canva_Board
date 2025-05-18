package Client.ClientUI;

import RMI.RemoteWhiteBoard;
import WhiteBoard.DrawableShapes.*;
import WhiteBoard.DrawableShapes.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class WhiteBoardUI extends JPanel {
    //private List<DrawableShape> shapes = new ArrayList<>();
    private DrawableShape currentShape = null;
    private String currentTool = "Freehand";
    private Point startPoint;
    private RemoteWhiteBoard remoteWhiteBoard;

    public WhiteBoardUI(RemoteWhiteBoard remoteWhiteBoard) {
        setBackground(Color.WHITE);
        this.remoteWhiteBoard = remoteWhiteBoard;

        MouseAdapter mouseHandler = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                switch (currentTool) {
                    case "Freehand":
                        currentShape = new Path();
                        ((Path) currentShape).addPoint(startPoint);
                        break;
                    case "Rectangle":
                        currentShape = new Rectangle(startPoint);
                        break;
                    case "Oval":
                        currentShape = new Oval(startPoint);
                        break;
                    case "Triangle":
                        currentShape = new Triangle(startPoint);
                        break;
                    case "Line":
                        currentShape = new Line(startPoint);
                        break;
                }
            }

            public void mouseDragged(MouseEvent e) {
                if (currentShape != null) {
                    Point currentPoint = e.getPoint();
                    if (currentShape instanceof Path) {
                        ((Path) currentShape).addPoint(currentPoint);
                    } else {
                        currentShape.updateShape(startPoint, currentPoint);
                    }
                    repaint();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (currentShape != null) {
                    try {
                        remoteWhiteBoard.addShape(currentShape);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    currentShape = null;
                    repaint();
                    //saveCanvasToFile("CanvasFile/Canvas_1.dat");
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void setTool(String tool) {
        this.currentTool = tool;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        try {
            for (DrawableShape s : this.remoteWhiteBoard.getShapes()) {
                s.draw(g2);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        if (currentShape != null) {
            currentShape.draw(g2);
        }
    }

    public void saveCanvasToFile(String path) throws RemoteException {
        this.remoteWhiteBoard.saveCanvasToFile(path);
    }

    @SuppressWarnings("unchecked")
    public void loadCanvasFromFile(String path) throws RemoteException {
        this.remoteWhiteBoard.loadCanvasFromFile(path);
        repaint();
    }

    public void exportAsImage(String path, String format) throws RemoteException {
        this.remoteWhiteBoard.exportAsImage(path, format, getWidth(), getHeight());
    }

}
