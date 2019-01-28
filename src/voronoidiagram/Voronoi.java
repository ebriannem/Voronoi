package voronoidiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Represents the main generator of the Voronoi diagram. Uses Fortune's Algorithm.
 */
public class Voronoi {
  //Dimensions
  public final int width;
  public final int height;
  //The points the edges are based off of
  public ArrayList<Point> sites;
  //The edges generated
  public ArrayList<Edge> edges;
  //The beachline of arcs based on their y-coordinate
  private Arc beachline;
  //The queue of events based on y-coordinate, ascending
  private PriorityQueue<Event> events;
  //The current y-coordinate of the sweepline
  private double sweepline;

  /**
   * Creates a new Voronoi generator, then generates the edges.
   *
   * @param width  the width of the diagram
   * @param height the height of the diagram
   * @param sites  the points to base the diagram off of- must be within the given diagram
   *               dimensions.
   */
  public Voronoi(int width, int height, ArrayList<Point> sites) {
    this.width = width;
    this.height = height;
    this.sites = sites;

    //Initialize the rest of the fields and generate the diagram
    this.resetDiagram();
  }

  /**
   * Regenerates the diagram's edges using the current sites. Due to the nature of Fortune's
   * Algorithm, the entire diagram must be regenerated whenever the sites change.
   */
  private void resetDiagram() {
    this.events = new PriorityQueue<>();
    for (Point p : sites) {
      events.add(new Event(p));
    }
    this.edges = new ArrayList<>();
    this.beachline = null;
    this.sweepline = 0;
    this.generateEdges();
  }

  //******************************CHANGING POINTS***************************************//

  /**
   * Adds a new random site and regenerates the diagram.
   */
  public void addSite() {
    Random r = new Random();
    double x = r.nextDouble() * width;
    double y = r.nextDouble() * height;
    this.addSite(x, y);
  }

  /**
   * Adds the given site and regenerates the diagram.
   *
   * @param x The site's x coordinate.
   * @param y The site's y coordinate.
   * @throws IllegalArgumentException if the site's coordinates are out of bounds.
   */
  public void addSite(double x, double y) throws IllegalArgumentException {
    if (!Util.inRangeEx(0, x, this.width)) {
      throw new IllegalArgumentException("X coordinate of site out of bounds!");
    }
    if (!Util.inRangeEx(0, y, this.height)) {
      throw new IllegalArgumentException("Y coordinate of site out of bounds!");
    }
    //Add the new point if it doesn't already exist, then regenerate the diagram
    Point p = new Point(x, y);
    if (!sites.contains(p)) {
      this.sites.add(new Point(x, y));
      this.resetDiagram();
    }
  }

  /**
   * Deletes a random site and regenerates the diagram.
   */
  public void delSite() {
    Random r = new Random();
    this.sites.remove(r.nextInt(sites.size()));
    this.resetDiagram();
  }

  //******************************MAIN GENERATION***************************************//

  /**
   * Generates the edges of the diagram using Fortune's Algorithm
   *
   * @return The edges.
   */
  private ArrayList<Edge> generateEdges() {
    //Process events until the queue is empty, when there are no sites left
    while (!events.isEmpty()) {
      //Get next event
      Event next = events.poll();
      //Update the sweepline by moving it down
      sweepline = next.site.y;
      //process the circle or site event, respectively
      if (next.type == Event.Type.SITE) {
        this.handleSite(next.site);
      } else {
        if (next.isValid) {
          this.handleCircle(next);
        }
      }
    }
    //All events have been processed.

    //Clean up infinite edges.
    sweepline = width + height;
    this.finishEdges();
    this.trimEdges();
    return this.edges;
  }

  /**
   * Fix the end of all edges on this arc and its descendants
   */
  private void finishEdges() {
    this.finishEdges(beachline);
  }

  /**
   * Fix the end of edges on this arc and its descendants
   * @param arc the arc to be cleaned
   */
  private void finishEdges(Arc arc) {
    if (arc.onBeach) return;

    double x = getXofEdge(arc);
    arc.edge.end = new Point(x, arc.edge.slope * x + arc.edge.yAtZero);
    edges.add(arc.edge);

    finishEdges(arc.leftChild);
    finishEdges(arc.rightChild);
  }

  /**
   * Fixes the edges that are out of bounds
   */
  private void trimEdges() {
    ArrayList<Edge> newEdges = new ArrayList<>();
    for (Edge e : edges) {
      if (!Util.inRangeIn(0, e.start.x, width) || Util.inRangeIn(0, e.start.y, height)) {
        e.start = new Point(e.start.x, e.slope * e.start.x + e.yAtZero);
      }
      if(!Util.inRangeIn(0, e.end.x, width) || Util.inRangeIn(0, e.end.y, height)) {
        e.end = new Point(e.end.x, e.slope * e.end.x + e.yAtZero);
      }
      newEdges.add(e);
    }

    edges = newEdges;
  }


  //******************************HANDLING EVENTS***************************************//

  /**
   * Handles a site event. Updates the edges and beachline as needed, then checks for new circle
   * events.
   *
   * @param site The site that is the source of the event
   */
  public void handleSite(Point site) {

    //Add the very first arc if the beachline is empty
    if (beachline == null) {
      beachline = new Arc(site);
      return;
    }


    //Find the arc directly above the new site.
    Arc above = this.findArcAbove(site.x);


    //Remove the arc's associated circle event, since the arc is being split/destroyed
    if (above.disappearance != null) {
      above.disappearance.isValid = false;
      above.disappearance = null;
    }

    //Start a new edge at the point on the beachline directly above the new site
    Point start = new Point(site.x, getYAtX(above.focus, site.x));
    //Create the new edge and its neighbor.
    Edge left = new Edge(start, above.focus, site);
    Edge right = new Edge(start, site, above.focus);
    left.setNeighbor(right);
    above.toEdge(left);

    /*Add the new arcs.
     //p0 and p2 are the left and right arc sides (split at the new site's y) of the old arc.
     //p1 is the new arc.
     //Both edges are the edge between the old arc's site and the new site.
     //The original arc is replaced with the following structure:
                  (edge)
                 /      \
              (P0)    (edge)
            /    \
          (P1)   (P2)
    */

    Arc p0 = new Arc(above.focus);
    Arc p1 = new Arc(site);
    Arc p2 = new Arc(above.focus);

    above.setLeftChild(p0);
    above.setRightChild(new Arc(right));
    above.rightChild.setLeftChild(p1);
    above.rightChild.setRightChild(p2);

    //Check for circle events
    checkForCircle(p0);
    checkForCircle(p2);
  }

  /**
   * Handles a circle event. Circle events, if valid, cause the arc they were spawned from to
   * disappear.
   */
  private void handleCircle(Event event) {
    //the arc that will disappear with this event
    Arc midArc = event.arc;
    //find the left/right parents
    Arc leftParent = midArc.getLeftParent();
    Arc rightParent = midArc.getRightParent();
    //find the left parent's left child & the right parent's right child
    Arc leftArc = leftParent.getLeftChild();
    Arc rightArc = rightParent.getRightChild();

    //remove the arcs' circle events since the disappearing arc would have been needed for them
    //to happen
    if (leftArc.disappearance != null) {
      leftArc.disappearance.isValid = false;
      leftArc.disappearance = null;
    }
    if (rightArc.disappearance != null) {
      rightArc.disappearance.isValid = false;
      rightArc.disappearance = null;
    }

    //Create a new vertex at the point on the beachline directly above the circle's center
    Point vertex = new Point(event.site.x, getYAtX(midArc.focus, event.site.x));

    //End the parent's edges at this vertex and add the edges
    leftParent.edge.end = vertex;
    rightParent.edge.end = vertex;
    edges.add(leftParent.edge);
    edges.add(rightParent.edge);

    //Start a new edge at this vertex and connect it to the higher parent's edge
    //The edge goes between the left and right arcs
    Arc higher = null;
    Arc curr = midArc;
    boolean foundParent = false;
    //the first found parent is the lower one
    while (curr != beachline && !foundParent) {
      curr = curr.parent;
      if (curr == leftParent) {
        higher = rightParent;
        foundParent = true;
      } else if (curr == rightParent) {
        higher = leftParent;
        foundParent = true;
      }
    }
    higher.edge = new Edge(vertex, leftArc.focus, rightArc.focus);

    //Delete the disappearing arc and its parent.
    Arc gParent = midArc.parent.parent;
    Arc goodChild = midArc.parent.leftChild;
    if (goodChild == midArc) {
      goodChild = midArc.parent.rightChild;
    }
    if (gParent.leftChild == midArc.parent) {
      gParent.setLeftChild(goodChild);
    } else {
      gParent.setRightChild(goodChild);
    }

    //Check if the circle events that were removed have been replaced with new circle events.
    checkForCircle(leftArc);
    checkForCircle(rightArc);
  }

  //******************************HELPERS FOR EVENT HANDLING*************************************//


  /**
   * Checks for a circle event. If there is a valid circle event for this arc, adds it to the event
   * queue. A circle event is valid if the midArc has a distinct left and right arcs with with it
   * forms a circle of positive area.
   *
   * @param midArc the arc that will disappear in the circle event
   */
  private void checkForCircle(Arc midArc) {
    //Find the arc's left and right parents
    Arc leftParent = midArc.getLeftParent();
    Arc rightParent = midArc.getRightParent();
    //Quit if both parents aren't present
    if (leftParent == null || rightParent == null) return;

    //Find the arc's left parent's left child and right parent's right child
    Arc leftArc = leftParent.getLeftChild();
    Arc rightArc = rightParent.getRightChild();
    //Quit if either of the children don't exist (since the given arc along with these two arcs
    // make up the circle event) or if the children are the same, since 3 distinct arcs are needed
    if (leftArc == null || rightArc == null || leftArc.focus == rightArc.focus) return;

    //don't create a circle event if the circle is invalid (not a positive area)
    if (circleArea(leftArc.focus, midArc.focus, rightArc.focus) != 1) return;

    //find the intersection of the left parent's edge with the right parent's, which will
    //be the center of the circle and where the edges will end
    Point center = leftParent.edge.intersectionWith(rightParent.edge);
    if (center == null) return;

    double dx = midArc.focus.x - center.x;
    double dy = midArc.focus.y - center.y;
    double d = Math.sqrt((dx * dx) + (dy * dy)); //The radius of the circle.
    //The top of the circle must have passed. (must be below sweepline)
    if (center.y + d < sweepline) {
      return;
    }

    //Find the bottom of the circle
    Point bottom = new Point(center.x, (center.y + d));

    //Add the bottom of the circle as a circle event
    //When this point has been reached, the intersection of the left and right parabolas will
    //be at the center of the circle and the edges can be finished
    events.add(new Event(bottom, midArc));
  }


  /**
   * Checks if the area of the circle defined by three points is positive, negative
   * or zero
   * @param a any distinct point on the circle
   * @param b any distinct point on the circle
   * @param c any distinct point on the circle
   * @return whether the are is positive (1), negative (-1) or 0 (0)
   */
  public int circleArea(Point a, Point b, Point c) {
    double area = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    if (area < 0) return -1;
    else if (area == 0) return 0;
    return 1; //valid area
  }

  /**
   * Finds the y-coordinate of the point on the parabola with the given focus at
   * the given x-coordinate.
   * Uses the following formula: (x-h)^2 = 4p(y-k) => y = ((x-h)^2 + k) / 4p
   *    (h, k) is the vertex of the parabola
   *     p is the distance between the vertex and the focus
   *    (x, y) is a point on the parabola
   * @param focus the focus of the parabola
   * @param x     the x-coordinate of the point
   * @return The y-coordinate corresponding to the focus and x-coordinate
   */
  private double getYAtX(Point focus, double x) {
    double h = focus.x;
    double k = (focus.y + sweepline) / 2;
    double p = focus.y - k;

    return ((x - h) * (x - h) + 4 * p * k) / 4 / p;
  }

  /**
   * Finds the arc on the beachline that is directly above the given x-coordinate.
   * @param x the x-coordinate
   * @return  the arc above the x-coordinate
   */
  private Arc findArcAbove(double x) {
    Arc curr = this.beachline;
    //Continue going deeper into the tree based on x until we find an arc on the beachline
    while (!curr.onBeach) {
      if (getXofEdge(curr) > x) {
        curr = curr.leftChild;
      } else {
        curr = curr.rightChild;
      }
    }
    return curr;
  }

  /**
   * Finds the x-coordinate of the point where the arc's children intersect
   * @param arc the arc whose children will be used
   * @return the x-coordinate
   */
  private double getXofEdge(Arc arc) {
    //Get the left and right children and their focuses
    Arc left = arc.getLeftChild();
    Arc right = arc.getRightChild();
    Point lFocus = left.focus;
    Point rFocus = right.focus;

    //Use math to find intersection

    //Fill out equation (x − focusX)^2 + focusY^2 − directix^2 = 2(focusX − directix)y for
    // left and right
    double lDif = 2 * (lFocus.y - sweepline);
    double la = 1 / lDif;
    double lb = -2 * lFocus.x / lDif;
    double lc = (Math.pow(lFocus.x, 2) + Math.pow(lFocus.y, 2) - Math.pow(sweepline, 2)) / lDif;

    double rDif = 2 * (rFocus.y - sweepline);
    double ra = 1 / rDif;
    double rb = -2 * rFocus.x / rDif;
    double rc = (Math.pow(rFocus.x, 2) + Math.pow(rFocus.y, 2) - Math.pow(sweepline, 2)) / rDif;

    //Use quadratic formula (-b +/- sqr(b^2 - 4ac)) / 2a
    double a = la - ra;
    double b = lb - rb;
    double c = lc - rc;

    double discriminant = b * b - 4 * a * c;
    double x1 = (-b + Math.sqrt(discriminant)) / (2 * a);
    double x2 = (-b - Math.sqrt(discriminant)) / (2 * a);

    if (lFocus.y > rFocus.y)  return Math.max(x1, x2);
    return Math.min(x1, x2);
  }

}
