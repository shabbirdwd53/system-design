package com.dailycodebuffer.system_design.QuadTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class QuadTreeVisualizer extends JPanel {

    // A simple 2D point class.
    static class Point {
        double x, y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    // A rectangle class representing the boundary of each QuadTree node.
    // x and y are the center coordinates; w and h are half the width and half the height.
    static class Rectangle {
        double x, y, w, h;
        public Rectangle(double x, double y, double w, double h) {
            this.x = x; this.y = y; this.w = w; this.h = h;
        }
        // Checks if the rectangle contains a point.
        public boolean contains(Point p) {
            return (p.x >= x - w && p.x <= x + w &&
                    p.y >= y - h && p.y <= y + h);
        }
        // Checks if the rectangle intersects another rectangle.
        public boolean intersects(Rectangle range) {
            return !(range.x - range.w > x + w ||
                    range.x + range.w < x - w ||
                    range.y - range.h > y + h ||
                    range.y + range.h < y - h);
        }
    }

    // The QuadTree class for spatial partitioning.
    static class QuadTree {
        Rectangle boundary;   // The area covered by this node.
        int capacity;         // Maximum points allowed before subdividing.
        List<Point> points;   // Points stored in this node.
        boolean divided;      // Flag to indicate if this node has been subdivided.
        QuadTree northeast;
        QuadTree northwest;
        QuadTree southeast;
        QuadTree southwest;

        public QuadTree(Rectangle boundary, int capacity) {
            this.boundary = boundary;
            this.capacity = capacity;
            points = new ArrayList<>();
            divided = false;
        }

        // Insert a point into the QuadTree.
        public boolean insert(Point p) {
            if (!boundary.contains(p)) {
                return false; // The point does not lie within this boundary.
            }
            if (points.size() < capacity) {
                points.add(p);
                return true;
            } else {
                if (!divided) {
                    subdivide();
                }
                if (northeast.insert(p)) return true;
                if (northwest.insert(p)) return true;
                if (southeast.insert(p)) return true;
                if (southwest.insert(p)) return true;
            }
            return false; // Should not reach here.
        }

        // Subdivide this node into four children.
        public void subdivide() {
            double x = boundary.x;
            double y = boundary.y;
            double w = boundary.w;
            double h = boundary.h;

            // In Swing's coordinate system, y increases downward.
            // We'll create four quadrants:
            // top-right, top-left, bottom-right, and bottom-left.
            Rectangle topRight = new Rectangle(x + w/2, y - h/2, w/2, h/2);
            Rectangle topLeft  = new Rectangle(x - w/2, y - h/2, w/2, h/2);
            Rectangle bottomRight = new Rectangle(x + w/2, y + h/2, w/2, h/2);
            Rectangle bottomLeft  = new Rectangle(x - w/2, y + h/2, w/2, h/2);

            northeast = new QuadTree(topRight, capacity);
            northwest = new QuadTree(topLeft, capacity);
            southeast = new QuadTree(bottomRight, capacity);
            southwest = new QuadTree(bottomLeft, capacity);

            divided = true;
        }

        // Recursively draw this node's boundary, its points, and its children.
        public void draw(Graphics2D g) {
            // Convert our boundary to a rectangle (top-left corner, width, height)
            int drawX = (int) (boundary.x - boundary.w);
            int drawY = (int) (boundary.y - boundary.h);
            int drawW = (int) (boundary.w * 2);
            int drawH = (int) (boundary.h * 2);
            g.drawRect(drawX, drawY, drawW, drawH);

            // Draw each point as a small filled circle.
            for (Point p : points) {
                int px = (int) p.x;
                int py = (int) p.y;
                g.fillOval(px - 2, py - 2, 4, 4);
            }

            // Recursively draw the subdivided quadrants.
            if (divided) {
                northeast.draw(g);
                northwest.draw(g);
                southeast.draw(g);
                southwest.draw(g);
            }
        }
    }

    // Panel dimensions.
    static final int PANEL_WIDTH = 800;
    static final int PANEL_HEIGHT = 800;

    QuadTree quadTree;

    public QuadTreeVisualizer() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        // The entire panel is the boundary. Center is at (PANEL_WIDTH/2, PANEL_HEIGHT/2)
        // and the half-dimensions are (PANEL_WIDTH/2, PANEL_HEIGHT/2).
        Rectangle boundary = new Rectangle(PANEL_WIDTH / 2.0, PANEL_HEIGHT / 2.0, PANEL_WIDTH / 2.0, PANEL_HEIGHT / 2.0);
        quadTree = new QuadTree(boundary, 1);

        // Insert some random points for initial visualization.
        for (int i = 0; i < 10; i++) {
            double x = Math.random() * PANEL_WIDTH;
            double y = Math.random() * PANEL_HEIGHT;
            quadTree.insert(new Point(x, y));
        }

        // Add a mouse listener to allow adding points on click.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                quadTree.insert(new Point(e.getX(), e.getY()));
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Enable anti-aliasing for smoother lines.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        quadTree.draw(g2d);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("QuadTree Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        QuadTreeVisualizer panel = new QuadTreeVisualizer();
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
