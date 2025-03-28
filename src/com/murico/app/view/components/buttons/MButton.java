package com.murico.app.view.components.buttons;

import com.murico.app.config.AppSettings;
import com.murico.app.controller.ui.buttons.MButtonMouseListener;
import com.murico.app.view.components.base.RoundedComponent;
import com.murico.app.view.components.buttons.variations.MButtonColorVariations;
import com.murico.app.view.components.buttons.variations.MButtonSizeVariations;
import com.murico.app.view.components.helper.ComponentHelper;
import java.awt.*;
import javax.swing.*;

public class MButton extends JButton implements MButtonInterface {

  protected final MButtonMouseListener mouseListener;

  protected final ComponentHelper<MButton> componentHelper;

  protected final RoundedComponent roundedComponent;

  protected MButtonColorVariations colorVariation;
  protected MButtonSizeVariations sizeVariation;

  public MButton(String text) {
    super(text);

    this.disableDefaultButtonStyle();
    this.setDefaults();

    this.mouseListener = new MButtonMouseListener(this);
    this.componentHelper = new ComponentHelper<>(this);
    this.roundedComponent = new RoundedComponent();

    this.addMouseListener(this.mouseListener);
  }

  private void disableDefaultButtonStyle() {
    setBorderPainted(false);
    setContentAreaFilled(false);
    setFocusPainted(false);
  }

  private void setDefaults() {
    this.setFont(AppSettings.getInstance().getButtonsFont());

    Dimension size = new Dimension(80, 40);

    this.setPreferredSize(size);
  }

  @Override
  public void mouseClicked() {}

  @Override
  public void mousePressed() {}

  @Override
  public void mouseReleased() {}

  @Override
  public void mouseEntered() {
    this.componentHelper.setCursorToHand();
  }

  @Override
  public void mouseExited() {
    this.componentHelper.setCursorToDefault();
  }

  @Override
  public void setBorderRadius(int radius) {
    this.roundedComponent.setBorderRadius(radius);
  }

  @Override
  public void setBorderRadius(int topLeft, int topRight, int bottomLeft, int bottomRight) {
    this.roundedComponent.setBorderRadius(topLeft, topRight, bottomLeft, bottomRight);
  }

  @Override
  public int getBorderTopLeftRadius() {
    return this.roundedComponent.getBorderTopLeftRadius();
  }

  @Override
  public void setBorderTopLeftRadius(int radius) {
    this.roundedComponent.setBorderTopLeftRadius(radius);
  }

  @Override
  public int getBorderTopRightRadius() {
    return this.roundedComponent.getBorderTopRightRadius();
  }

  @Override
  public void setBorderTopRightRadius(int radius) {
    this.roundedComponent.setBorderTopRightRadius(radius);
  }

  @Override
  public int getBorderBottomLeftRadius() {
    return this.roundedComponent.getBorderBottomLeftRadius();
  }

  @Override
  public void setBorderBottomLeftRadius(int radius) {
    this.roundedComponent.setBorderBottomLeftRadius(radius);
  }

  @Override
  public int getBorderBottomRightRadius() {
    return this.roundedComponent.getBorderBottomRightRadius();
  }

  @Override
  public void setBorderBottomRightRadius(int radius) {
    this.roundedComponent.setBorderBottomRightRadius(radius);
  }

  @Override
  public MButtonSizeVariations getSizeVariation() {
    return this.sizeVariation;
  }

  @Override
  public MButtonColorVariations getColorVariation() {
    return this.colorVariation;
  }

  @Override
  protected void paintComponent(Graphics g) {
    this.roundedComponent.paintComponent(g, this);
    super.paintComponent(g);
  }
}
