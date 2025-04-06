package com.murico.app.view.components.buttons;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JToggleButton;
import com.murico.app.config.UISettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;
import com.murico.app.view.components.buttons.listeners.ButtonFocusListenerVisualFeedback;
import com.murico.app.view.components.buttons.listeners.ButtonMouseListenerVisualFeedback;
import com.murico.app.view.components.buttons.variations.MButtonColorVariations;
import com.murico.app.view.utilities.RenderingUtilities;

public class MToggleButton extends JToggleButton implements MButtonInterface,
    RoundedCornerBorderComponentInterface {

  private static final long serialVersionUID = 3411237617423863266L;

  protected MButtonColorVariations colorVariation = MButtonColorVariations.TRANSPARENT;

  protected boolean hovered;
  protected boolean pressed;

  public MToggleButton() {
    this("T");
  }

  public MToggleButton(String text) {
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

    setBackground(UISettings.getInstance().getUIColor().getTransparentColor());
    setRolloverEnabled(false);
  }

  /** === MButtonInterface === */

  @Override
  public MButtonColorVariations getColorVariation() {
    return colorVariation;
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
