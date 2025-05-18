package WhiteBoard.DrawableShapes;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class Triangle extends DrawableShape {
    private Polygon triangle = new Polygon();

    public Triangle(Point start, Color color) {
        triangle.addPoint(start.x, start.y);
        triangle.addPoint(start.x, start.y);
        triangle.addPoint(start.x, start.y);
        setColor(color);
    }

    public void updateShape(Point p1, Point p2) {
        triangle.reset();
        int x1 = p1.x;
        int y1 = p2.y;
        int x2 = (p1.x + p2.x) / 2;
        int y2 = p1.y;
        int x3 = p2.x;
        int y3 = p2.y;

        triangle.addPoint(x1, y1);
        triangle.addPoint(x2, y2);
        triangle.addPoint(x3, y3);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.draw(triangle);
    }

    @Override
    public boolean containsPoint(Point p) {
        return triangle.contains(p);
    }

    @Override
    public boolean intersectsCircle(Point center, int radius) {
        // Create a stroked shape that represents the visible border
        Stroke stroke = new BasicStroke(2); // Adjust stroke width if needed
        Shape strokedRect = stroke.createStrokedShape(triangle);

        // Create the eraser circle shape
        Ellipse2D eraserCircle = new Ellipse2D.Double(
                center.x - radius, center.y - radius,
                radius * 2.0, radius * 2.0
        );

        return strokedRect.intersects(eraserCircle.getBounds2D());
    }



}
