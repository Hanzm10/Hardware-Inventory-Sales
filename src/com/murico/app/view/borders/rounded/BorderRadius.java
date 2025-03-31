package com.murico.app.view.borders.rounded;

import java.io.Serializable;

/**
 * BorderRadius is a record that defines the border radius for components with rounded corners. It
 * is used to ensure that components can have their border radius set in a consistent manner.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 * 
 * @param topLeftRadius the radius for the top left corner
 * @param topRightRadius the radius for the top right corner
 * @param bottomLeftRadius the radius for the bottom left corner
 * @param bottomRightRadius the radius for the bottom right corner
 * 
 * @throws AssertionError if any radius is negative
 */
public record BorderRadius(int topLeftRadius, int topRightRadius, int bottomLeftRadius,
    int bottomRightRadius) implements Serializable {

  public BorderRadius {
    assert topLeftRadius >= 0 : "Top left radius must be non-negative";
    assert topRightRadius >= 0 : "Top right radius must be non-negative";
    assert bottomLeftRadius >= 0 : "Bottom left radius must be non-negative";
    assert bottomRightRadius >= 0 : "Bottom right radius must be non-negative";
  }

  /**
   * Checks if all corners have the same radius.
   * 
   * @return true if all corners have the same radius, false otherwise
   */
  public boolean uniform() {
    return topLeftRadius == topRightRadius && topLeftRadius == bottomLeftRadius
        && topLeftRadius == bottomRightRadius;
  }

  /**
   * Calculates the total radius by summing the radii of all corners.
   * 
   * @return the total radius
   */
  public int totalRadius() {
    return topLeftRadius + topRightRadius + bottomLeftRadius + bottomRightRadius;
  }

  /**
   * Creates a new BorderRadius with the same radius for all corners.
   * 
   * @param radius the radius for all corners
   * @return a new BorderRadius with the same radius for all corners
   * 
   * @throws AssertionError if radius is negative
   */
  public static BorderRadius all(int radius) throws AssertionError {
    return new BorderRadius(radius, radius, radius, radius);
  }

  /**
   * Creates a new BorderRadius with the same radius for the top left and bottom right corners, and
   * the same radius for the top right and bottom left corners.
   * 
   * @param horizontalRadius the radius for the top left and bottom right corners
   * @param verticalRadius the radius for the top right and bottom left corners
   * @return a new BorderRadius with the specified horizontal and vertical radii
   * 
   * @throws AssertionError if any radius is negative
   */
  public static BorderRadius horizontal(int horizontalRadius, int verticalRadius)
      throws AssertionError {
    return new BorderRadius(horizontalRadius, verticalRadius, horizontalRadius, verticalRadius);
  }

  /**
   * Creates a new BorderRadius with the same radius for the top left and top right corners, and the
   * same radius for the bottom left and bottom right corners.
   * 
   * @param verticalRadius the radius for the top left and top right corners
   * @param horizontalRadius the radius for the bottom left and bottom right corners
   * @return a new BorderRadius with the specified vertical and horizontal radii
   * 
   * @throws AssertionError if any radius is negative
   */
  public static BorderRadius vertical(int verticalRadius, int horizontalRadius)
      throws AssertionError {
    return new BorderRadius(verticalRadius, verticalRadius, horizontalRadius, horizontalRadius);
  }

  boolean isValid() {
    return topLeftRadius >= 0 && topRightRadius >= 0 && bottomLeftRadius >= 0
        && bottomRightRadius >= 0;
  }

  @Override
  public String toString() {
    return "BorderRadius [topLeftRadius=" + topLeftRadius + ", topRightRadius=" + topRightRadius
        + ", bottomLeftRadius=" + bottomLeftRadius + ", bottomRightRadius=" + bottomRightRadius
        + "]";
  }
}
