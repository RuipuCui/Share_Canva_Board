package WhiteBoard.DrawableShapes;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class Oval extends DrawableShape {
    private Ellipse2D oval;

    public Oval(Point start, Color color) {
        oval = new Ellipse2D.Double(start.x, start.y, 0, 0);
        setColor(color);
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

    @Override
    public boolean containsPoint(Point p) {
        return oval.contains(p);
    }

    @Override
    public boolean intersectsCircle(Point center, int radius) {
        // Use the shape's outline as a stroke
        Stroke stroke = new BasicStroke(2); // thickness of oval border, adjust if needed
        Shape strokedOval = stroke.createStrokedShape(oval);

        // Create eraser circle shape
        Ellipse2D eraserCircle = new Ellipse2D.Double(
                center.x - radius, center.y - radius,
                radius * 2.0, radius * 2.0
        );

        return strokedOval.intersects(eraserCircle.getBounds2D());
    }



}
