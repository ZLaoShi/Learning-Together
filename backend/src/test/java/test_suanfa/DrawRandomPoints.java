package test_suanfa;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawRandomPoints extends JPanel {

    private static final int SIZE = 400;
    private static final int MIN_DISTANCE = 30;
    private static final int POINT_COUNT = 4;
    private List<Point> points;

    public DrawRandomPoints() {
        points = generateValidPoints();
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    private List<Point> generateValidPoints() {
        List<Point> validPoints = new ArrayList<>();
        Random random = new Random();

        while (validPoints.size() < POINT_COUNT) {
            Point newPoint = new Point(random.nextInt(SIZE), random.nextInt(SIZE));

            if (isValidPoint(newPoint, validPoints)) {
                validPoints.add(newPoint);
            }
        }

        return validPoints;
    }

    private boolean isValidPoint(Point newPoint, List<Point> points) {
        for (Point point : points) {
            if (newPoint.distance(point) < MIN_DISTANCE) {
                return false;
            }
        }

        int n = points.size();
        if (n >= 2) {
            Point p1 = points.get(n - 2);
            Point p2 = points.get(n - 1);
            double slope1 = (double) (p2.y - p1.y) / (p2.x - p1.x);
            double slope2 = (double) (newPoint.y - p2.y) / (newPoint.x - p2.x);

            if (Math.abs(slope1 - slope2) < 0.1) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);

        // Draw points
        for (Point point : points) {
            g.fillOval(point.x - 3, point.y - 3, 6, 6);  // Draw point
        }

        // Draw lines between points
        g.setColor(Color.RED);
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());
            g.drawLine(p1.x, p1.y, p2.x, p2.y);  // Draw line
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw Random Points");
        DrawRandomPoints panel = new DrawRandomPoints();
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
