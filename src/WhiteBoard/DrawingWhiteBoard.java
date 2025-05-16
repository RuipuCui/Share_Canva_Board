package WhiteBoard;

import RMI.RemoteWhiteBoard;
import Server.RemoteWhiteBoards;
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
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class DrawingWhiteBoard extends JPanel implements RemoteWhiteBoard {
    private List<DrawableShape> shapes = new ArrayList<>();
    private DrawableShape currentShape = null;
    private String currentTool = "Freehand";
    private Point startPoint;

    public DrawingWhiteBoard() throws RemoteException {
        setBackground(Color.WHITE);
        //loadCanvasFromFile("CanvasFile/Canvas_1.dat");
        UnicastRemoteObject.exportObject(this, 0);

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
                    shapes.add(currentShape);
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

        for (DrawableShape s : shapes) {
            s.draw(g2);
        }

        if (currentShape != null) {
            currentShape.draw(g2);
        }
    }

    public void saveCanvasToFile(String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(shapes);
            System.out.println("Canvas saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadCanvasFromFile(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            shapes = (List<DrawableShape>) ois.readObject();
            repaint();
            System.out.println("Canvas loaded.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportAsImage(String path, String format) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        g2.dispose();

        try {
            ImageIO.write(image, format, new File(path));
            System.out.println("Canvas exported as " + format.toUpperCase() + " to " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
