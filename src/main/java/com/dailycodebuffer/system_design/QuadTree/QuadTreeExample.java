package com.dailycodebuffer.system_design.QuadTree;

import java.util.ArrayList;
import java.util.List;

// A simple class to represent a 2D point.
class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

// Class representing a rectangular boundary.
// The rectangle is defined by its center (x, y) and half-dimensions (w, h).
class Rectangle {
    double x, y, w, h;

    public Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w; // half of the total width
        this.h = h; // half of the total height
    }

    // Checks if this rectangle contains a given point.
    public boolean contains(Point p) {
        return (p.x >= (x - w) && p.x < (x + w) &&
                p.y >= (y - h) && p.y < (y + h));
    }

    // Checks if this rectangle intersects with another rectangle (range).
    public boolean intersects(Rectangle range) {
        return !(range.x - range.w > x + w ||
                range.x + range.w < x - w ||
                range.y - range.h > y + h ||
                range.y + range.h < y - h);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}

// The QuadTree class for spatial partitioning.
class QuadTree {
    Rectangle boundary;    // The region represented by this node.
    int capacity;          // Maximum number of points before subdivision.
    List<Point> points;    // Points contained in this QuadTree node.
    boolean divided;       // Flag to indicate whether this node has been subdivided.

    // Child QuadTrees.
    QuadTree northeast;
    QuadTree northwest;
    QuadTree southeast;
    QuadTree southwest;

    public QuadTree(Rectangle boundary, int capacity) {
        this.boundary = boundary;
        this.capacity = capacity;
        this.points = new ArrayList<>();
        this.divided = false;
        System.out.println("Boundry: " + boundary);
    }

    // Subdivide the current node into four quadrants.
    public void subdivide() {
        double x = boundary.x;
        double y = boundary.y;
        double w = boundary.w;
        double h = boundary.h;

        Rectangle ne = new Rectangle(x + w / 2, y + h / 2, w / 2, h / 2);
        Rectangle nw = new Rectangle(x - w / 2, y + h / 2, w / 2, h / 2);
        Rectangle se = new Rectangle(x + w / 2, y - h / 2, w / 2, h / 2);
        Rectangle sw = new Rectangle(x - w / 2, y - h / 2, w / 2, h / 2);

        northeast = new QuadTree(ne, capacity);
        northwest = new QuadTree(nw, capacity);
        southeast = new QuadTree(se, capacity);
        southwest = new QuadTree(sw, capacity);

        divided = true;
    }

    // Insert a point into the QuadTree.
    public boolean insert(Point p) {
        // Ignore points that do not belong in this quad tree.
        if (!boundary.contains(p)) {
            return false;
        }

        // If there is room, add the point directly.
        if (points.size() < capacity) {
            points.add(p);
            return true;
        } else {
            // Otherwise, subdivide and add the point to whichever child will accept it.
            if (!divided) {
                subdivide();
            }
            if (northeast.insert(p)) return true;
            if (northwest.insert(p)) return true;
            if (southeast.insert(p)) return true;
            if (southwest.insert(p)) return true;
        }
        return false; // Should never happen.
    }

    // Query the QuadTree to find all points within a given range.
    public void query(Rectangle range, List<Point> found) {
        // If the range does not intersect this boundary, return immediately.
        if (!boundary.intersects(range)) {
            return;
        } else {
            // Otherwise, add the points that are in the range.
            for (Point p : points) {
                if (range.contains(p)) {
                    found.add(p);
                }
            }
            // If subdivided, search the children.
            if (divided) {
                northwest.query(range, found);
                northeast.query(range, found);
                southwest.query(range, found);
                southeast.query(range, found);
            }
        }
    }
}

// A class to demonstrate the QuadTree usage.
public class QuadTreeExample {
    public static void main(String[] args) {
        // Define the boundary of the QuadTree: a 200x200 region centered at (0,0)
        Rectangle boundary = new Rectangle(0, 0, 200, 200);
        QuadTree qt = new QuadTree(boundary, 4); // Capacity of 4 points per node

        // Insert random points into the QuadTree.
        int numPoints = 100;
        for (int i = 0; i < numPoints; i++) {
            double x = Math.random() * 400 - 200; // Random x in [-200, 200]
            double y = Math.random() * 400 - 200; // Random y in [-200, 200]
            Point p = new Point(x, y);
            qt.insert(p);
        }

        // Define a query range: a 100x100 rectangle centered at (0,0)
        Rectangle range = new Rectangle(0, 0, 50, 50);
        List<Point> found = new ArrayList<>();
        qt.query(range, found);

        System.out.println("Points found in the range: " + found.size());
    }
}

