package WhiteBoard.DrawableShapes;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class Path extends DrawableShape {
    private Path2D path = new Path2D.Double();

    public void addPoint(Point p) {
        if (path.getCurrentPoint() == null) {
            path.moveTo(p.x, p.y);
        } else {
            path.lineTo(p.x, p.y);
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.draw(path);
    }

    @Override
    public boolean containsPoint(Point p) {
        return path.contains(p);
    }

    @Override
    public boolean intersectsCircle(Point center, int radius) {
        // Create a stroked version of the path (approximate thickness of pen)
        Stroke stroke = new BasicStroke(2); // Adjust stroke width to match drawing thickness
        Shape strokedPath = stroke.createStrokedShape(path);

        // Create eraser circle shape
        Ellipse2D eraserCircle = new Ellipse2D.Double(
                center.x - radius, center.y - radius,
                radius * 2.0, radius * 2.0
        );

        return strokedPath.intersects(eraserCircle.getBounds2D());
    }



}
