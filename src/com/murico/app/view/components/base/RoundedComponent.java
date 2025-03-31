package com.murico.app.view.components.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * RoundedComponent is a class that provides methods to create a rounded component with customizable
 * border radius for each corner.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class RoundedComponent {
  private int borderTopLeftRadius;
  private int borderTopRightRadius;
  private int borderBottomLeftRadius;
  private int borderBottomRightRadius;

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

  public void paintComponent(Graphics g, Dimension size, Color background) {
    var g2 = (Graphics2D) g.create();

    enableSmoothness(g2);

    var componentArea = createBorderTopLeftRadius(size);

    componentArea.intersect(createBorderTopRightRadius(size));
    componentArea.intersect(createBorderBottomRightRadius(size));
    componentArea.intersect(createBorderBottomLeftRadius(size));

    g2.setColor(background);
    g2.fill(componentArea);
    g2.dispose();
  }

  private void enableSmoothness(Graphics2D g2) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  }

  private Area createBorderTopLeftRadius(Dimension size) {
    var width = size.width;
    var height = size.height;

    double borderRadiusInline = Math.min(width, borderTopLeftRadius);
    double borderRadiusBlock = Math.min(height, borderTopLeftRadius);
    var halfInline = borderRadiusInline / 2;

    var componentArea =
        new Area(
            new RoundRectangle2D.Double(
                0, 0, width, height, borderRadiusInline, borderRadiusBlock));

    componentArea.add(new Area(new Rectangle2D.Double(halfInline, 0, width - halfInline, height)));
    componentArea.add(new Area(new Rectangle2D.Double(0, halfInline, width, height - halfInline)));

    return componentArea;
  }

  private Area createBorderTopRightRadius(Dimension size) {
    var width = size.width;
    var height = size.height;

    double borderRadiusInline = Math.min(width, borderTopRightRadius);
    double borderRadiusBlock = Math.min(height, borderTopRightRadius);
    var halfInline = borderRadiusInline / 2;

    var componentArea =
        new Area(
            new RoundRectangle2D.Double(
                0, 0, width, height, borderRadiusInline, borderRadiusBlock));

    componentArea.add(new Area(new Rectangle2D.Double(0, 0, width - halfInline, height)));
    componentArea.add(new Area(new Rectangle2D.Double(0, halfInline, width, height - halfInline)));
    return componentArea;
  }

  private Area createBorderBottomRightRadius(Dimension size) {
    var width = size.width;
    var height = size.height;

    double borderRadiusInline = Math.min(width, borderBottomRightRadius);
    double borderRadiusBlock = Math.min(height, borderBottomRightRadius);
    var halfBlock = borderRadiusBlock / 2;

    var componentArea =
        new Area(
            new RoundRectangle2D.Double(
                0, 0, width, height, borderRadiusInline, borderRadiusBlock));

    componentArea.add(new Area(new Rectangle2D.Double(0, 0, width, height - halfBlock)));
    componentArea.add(new Area(new Rectangle2D.Double(0, halfBlock, width - halfBlock, height)));

    return componentArea;
  }

  private Area createBorderBottomLeftRadius(Dimension size) {
    var width = size.width;
    var height = size.height;

    double borderRadiusInline = Math.min(width, borderBottomLeftRadius);
    double borderRadiusBlock = Math.min(height, borderBottomLeftRadius);
    var halfBlock = borderRadiusBlock / 2;

    var componentArea =
        new Area(
            new RoundRectangle2D.Double(
                0, 0, width, height, borderRadiusInline, borderRadiusBlock));

    componentArea.add(new Area(new Rectangle2D.Double(halfBlock, 0, width - halfBlock, height)));
    componentArea.add(new Area(new Rectangle2D.Double(0, 0, width, height - halfBlock)));

    return componentArea;
  }
}
