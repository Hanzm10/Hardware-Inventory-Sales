package com.murico.app.view.utilities;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import com.murico.app.config.AppSettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;
import com.murico.app.view.components.inputs.MTextFieldInterface;

/**
 * RenderingUtilities is a utility class that provides methods for {@link Graphics2D} or
 * {@link Graphics} objects. It is used to improve the visual quality of rendered graphics.
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

  public static void paintBackgroundWithRoundedCornerBorder(Graphics2D g2d, JComponent component,
      RoundedCornerBorder roundedBorder) {
    var borderWidth = roundedBorder.getBorderWidth();
    var borderX = borderWidth / 2;
    var borderY = borderWidth / 2;
    var borderW = component.getWidth() - borderWidth;
    var borderH = component.getHeight() - borderWidth;

    g2d.setColor(component.getBackground());
    g2d.fill(roundedBorder.createBorder(borderX, borderY, borderW, borderH));
    g2d.dispose();
  }

  public static void paintPlaceholderText(Graphics g, MTextFieldInterface textField, Insets insets,
      Font font, int h) {
    g.setColor(textField.getPlaceholderColor());
    g.drawString(textField.getPlaceholderText(), insets.left,
        h / 2 + font.getSize() / 2 - insets.top);
  }

  public static void setBorderColorOfComponentWithRoundedCornerBorderOnFocus(
      RoundedCornerBorderComponentInterface r, boolean isFocused) {
    if (isFocused) {
      r.getRoundedCornerBorder().setBorderColor(AppSettings.getInstance().getPrimaryColor());
    } else {
      r.getRoundedCornerBorder().setBorderColor(AppSettings.getInstance().getBorderColor());
    }
  }
}
