package com.murico.app.view.components.caret;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.text.DefaultCaret;

public class SimpleCaret extends DefaultCaret {
  /**
   * 
   */
  private static final long serialVersionUID = -757998903709813785L;

  @Override
  protected synchronized void damage(Rectangle r) {
    if (r == null) {
      return;
    }

    // Call the superclass method to handle the default behavior
    super.damage(r);

    x = r.x;
    y = r.y;
    width = r.width + 1; // Add 1 pixel to the width for the caret
    height = r.height;
    repaint();
  }

  @Override
  public void paint(Graphics g) {
    // Call the superclass method to handle the default behavior
    super.paint(g);

    if (isVisible()) {
      g.setColor(Color.BLUE);
      g.fillRect(x, y, width, height);
    }
  }
}
