package voronoidiagram;

/**
 * Represents a single 2D point
 */
public class Point implements Comparable<Point> {
  //The coordinates
  public final double x;
  public final double y;

  /**
   * Generates a new Point
   * @param x the x-coordinate
   * @param y the y-coordinate
   */
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Checks if this point is equal to the given object by comparing coordinates, if possible.
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Point) {
      Point p = (Point) o;
      return compareTo(p) == 0;
    } else {
      return false;
    }
  }

  /**
   * Compares this point to the given point based on y-coordinate. Considers two coordinates to
   * be equal if their difference is less than 0.001.
   * @param o the point being compared
   * @return 1 if this point is greater than the other, -1 if the other is greater than this, 0 if
   *          they are equivalent
   */
  @Override
  public int compareTo(Point o) {
    int yComp = Util.doubleComp(this.y, o.y);
    if (yComp > 0) {
      return 1;
    } else if (yComp < 0) {
      return -1;
    }
    return 0;
  }
}
