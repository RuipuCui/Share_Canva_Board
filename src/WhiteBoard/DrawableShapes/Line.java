package WhiteBoard.DrawableShapes;

import java.awt.*;
import java.awt.geom.Line2D;

public class Line extends DrawableShape {
    private Line2D line;
    public Line(Point start) {
        line = new Line2D.Double(start, start);
    }

    public void updateShape(Point p1, Point p2) {
        line.setLine(p1, p2);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.draw(line);
    }

    @Override
    public boolean containsPoint(Point p) {
        return line.contains(p);
    }

    public boolean intersectsCircle(Point center, int radius) {
        double dist = line.ptSegDist(center);
        return dist <= radius;
    }


}