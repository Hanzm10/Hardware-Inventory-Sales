package com.murico.app.view.components.buttons;

import com.murico.app.view.components.buttons.variations.MButtonColorVariations;

/**
 * MButtonInterface is an interface that defines the basic properties and methods for a button
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public interface MButtonInterface {
  /**
   * The alpha value for the transparent state of the button.
   */
  public static final int ALPHA_TRANSPARENT = 0;

  /**
   * The alpha value for the solid state of the button.
   */
  public static final int ALPHA_SOLID = 255;

  /**
   * The alpha value for the pressed state of the button when transparent.
   */
  public static final int ALPHA_PRESSED_TRANSPARENT = 50;

  /**
   * The alpha value for the pressed state of the button when solid.
   */
  public static final int ALPHA_PRESSED_SOLID = 200;

  /**
   * The alpha value for the hovered state of the button when transparent.
   */
  public static final int ALPHA_HOVERED_TRANSPARENT = 75;

  /**
   * The alpha value for the hovered state of the button when solid.
   */
  public static final int ALPHA_HOVERED_SOLID = 225;

  /**
   * The alpha value for the pressed state of the button when toggled.
   */
  public static final int ALPHA_PRESSED_TOGGLED = 125;

  /**
   * The alpha value for the pressed state of the button when toggled and hovered.
   */
  public static final int ALPHA_PRESSED_TOGGLED_HOVERED = 150;

  /**
   * The alpha value for the pressed state of the button when not toggled.
   */
  public static final int ALPHA_PRESSED_NOT_TOGGLED = 75;

  /**
   * The alpha value for toggled state of the button.
   */
  public static final int ALPHA_TOGGLED = 100;

  /**
   * The alpha value for the untoggled state of the button.
   */
  public static final int ALPHA_NOT_TOGGLED = 0;

  /**
   * The alpha value for the toggled state of the button when hovered.
   */
  public static final int ALPHA_TOGGLED_HOVERED = 175;

  /**
   * The alpha value for the untoggled state of the button when hovered.
   */
  public static final int ALPHA_NOT_TOGGLED_HOVERED = 100;

  MButtonColorVariations getColorVariation();

  boolean isHovered();

  /**
   * Sets the hovered state of the button. Purely changes the property of the button, does not
   * change the visual state.
   * 
   * @param hovered true if the button is hovered, false otherwise
   */
  void setHovered(boolean hovered);

  boolean isPressed();

  /**
   * Sets the pressed state of the button. Purely changes the property of the button, does not
   * change the visual state.
   * 
   * @param pressed true if the button is pressed, false otherwise
   */
  void setPressed(boolean pressed);
}
