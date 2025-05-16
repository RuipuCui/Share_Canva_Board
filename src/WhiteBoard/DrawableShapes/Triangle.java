package WhiteBoard.DrawableShapes;

import java.awt.*;

public class Triangle extends DrawableShape {
    private Polygon triangle = new Polygon();

    public Triangle(Point start) {
        triangle.addPoint(start.x, start.y);
        triangle.addPoint(start.x, start.y);
        triangle.addPoint(start.x, start.y);
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
}
