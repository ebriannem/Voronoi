package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import voronoidiagram.Voronoi;
import view.VoronoiView;

public class VoronoiController implements ActionListener, Runnable, MouseListener {
  private VoronoiView view;
  private Voronoi voronoi;

  public VoronoiController(VoronoiView view, Voronoi voronoi) {
    this.view = view;
    this.view.setListener(this);
    this.voronoi = voronoi;
  }

  @Override
  public void run() {
    this.view.display();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()){
      case "add_site_button":
        this.voronoi.addSite();
        break;
      case "del_site_button":
        this.voronoi.delSite();
        break;
      default:
        break;
    }
    this.view.refresh();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.voronoi.addSite(e.getX(), e.getY());
    this.view.refresh();
  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }
}
