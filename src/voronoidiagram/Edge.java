package voronoidiagram;

/**
 * Represents an edge in the diagram
 */
public class Edge {
  public Point start;
  public Point end;
  private Edge neighbor;
  final double slope;
  final double yAtZero;

  Edge(Point start, Point leftSite, Point rightSite) {
    this.start = start;

    //Direction is the vector perpendicular to the line between the left & right sites
    this.slope = (rightSite.x - leftSite.x)/(leftSite.y - rightSite.y);

    //End has yet to be found
    end = null;

    //Y when x = 0
    this.yAtZero = start.y - slope * start.x;
  }


  /**
   * Finds the intersection of this edge with another edge
   * @param other the other edge
   * @return the point of intersection
   */
  Point intersectionWith(Edge other) {
    if (this.slope == other.slope && this.yAtZero != other.yAtZero) return null;
    double x = (other.yAtZero - this.yAtZero) / (this.slope - other.slope);
    double y = this.slope * x + this.yAtZero;
    return new Point(x, y);
  }

  /**
   * Sets the neighbor of this edge as well as that of the new neighbor edge
   * @param neighbor the new neighbor
   */
  void setNeighbor(Edge neighbor) {
    this.neighbor = neighbor;
    this.neighbor.neighbor = this;
  }
}
