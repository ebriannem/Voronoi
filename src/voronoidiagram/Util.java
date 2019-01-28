package voronoidiagram;

/**
 * A basic utility class
 */
public class Util {
  /**
   * Checks if a number is in a range, exclusive
   * @param min the bottom of the range
   * @param num the number
   * @param max the top of the range
   * @return whether the number is in the range
   */
  public static boolean inRangeEx(double min, double num, double max) {
    return min < num && num < max;
  }
  /**
   * Checks if a number is in a range, inclusive
   * @param min the bottom of the range
   * @param num the number
   * @param max the top of the range
   * @return whether the number is in the range
   */
  public static boolean inRangeIn(double min, double num, double max) {
    return min <= num && num <= max;
  }

  /**
   * Compares two doubles with a margin of 0.0001
   * @param a the first double
   * @param b the second double
   * @return 0 for equality, 1 if a > b, -1 if a < b
   */
  public static int doubleComp(double a, double b) {
    if (Math.abs(a - b) < 0.0001) {
      return 0;
    }
    else if(a < b) {
      return -1;
    }
    return 1;
  }
}