package view;

import java.awt.event.ActionListener;

import controller.VoronoiController;
import voronoidiagram.Voronoi;

/**
 * Represents the full view of the Voronoi diagram, including the control panel and diagram
 */
public class VoronoiView {
  private VoronoiController listener;
  private VoronoiFrame frame;
  private Voronoi diagram;

  public VoronoiView(Voronoi diagram) {
    this.diagram = diagram;
  }

  public void setListener(VoronoiController al) {
    this.listener = al;
  }

  public void display() {
    frame = new VoronoiFrame(diagram, listener);
  }

  public void refresh() {
    this.frame.repaint();
  }

}
