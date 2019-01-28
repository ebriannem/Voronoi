package view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

/**
 * Represents a simple control panel for generating Voronoi diagrams
 */
public class VoronoiControls extends JPanel {
  public VoronoiControls(ActionListener al) {
    this.setLayout(new FlowLayout());

    //Button to add points
    JButton addSiteButton = new JButton("+");
    addSiteButton.addActionListener(al);
    addSiteButton.setActionCommand("add_site_button");
    this.add(addSiteButton);

    //Button to remove points
    JButton delSiteButton = new JButton("-");
    delSiteButton.addActionListener(al);
    delSiteButton.setActionCommand("del_site_button");
    this.add(delSiteButton);
  }
}
