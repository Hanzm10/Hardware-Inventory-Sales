package com.murico.app.view.borders.rounded;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import javax.swing.border.AbstractBorder;
import com.murico.app.config.AppSettings;
import com.murico.app.view.utilities.RenderingUtilities;

/**
 * RoundedCornerBorder is a class that defines a border with rounded corners. It is used to create
 * components with rounded edges and provides methods to set the radius for each corner.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class RoundedCornerBorder extends AbstractBorder implements Serializable {

  private static final long serialVersionUID = -9015918372669228685L;

  protected Color borderColor;

  protected int borderWidth;
  protected int borderOffset;

  protected boolean drawBorder;
  protected boolean drawBackground;

  protected BorderRadius borderRadius;

  public RoundedCornerBorder() {
    this.setDefaults();
  }

  public RoundedCornerBorder(boolean drawBorder) {
    this.setDefaults();

    this.drawBorder = drawBorder;
  }

  public boolean getDrawBorder() {
    return drawBorder;
  }

  public void setDrawBorder(boolean drawBorder) {
    this.drawBorder = drawBorder;
  }

  public int getBorderWidth() {
    return borderWidth;
  }

  /**
   * Sets the border width. The border width is the thickness of the border around the component.
   * 
   * @param borderWidth
   * 
   * @throws AssertionError if the border width is less than 0
   */
  public void setBorderWidth(int borderWidth) throws AssertionError {
    assert borderWidth >= 0 : "Border width must be greater than or equal to 0";

    this.borderWidth = borderWidth;
  }

  public Color getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }

  public BorderRadius getBorderRadius() {
    return borderRadius;
  }

  /**
   * Sets the border radius. The border radius is the radius of the rounded corners of the border.
   * 
   * @param borderRadius
   * 
   * @throws AssertionError if the border radius is null or contains negative values
   * 
   * @see BorderRadius
   */
  public void setBorderRadius(BorderRadius borderRadius) throws AssertionError {
    assert borderRadius != null : "Border radius cannot be null";
    assert borderRadius.isValid() : "Border radius cannot contain negative values";

    this.borderRadius = borderRadius;
  }

  public int getBorderOffset() {
    return borderOffset;
  }

  /**
   * Sets the border offset. The border offset is the distance between the border and the background
   * 
   * @param borderOffset
   * 
   * @throws AssertionError if the border offset is less than 0
   */
  public void setBorderOffset(int borderOffset) throws AssertionError {
    assert borderOffset >= 0 : "Border offset must be greater than or equal to 0";

    this.borderOffset = borderOffset;
  }

  private void setDefaults() {
    this.borderRadius = BorderRadius.all(AppSettings.getInstance().getBaseBorderRadius());
    this.borderColor = AppSettings.getInstance().getBorderColor();
    this.borderWidth = AppSettings.getInstance().getBaseBorderWidth();
    this.borderOffset = AppSettings.getInstance().getBaseBorderOffset();
    this.drawBorder = false;
  }

  public Area createBorder(double x, double y, double w, double h) {
    var componentArea = new Area(new Rectangle2D.Double(x, y, w, h));
    
    if (this.borderRadius.topLeftRadius() != 0) {
      componentArea.intersect(createBorderTopLeft(x, y, w, h));
    }
    
    if (this.borderRadius.topRightRadius() != 0) {
      componentArea.intersect(createBorderTopRightRadius(x, y, w, h));
    }
    
    if (this.borderRadius.bottomLeftRadius() != 0) {
      componentArea.intersect(createBorderBottomLeftRadius(x, y, w, h));
    }
    
    if (this.borderRadius.bottomRightRadius() != 0) {
      componentArea.intersect(createBorderBottomRightRadius(x, y, w, h));
    }
    
    return componentArea;
  }

  private Area createBorderTopLeft(double x, double y, double w, double h) {
    var borderRadiusInline = Math.min(w, borderRadius.topLeftRadius());
    var borderRadiusBlock = Math.min(h, borderRadius.topLeftRadius());
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
    var borderRadiusInline = Math.min(w, borderRadius.topRightRadius());
    var borderRadiusBlock = Math.min(h, borderRadius.topRightRadius());
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
    var borderRadiusInline = Math.min(w, borderRadius.bottomRightRadius());
    var borderRadiusBlock = Math.min(h, borderRadius.bottomRightRadius());
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
    var borderRadiusInline = Math.min(w, borderRadius.bottomLeftRadius());
    var borderRadiusBlock = Math.min(h, borderRadius.bottomLeftRadius());
    var halfBlock = borderRadiusBlock / 2;

    var componentArea =
        new Area(new RoundRectangle2D.Double(x, y, w, h, borderRadiusInline, borderRadiusBlock));

    var bottomLeftArcX = new Area(new Rectangle2D.Double(halfBlock, y, w - halfBlock, h));
    var bottomLeftArcY = new Area(new Rectangle2D.Double(x, y, w, h - halfBlock));

    componentArea.add(bottomLeftArcX);
    componentArea.add(bottomLeftArcY);

    return componentArea;
  }

  // === Paint ===

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
    super.paintBorder(c, g, x, y, w, h);

    if (!drawBorder) {
      return;
    }

    var g2 = (java.awt.Graphics2D) g.create();

    RenderingUtilities.enableSmoothness(g2);

    double borderX = x + borderWidth / 2;
    double borderY = y + borderWidth / 2;
    double borderW = w - 1 - borderWidth;
    double borderH = h - 1 - borderWidth;

    Shape borderShape = createBorder(borderX, borderY, borderW, borderH);
    var borderEdgeArea = new Area(new Rectangle2D.Double(x, y, w, h));

    borderEdgeArea.intersect(new Area(borderShape));

    g2.setColor(this.borderColor);
    g2.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2.draw(borderShape);

    g2.dispose();
  }

  // === Insets ===

  @Override
  public Insets getBorderInsets(java.awt.Component c) {
    var PADDING_X = 12;
    var PADDING_Y = 4;

    var insetX = borderWidth + PADDING_X;
    var insetY = borderWidth + PADDING_Y;

    return new Insets(insetY, insetX, insetY, insetX);
  }

  @Override
  public Insets getBorderInsets(java.awt.Component c, Insets insets) {
    var PADDING_X = 12;
    var PADDING_Y = 4;

    var insetX = borderWidth + PADDING_X;
    var insetY = borderWidth + PADDING_Y;

    insets.set(insetY, insetX, insetY, insetX);
    return insets;
  }

  // === Object ===
  @Override
  public String toString() {
    return "RoundedCornerBorder [borderColor=" + borderColor + ", borderWidth=" + borderWidth
        + ", drawBorder=" + drawBorder + ", drawBackground=" + drawBackground + ", borderRadius="
        + borderRadius + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var that = (RoundedCornerBorder) obj;
    return borderWidth == that.borderWidth && drawBorder == that.drawBorder
        && drawBackground == that.drawBackground && borderRadius.equals(that.borderRadius)
        && borderColor.equals(that.borderColor);
  }
}

