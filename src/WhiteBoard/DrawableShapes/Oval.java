package WhiteBoard.DrawableShapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Oval extends DrawableShape {
    private Ellipse2D oval;

    public Oval(Point start) {
        oval = new Ellipse2D.Double(start.x, start.y, 0, 0);
    }

    public void updateShape(Point p1, Point p2) {
        double x = Math.min(p1.x, p2.x);
        double y = Math.min(p1.y, p2.y);
        double w = Math.abs(p1.x - p2.x);
        double h = Math.abs(p1.y - p2.y);
        oval.setFrame(x, y, w, h);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.draw(oval);
    }
}
