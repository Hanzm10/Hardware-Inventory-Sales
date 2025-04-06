package com.murico.app.view.components.buttons;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import com.murico.app.config.UISettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;
import com.murico.app.view.components.buttons.listeners.ButtonFocusListenerVisualFeedback;
import com.murico.app.view.components.buttons.listeners.ButtonMouseListenerVisualFeedback;
import com.murico.app.view.components.buttons.variations.MButtonColorVariations;
import com.murico.app.view.utilities.RenderingUtilities;

/**
 * MButton is a custom button class that extends JButton and implements MButtonInterface and
 * MouseListener. It provides a rounded button with color variations and hover/press effects.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class MButton extends JButton implements MButtonInterface,
    RoundedCornerBorderComponentInterface {

  /**
   * 
   */
  private static final long serialVersionUID = -5038858231143921204L;

  protected MButtonColorVariations colorVariation;

  protected boolean hovered;
  protected boolean pressed;

  public MButton(String text) {
    super(text);

    disableDefaultButtonStyle();
    setDefaults();

    addMouseListener(new ButtonMouseListenerVisualFeedback());
    addFocusListener(new ButtonFocusListenerVisualFeedback());
  }

  private void disableDefaultButtonStyle() {
    setFocusPainted(false);
    setContentAreaFilled(false);
  }

  private void setDefaults() {
    setFont(UISettings.getInstance().getUIFont().getButtonFont());

    setBorder(new RoundedCornerBorder());
  }

  /** === MButtonInterface === */

  @Override
  public MButtonColorVariations getColorVariation() {
    return this.colorVariation;
  }

  @Override
  public boolean isHovered() {
    return hovered;
  }

  @Override
  public void setHovered(boolean hovered) {
    this.hovered = hovered;
  }

  @Override
  public boolean isPressed() {
    return pressed;
  }

  @Override
  public void setPressed(boolean pressed) {
    this.pressed = pressed;
  }

  /** === PaintComponent === */

  @Override
  protected void paintComponent(Graphics g) {
    var b = this.getBorder();

    if (!this.isOpaque() && b instanceof RoundedCornerBorder) {
      RenderingUtilities.paintBackgroundWithRoundedCornerBorder((Graphics2D) g.create(), this,
          (RoundedCornerBorder) b);
    }

    super.paintComponent(g);
  }

}
