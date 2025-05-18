package WhiteBoard.DrawableShapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Rectangle extends DrawableShape {
    private Rectangle2D rect;

    public Rectangle(Point start, Color color) {
        rect = new Rectangle2D.Double(start.x, start.y, 0, 0);
        setColor(color);
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

    @Override
    public boolean containsPoint(Point p) {
        return rect.contains(p);
    }

    @Override
    public boolean intersectsCircle(Point center, int radius) {
        // Create a stroked shape that represents the visible border
        Stroke stroke = new BasicStroke(2); // Adjust stroke width if needed
        Shape strokedRect = stroke.createStrokedShape(rect);

        // Create the eraser circle shape
        Ellipse2D eraserCircle = new Ellipse2D.Double(
                center.x - radius, center.y - radius,
                radius * 2.0, radius * 2.0
        );

        return strokedRect.intersects(eraserCircle.getBounds2D());
    }



}
