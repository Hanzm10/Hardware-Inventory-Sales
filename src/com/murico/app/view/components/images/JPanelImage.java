package com.murico.app.view.components.images;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelImage extends JPanel {

  private static final long serialVersionUID = 1L;

  private ImageIcon image;

  public JPanelImage(ImageIcon image) {
    super();

    this.image = image;

    setOpaque(false);
  }

  @Override
  public void paintComponent(java.awt.Graphics g) {
    super.paintComponent(g);
    g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
  }

  @Override
  public java.awt.Dimension getPreferredSize() {
    if (image != null) {
      return new Dimension(image.getIconWidth(), image.getIconHeight());
    } else {
      return super.getPreferredSize();
    }
  }
}
