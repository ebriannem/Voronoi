package view;

import java.awt.*;

import javax.swing.*;

import voronoidiagram.Edge;
import voronoidiagram.Point;
import voronoidiagram.Voronoi;

/**
 * Represents a simple way to view the Voronoi diagram
 */
public class VoronoiDrawer extends JComponent {
  private Voronoi voronoi;

  /**
   * Creates a new Voronoi drawer based on the given Voronoi diagram
   * @param voronoi
   */
  public VoronoiDrawer(Voronoi voronoi) {
    super();
    this.voronoi = voronoi;
    setPreferredSize(new Dimension(voronoi.width, voronoi.height));
  }

  /**
   * Draws the Voronoi diagram with the points and edges
   * @param g the graphics used to draw
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.white);
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.black);
    for (Edge edge : voronoi.edges) {
      g.drawLine(
              (int) edge.start.x,
              (int) edge.start.y,
              (int) edge.end.x,
              (int) edge.end.y
      );
    }
    for (Point point : voronoi.sites) {
      g.drawOval(
              (int) point.x,
              (int) point.y,
              2,
              2
      );
    }

  }

}

