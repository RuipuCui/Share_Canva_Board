package WhiteBoard.DrawableShapes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class DrawableShape implements Serializable {
    Color color = Color.BLACK;

    public abstract void draw(Graphics2D g2);

    // For rectangle and oval updates
    public void updateShape(Point p1, Point p2) {}
}
