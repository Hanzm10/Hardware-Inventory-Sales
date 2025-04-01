package com.murico.app.view.utilities;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
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

  public static Color RgbColorToColorWithAlpha(Color color, int alpha) {
    return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
  }

  public static void setCursorToHandCursor(JComponent component) {
    component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public static void setCursorToDefaultCursor(JComponent component) {
    component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }

  public static void paintBackgroundWithRoundedCornerBorder(Graphics2D g2d, JComponent component,
      RoundedCornerBorder roundedBorder) {
    RenderingUtilities.enableSmoothness(g2d);

    var borderSpace = 1;
    var backgroundSpace = 1;
    var backgroundOffsetPosition = roundedBorder.getBorderOffset() + borderSpace + backgroundSpace;

    var borderWidth = roundedBorder.getBorderWidth();
    var borderX = roundedBorder.getDrawBorder() ? borderWidth / 2 + backgroundOffsetPosition / 2
        : borderWidth / 2;
    var borderY = roundedBorder.getDrawBorder() ? borderWidth / 2 + backgroundOffsetPosition / 2
        : borderWidth / 2;
    var borderW = roundedBorder.getDrawBorder()
        ? component.getWidth() - borderSpace - backgroundSpace - roundedBorder.getBorderOffset()
            - borderWidth
        : component.getWidth() - borderWidth;
    var borderH = roundedBorder.getDrawBorder()
        ? component.getHeight() - borderSpace - backgroundSpace - roundedBorder.getBorderOffset()
            - borderWidth
        : component.getHeight() - borderWidth;

    g2d.setColor(component.getBackground());
    g2d.fill(roundedBorder.createBorder(borderX, borderY, borderW, borderH));
    g2d.dispose();
  }

  public static void paintPlaceholderText(Graphics g, MTextFieldInterface textField, Insets insets,
      Font font, int h) {
    g.setColor(textField.getPlaceholderColor());
    g.drawString(textField.getPlaceholderText(), insets.left,
        h / 2 + font.getSize() / 2 - insets.top / 2);
  }
}
