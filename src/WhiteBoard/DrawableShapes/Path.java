package WhiteBoard.DrawableShapes;

import java.awt.*;
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
}
