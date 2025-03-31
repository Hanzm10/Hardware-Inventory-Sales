package com.murico.app.view.borders.rounded;

import java.awt.Color;

/**
 * RoundedCornerBorderComponentInterface is an interface that defines methods for setting border
 * radius for components with rounded corners. It is used to ensure that components can have their
 * border radius set in a consistent manner.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public interface RoundedCornerBorderComponentInterface {
  void repaintBorder();

  /**
   * Returns the border of the component.
   * 
   * @return the border of the component
   * 
   * @throws AssertionError if the border is not {@link RoundedCornerBorder}
   */
  RoundedCornerBorder getRoundedCornerBorder() throws AssertionError;

  default BorderRadius getBorderRadius() throws AssertionError {
    return this.getRoundedCornerBorder().getBorderRadius();
  };

  /**
   * Sets border radius for all corners of the component.
   * 
   * @param radius the radius for all corners
   * @throws AssertionError if the radius is negative or border is not {@link RoundedCornerBorder}
   */
  default void setBorderRadius(int radius) throws AssertionError {
    this.getRoundedCornerBorder().setBorderRadius(BorderRadius.all(radius));
    this.repaintBorder();
  }

  /**
   * Sets border radius for all corners of the component.
   * 
   * @param radius the radius for all corners
   * @throws AssertionError if the radius is negative or border is not {@link RoundedCornerBorder}
   */

  default void setBorderRadius(BorderRadius radius) throws AssertionError {
    assert radius != null : "Border radius cannot be null";
    assert radius.isValid() : "Border radius cannot contain negative values";

    this.getRoundedCornerBorder().setBorderRadius(radius);
    this.repaintBorder();
  }

  /**
   * Sets border radius for each corner of the component.
   * 
   * @param topLeft the radius for the top left corner
   * @param topRight the radius for the top right corner
   * @param bottomLeft the radius for the bottom left corner
   * @param bottomRight the radius for the bottom right corner
   * 
   * @throws AssertionError if the radius is negative or border is not {@link RoundedCornerBorder}
   */
  default void setBorderRadius(int topLeft, int topRight, int bottomLeft, int bottomRight)
      throws AssertionError {
    this.getRoundedCornerBorder()
        .setBorderRadius(new BorderRadius(topLeft, topRight, bottomLeft, bottomRight));
    this.repaintBorder();
  }

  default int getBorderTopLeftRadius() {
    return this.getRoundedCornerBorder().getBorderRadius().topLeftRadius();
  }

  /**
   * Sets the border radius for the top left corner of the component.
   * 
   * @param radius the radius for the top left corner
   * @throws AssertionError if the radius is negative or border is not {@link RoundedCornerBorder}
   */
  default void setBorderTopLeftRadius(int radius) throws AssertionError {
    this.getRoundedCornerBorder()
        .setBorderRadius(new BorderRadius(radius, this.getBorderTopRightRadius(),
            this.getBorderBottomLeftRadius(), this.getBorderBottomRightRadius()));

    this.repaintBorder();
  }

  default int getBorderTopRightRadius() {
    return this.getRoundedCornerBorder().getBorderRadius().topRightRadius();
  }

  /**
   * Sets the border radius for the top right corner of the component.
   * 
   * @param radius the radius for the top right corner
   * @throws AssertionError if the radius is negative or border is not {@link RoundedCornerBorder}
   */
  default void setBorderTopRightRadius(int radius) throws AssertionError {
    this.getRoundedCornerBorder().setBorderRadius(new BorderRadius(this.getBorderTopLeftRadius(),
        radius, this.getBorderBottomLeftRadius(), this.getBorderBottomRightRadius()));

    this.repaintBorder();
  };

  default int getBorderBottomLeftRadius() {
    return this.getRoundedCornerBorder().getBorderRadius().bottomLeftRadius();
  }

  /**
   * Sets the border radius for the bottom left corner of the component.
   * 
   * @param radius the radius for the bottom left corner
   * @throws AssertionError if the radius is negative or border is not {@link RoundedCornerBorder}
   */
  default void setBorderBottomLeftRadius(int radius) throws AssertionError {
    this.getRoundedCornerBorder().setBorderRadius(new BorderRadius(this.getBorderTopLeftRadius(),
        this.getBorderTopRightRadius(), radius, this.getBorderBottomRightRadius()));

    this.repaintBorder();
  }

  default int getBorderBottomRightRadius() {
    return this.getRoundedCornerBorder().getBorderRadius().bottomRightRadius();
  }

  /**
   * Sets the border radius for the bottom right corner of the component.
   * 
   * @param radius the radius for the bottom right corner
   * @throws AssertionError if the radius is negative or border is not {@link RoundedCornerBorder}
   */
  default void setBorderBottomRightRadius(int radius) throws AssertionError {
    this.getRoundedCornerBorder().setBorderRadius(new BorderRadius(this.getBorderTopLeftRadius(),
        this.getBorderTopRightRadius(), this.getBorderBottomLeftRadius(), radius));

    this.repaintBorder();
  }

  default void setBorderColor(Color color) {
    this.getRoundedCornerBorder().setBorderColor(color);
    this.repaintBorder();
  }

  default Color getBorderColor() {
    return this.getRoundedCornerBorder().getBorderColor();
  }
}
