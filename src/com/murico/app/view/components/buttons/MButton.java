package com.murico.app.view.components.buttons;

import com.murico.app.config.AppSettings;
import com.murico.app.controller.ui.buttons.MButtonMouseListener;
import com.murico.app.view.components.base.RoundedComponent;
import com.murico.app.view.components.buttons.variations.MButtonColorVariations;
import com.murico.app.view.components.helper.ComponentHelper;
import java.awt.*;
import javax.swing.*;

public class MButton extends JButton implements MButtonInterface {

  protected final MButtonMouseListener mouseListener;

  protected final ComponentHelper<MButton> componentHelper;

  protected final RoundedComponent roundedComponent;

  protected MButtonColorVariations colorVariation;

  protected boolean hovered;
  protected boolean pressed;

  private Color previousBackground;

  public MButton(String text) {
    super(text);

    this.disableDefaultButtonStyle();
    this.setDefaults();

    this.mouseListener = new MButtonMouseListener(this);
    this.componentHelper = new ComponentHelper<>(this);
    this.roundedComponent = new RoundedComponent();

    this.roundedComponent.setBorderRadius(AppSettings.getInstance().getBaseBorderRadius());

    this.addMouseListener(this.mouseListener);
  }

  private void disableDefaultButtonStyle() {
    setBorderPainted(false);
    setContentAreaFilled(false);
    setFocusPainted(false);
  }

  private void setDefaults() {
    this.setFont(AppSettings.getInstance().getButtonsFont());
  }

  @Override
  public void mouseClicked() {
    this.previousBackground = this.getBackground();
    this.setBackground(
        new Color(
            this.previousBackground.getRed(),
            this.previousBackground.getGreen(),
            this.previousBackground.getBlue(),
            200));
  }

  @Override
  public void mousePressed() {
    this.pressed = true;

    this.previousBackground = this.getBackground();
    this.setBackground(
        new Color(
            this.previousBackground.getRed(),
            this.previousBackground.getGreen(),
            this.previousBackground.getBlue(),
            175));
  }

  @Override
  public void mouseReleased() {
    this.pressed = false;

    if (!this.hovered) {
      this.componentHelper.setCursorToDefault();

      this.previousBackground = this.getBackground();
      this.setBackground(
          new Color(
              this.previousBackground.getRed(),
              this.previousBackground.getGreen(),
              this.previousBackground.getBlue(),
              255));
    }
  }

  @Override
  public void mouseEntered() {
    this.hovered = true;

    if (!this.pressed) {
      this.componentHelper.setCursorToHand();
      this.previousBackground = this.getBackground();
      this.setBackground(
          new Color(
              this.previousBackground.getRed(),
              this.previousBackground.getGreen(),
              this.previousBackground.getBlue(),
              225));
    }
  }

  @Override
  public void mouseExited() {
    this.hovered = false;

    if (!this.pressed) {
      this.componentHelper.setCursorToDefault();

      this.previousBackground = this.getBackground();
      this.setBackground(
          new Color(
              this.previousBackground.getRed(),
              this.previousBackground.getGreen(),
              this.previousBackground.getBlue(),
              255));
    }
  }

  @Override
  public void setBorderRadius(int radius) {
    this.roundedComponent.setBorderRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public void setBorderRadius(int topLeft, int topRight, int bottomLeft, int bottomRight) {
    this.roundedComponent.setBorderRadius(topLeft, topRight, bottomLeft, bottomRight);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderTopLeftRadius() {
    return this.roundedComponent.getBorderTopLeftRadius();
  }

  @Override
  public void setBorderTopLeftRadius(int radius) {
    this.roundedComponent.setBorderTopLeftRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderTopRightRadius() {
    return this.roundedComponent.getBorderTopRightRadius();
  }

  @Override
  public void setBorderTopRightRadius(int radius) {
    this.roundedComponent.setBorderTopRightRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderBottomLeftRadius() {
    return this.roundedComponent.getBorderBottomLeftRadius();
  }

  @Override
  public void setBorderBottomLeftRadius(int radius) {
    this.roundedComponent.setBorderBottomLeftRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderBottomRightRadius() {
    return this.roundedComponent.getBorderBottomRightRadius();
  }

  @Override
  public void setBorderBottomRightRadius(int radius) {
    this.roundedComponent.setBorderBottomRightRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public MButtonColorVariations getColorVariation() {
    return this.colorVariation;
  }

  @Override
  protected void paintComponent(Graphics g) {
    this.roundedComponent.paintComponent(g, this.getSize(), this.getBackground());
    super.paintComponent(g);
  }
}
