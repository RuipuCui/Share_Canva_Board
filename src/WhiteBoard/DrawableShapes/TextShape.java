package WhiteBoard.DrawableShapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TextShape extends DrawableShape {
    private Point location;
    private String text;

    public TextShape(Point location, String text, Color color) {
        this.location = location;
        this.text = text;
        setColor(color);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.drawString(text, location.x, location.y);
    }

    @Override
    public boolean containsPoint(Point p) {
        Rectangle2D bounds = getBounds();
        return bounds.contains(p);
    }

    @Override
    public boolean intersectsCircle(Point center, int radius) {
        Rectangle2D bounds = getBounds();

        // Approximate intersection between eraser circle and text bounds
        double closestX = Math.max(bounds.getX(), Math.min(center.x, bounds.getX() + bounds.getWidth()));
        double closestY = Math.max(bounds.getY(), Math.min(center.y, bounds.getY() + bounds.getHeight()));
        double dx = center.x - closestX;
        double dy = center.y - closestY;
        return (dx * dx + dy * dy) <= radius * radius;
    }

    private Rectangle2D getBounds() {
        Font font = new Font("SansSerif", Font.PLAIN, 16);
        FontMetrics metrics = new Canvas().getFontMetrics(font);
        int width = metrics.stringWidth(text);
        int height = metrics.getHeight();
        return new Rectangle2D.Double(location.x, location.y - height, width, height);
    }
}

