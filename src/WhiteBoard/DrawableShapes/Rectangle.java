package WhiteBoard.DrawableShapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rectangle extends DrawableShape {
    private Rectangle2D rect;

    public Rectangle(Point start) {
        rect = new Rectangle2D.Double(start.x, start.y, 0, 0);
    }

    public void updateShape(Point p1, Point p2) {
        double x = Math.min(p1.x, p2.x);
        double y = Math.min(p1.y, p2.y);
        double w = Math.abs(p1.x - p2.x);
        double h = Math.abs(p1.y - p2.y);
        rect.setRect(x, y, w, h);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.draw(rect);
    }
}
