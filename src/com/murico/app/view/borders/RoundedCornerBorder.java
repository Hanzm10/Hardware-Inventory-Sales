package com.murico.app.view.borders;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;
import com.murico.app.config.AppSettings;

/**
 * RoundedCornerBorder is a custom border class that extends AbstractBorder to create a rounded
 * corner effect for Swing components. It allows setting different radius for each corner and
 * provides methods to customize the border appearance.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class RoundedCornerBorder extends AbstractBorder {

  /**
   * 
   */
  private static final long serialVersionUID = -7936746255951614535L;

  protected Color borderColor;
  protected int borderWidth;
  protected boolean drawBorder;

  protected int borderTopLeftRadius;
  protected int borderTopRightRadius;
  protected int borderBottomLeftRadius;
  protected int borderBottomRightRadius;

  public RoundedCornerBorder() {
    this.setDefaults();
  }


  public RoundedCornerBorder(Color borderColor) {
    this.setDefaults();

    this.borderColor = borderColor;
  }

  public RoundedCornerBorder(int borderWidth) {
    this.setDefaults();

    this.borderWidth = borderWidth;
  }

  public RoundedCornerBorder(boolean drawBorder) {
    this.setDefaults();

    this.drawBorder = drawBorder;
  }

  public RoundedCornerBorder(Color borderColor, int borderWidth) {
    this.setDefaults();

    this.borderColor = borderColor;
    this.borderWidth = borderWidth;
  }

  public RoundedCornerBorder(Color borderColor, boolean drawBorder) {
    this.setDefaults();

    this.borderColor = borderColor;
    this.drawBorder = drawBorder;
  }

  public RoundedCornerBorder(int borderWidth, boolean drawBorder) {
    this.setDefaults();

    this.borderWidth = borderWidth;
    this.drawBorder = drawBorder;
  }

  public RoundedCornerBorder(Color borderColor, int borderWidth, boolean drawBorder) {
    this.setDefaults();

    this.borderColor = borderColor;
    this.borderWidth = borderWidth;
    this.drawBorder = drawBorder;
  }

  public void setBorderRadius(int radius) {
    this.borderTopLeftRadius = radius;
    this.borderTopRightRadius = radius;
    this.borderBottomLeftRadius = radius;
    this.borderBottomRightRadius = radius;
  }

  public void setBorderRadius(int topLeft, int topRight, int bottomLeft, int bottomRight) {
    this.borderTopLeftRadius = topLeft;
    this.borderTopRightRadius = topRight;
    this.borderBottomLeftRadius = bottomLeft;
    this.borderBottomRightRadius = bottomRight;
  }

  public int getBorderTopLeftRadius() {
    return borderTopLeftRadius;
  }

  public void setBorderTopLeftRadius(int radius) {
    this.borderTopLeftRadius = radius;
  }

  public int getBorderTopRightRadius() {
    return borderTopRightRadius;
  }

  public void setBorderTopRightRadius(int radius) {
    this.borderTopRightRadius = radius;
  }

  public int getBorderBottomLeftRadius() {
    return borderBottomLeftRadius;
  }

  public void setBorderBottomLeftRadius(int radius) {
    this.borderBottomLeftRadius = radius;
  }

  public int getBorderBottomRightRadius() {
    return borderBottomRightRadius;
  }

  public void setBorderBottomRightRadius(int radius) {
    this.borderBottomRightRadius = radius;
  }

  public Color getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }

  public int getBorderWidth() {
    return borderWidth;
  }

  public void setBorderWidth(int borderWidth) {
    this.borderWidth = borderWidth;
  }

  public boolean isDrawBorder() {
    return drawBorder;
  }

  public void setDrawBorder(boolean drawBorder) {
    this.drawBorder = drawBorder;
  }

  private void setDefaults() {
    this.setBorderRadius(AppSettings.getInstance().getBaseBorderRadius());
    this.borderColor = AppSettings.getInstance().getBorderColor();
    this.borderWidth = AppSettings.getInstance().getBaseBorderWidth();
    this.drawBorder = false;
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    var g2 = (Graphics2D) g.create();

    enableSmoothness(g2);

    if (drawBorder) {
      double borderX = x + borderWidth / 2;
      double borderY = y + borderWidth / 2;
      double borderW = width - 1 - borderWidth;
      double borderH = height - 1 - borderWidth;

      Shape borderShape = createBorder(borderX, borderY, borderW, borderH);
      var borderEdgeArea = new Area(new Rectangle2D.Double(x, y, width, height));

      borderEdgeArea.intersect(new Area(borderShape));

      g2.setPaint(c.getBackground());
      g2.fill(borderShape);

      g2.setPaint(this.borderColor);
      g2.setStroke(
          new BasicStroke(borderWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
      g2.draw(borderShape);
    } else {
      Shape borderShape = createBorder(x, y, width - 1, height - 1);

      g2.setPaint(c.getBackground());
      g2.fill(borderShape);
    }

    g2.dispose();

    super.paintBorder(c, g, x, y, width, height);
  }

  private void enableSmoothness(Graphics2D g2) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  }

  private Area createBorder(double x, double y, double w, double h) {
    var componentArea = new Area(new Rectangle2D.Double(x, y, w, h));

    if (borderTopLeftRadius > 0) {
      componentArea.intersect(createBorderTopLeftRadius(x, y, w, h));
    }

    if (borderTopRightRadius > 0) {
      componentArea.intersect(createBorderTopRightRadius(x, y, w, h));
    }

    if (borderBottomLeftRadius > 0) {
      componentArea.intersect(createBorderBottomLeftRadius(x, y, w, h));
    }

    if (borderBottomRightRadius > 0) {
      componentArea.intersect(createBorderBottomRightRadius(x, y, w, h));
    }

    return componentArea;
  }

  private Area createBorderTopLeftRadius(double x, double y, double w, double h) {
    var borderRadiusInline = Math.min(w, borderTopLeftRadius);
    var borderRadiusBlock = Math.min(h, borderTopLeftRadius);
    var halfInline = borderRadiusInline / 2;

    var componentArea =
        new Area(new RoundRectangle2D.Double(x, y, w, h, borderRadiusInline, borderRadiusBlock));

    var topLeftArcX = new Area(new Rectangle2D.Double(halfInline, y, w - halfInline, h));
    var topLeftArcY = new Area(new Rectangle2D.Double(x, halfInline, w, h - halfInline));

    componentArea.add(topLeftArcX);
    componentArea.add(topLeftArcY);

    return componentArea;
  }

  private Area createBorderTopRightRadius(double x, double y, double w, double h) {
    var borderRadiusInline = Math.min(w, borderTopRightRadius);
    var borderRadiusBlock = Math.min(h, borderTopRightRadius);
    var halfInline = borderRadiusInline / 2;

    var componentArea =
        new Area(new RoundRectangle2D.Double(x, y, w, h, borderRadiusInline, borderRadiusBlock));

    var topRightArcX = new Area(new Rectangle2D.Double(x, y, w - halfInline, h));
    var topRightArcY = new Area(new Rectangle2D.Double(x, halfInline, w, h - halfInline));

    componentArea.add(topRightArcX);
    componentArea.add(topRightArcY);

    return componentArea;
  }

  private Area createBorderBottomRightRadius(double x, double y, double w, double h) {
    var borderRadiusInline = Math.min(w, borderBottomRightRadius);
    var borderRadiusBlock = Math.min(h, borderBottomRightRadius);
    var halfBlock = borderRadiusBlock / 2;

    var componentArea =
        new Area(new RoundRectangle2D.Double(x, y, w, h, borderRadiusInline, borderRadiusBlock));

    var bottomRightArcX = new Area(new Rectangle2D.Double(x, y, w, h - halfBlock));
    var bottomRightArcY = new Area(new Rectangle2D.Double(x, halfBlock, w - halfBlock, h));

    componentArea.add(bottomRightArcX);
    componentArea.add(bottomRightArcY);

    return componentArea;
  }

  private Area createBorderBottomLeftRadius(double x, double y, double w, double h) {
    var borderRadiusInline = Math.min(w, borderBottomLeftRadius);
    var borderRadiusBlock = Math.min(h, borderBottomLeftRadius);
    var halfBlock = borderRadiusBlock / 2;

    var componentArea =
        new Area(new RoundRectangle2D.Double(x, y, w, h, borderRadiusInline, borderRadiusBlock));

    var bottomLeftArcX = new Area(new Rectangle2D.Double(halfBlock, y, w - halfBlock, h));
    var bottomLeftArcY = new Area(new Rectangle2D.Double(x, y, w, h - halfBlock));

    componentArea.add(bottomLeftArcX);
    componentArea.add(bottomLeftArcY);

    return componentArea;
  }

  @Override
  public Insets getBorderInsets(Component c) {
    var inset = borderWidth + borderWidth / 2;
    
    if (drawBorder) {
      return new Insets(inset, inset, inset, inset);
    } else {
      return new Insets(0, 0, 0, 0);
    }
  }  @Override
  public Insets getBorderInsets(Component c, Insets insets) {
    var inset = borderWidth + borderWidth / 2;
    
    if (drawBorder) {
      insets.set(inset, inset, inset, inset);
    } else {
      insets.set(0, 0, 0, 0);
    }
    
    return insets;
  }
}
