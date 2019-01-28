package view;

import java.awt.*;
import java.awt.event.ActionListener;


import javax.swing.*;

import controller.VoronoiController;
import voronoidiagram.Voronoi;

/**
 * Represents a simple view of the Voronoi diagram and controls
 */
public class VoronoiFrame extends JFrame {
  private VoronoiDrawer diagramComponent;
  private VoronoiControls controlPanel;

  /**
   * Generates a new VoronoiFrame
   * @param diagram the Voronoi diagram, which keeps track of the points and edges
   * @param al the Voronoi controller, which handles user input
   */
  public VoronoiFrame(Voronoi diagram, VoronoiController al) {
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    this.diagramComponent = new VoronoiDrawer(diagram);
    this.controlPanel = new VoronoiControls(al);
    this.add(controlPanel, BorderLayout.NORTH);
    this.add(diagramComponent, BorderLayout.CENTER);
    this.diagramComponent.addMouseListener(al);
    this.setVisible(true);
    this.pack();
  }

}
