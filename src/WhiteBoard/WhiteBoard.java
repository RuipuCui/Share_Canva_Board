package WhiteBoard;

import RMI.RemoteWhiteBoard;
import WhiteBoard.DrawableShapes.DrawableShape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhiteBoard extends UnicastRemoteObject implements RemoteWhiteBoard {
    private List<DrawableShape> shapes = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    public UUID getId() {
        return id;
    }

    public WhiteBoard() throws RemoteException {
        super();
    }

    @Override
    public synchronized void addShape(DrawableShape shape) throws RemoteException {
        shapes.add(shape);
    }

    @Override
    public synchronized List<DrawableShape> getShapes() throws RemoteException {
        return new ArrayList<>(shapes); // defensive copy
    }

    public synchronized void saveCanvasToFile(String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(shapes);
            System.out.println("Canvas saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadCanvasFromFile(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            shapes = (List<DrawableShape>) ois.readObject();
            //repaint();
            System.out.println("Canvas loaded.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void exportAsImage(String path, String format, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        for (DrawableShape shape : shapes) {
            shape.draw(g2);
        }

        g2.dispose();

        try {
            ImageIO.write(image, format, new File(path));
            System.out.println("Canvas exported as " + format.toUpperCase() + " to " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void removeShape(DrawableShape shape) {
        shapes.removeIf(s -> s.getId().equals(shape.getId()));
    }

}
