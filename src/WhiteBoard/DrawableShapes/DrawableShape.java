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
import java.util.UUID;

public abstract class DrawableShape implements Serializable {
    Color color = Color.BLACK;
    private final UUID id = UUID.randomUUID();
    public UUID getId() {
        return id;
    }

    public void setColor(Color color){
        this.color = color;
    };

    public abstract void draw(Graphics2D g2);

    // For rectangle and oval updates
    public void updateShape(Point p1, Point p2) {};
    public abstract boolean containsPoint(Point p);
    public abstract boolean intersectsCircle(Point center, int radius);


}
