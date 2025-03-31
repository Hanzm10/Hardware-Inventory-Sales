package com.murico.app.view.utilities;

import java.awt.Graphics2D;

/**
 * RenderingUtilities is a utility class that provides methods for enabling smooth rendering
 * settings for Graphics2D objects. It is used to improve the visual quality of rendered graphics.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class RenderingUtilities {
  /**
   * Enables smooth rendering for the given Graphics2D object.
   * 
   * @param g2 the Graphics2D object to enable smooth rendering for
   */
  public static void enableSmoothness(Graphics2D g2) {
    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
        java.awt.RenderingHints.VALUE_RENDER_QUALITY);
    g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
        java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION,
        java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
  }

  /**
   * Disables smooth rendering for the given Graphics2D object.
   * 
   * @param g2 the Graphics2D object to disable smooth rendering for
   */
  public static void disableSmoothness(Graphics2D g2) {
    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
        java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
    g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
        java.awt.RenderingHints.VALUE_RENDER_DEFAULT);
    g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
        java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION,
        java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
  }

}
