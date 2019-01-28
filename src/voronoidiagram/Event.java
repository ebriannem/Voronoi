package voronoidiagram;

/**
 * Represents an event used in Fortune's Algorithm
 */
public class Event implements Comparable<Event> {
  final Point site;
  final Arc arc;
  final Type type;
  boolean isValid;

  /**
   * Creates a site event
   * @param site the site of the event
   */
  public Event(Point site) {
    this.site = site;
    this.arc = null;
    this.type = Type.SITE;
    this.isValid = true;
  }

  /**
   * Creates a circle event
   * @param arc the arc that will disappear
   */
  public Event(Point site, Arc arc) {
    this.site = site;
    this.arc = arc;
    arc.disappearance = this;
    this.type = Type.CIRCLE;
    this.isValid = true;
  }

  /**
   * Compares this event to another based on y-coordinate
   * @param e the event to be compared
   * @return the comparison (-1 for less than, 0 for equal, 1 for greater than)
   */
  @Override
  public int compareTo(Event e) {
    return this.site.compareTo(e.site);
  }

  /**
   * The type of the event
   */
  public enum Type {
    SITE,
    CIRCLE
  }
}
