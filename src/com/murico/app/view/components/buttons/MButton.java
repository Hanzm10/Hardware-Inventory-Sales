package com.murico.app.view.components.buttons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import com.murico.app.config.AppSettings;
import com.murico.app.view.components.base.RoundedComponent;
import com.murico.app.view.components.buttons.variations.MButtonColorVariations;
import com.murico.app.view.components.helper.ComponentHelper;

/**
 * MButton is a custom button class that extends JButton and implements MButtonInterface and
 * MouseListener. It provides a rounded button with color variations and hover/press effects.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class MButton extends JButton implements MButtonInterface, MouseListener {

  /**
   * 
   */
  private static final long serialVersionUID = -5038858231143921204L;

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

    this.componentHelper = new ComponentHelper<>(this);
    this.roundedComponent = new RoundedComponent();

    this.roundedComponent.setBorderRadius(AppSettings.getInstance().getBaseBorderRadius());

    this.addMouseListener(this);
  }

  private void disableDefaultButtonStyle() {
    setBorderPainted(false);
    setContentAreaFilled(false);
    setFocusPainted(false);
  }

  private void setDefaults() {
    this.setFont(AppSettings.getInstance().getMainFontButton());
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      this.pressed = true;
      var alpha = this.colorVariation == MButtonColorVariations.TRANSPARENT ? 150 : 200;

      this.previousBackground = this.getBackground();
      this.setBackground(new Color(this.previousBackground.getRed(),
          this.previousBackground.getGreen(), this.previousBackground.getBlue(), alpha));
    });
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      this.pressed = false;

      if (!this.hovered) {
        var alpha = this.colorVariation == MButtonColorVariations.TRANSPARENT ? 0 : 255;
        this.componentHelper.setCursorToDefault();
        this.previousBackground = this.getBackground();
        this.setBackground(new Color(this.previousBackground.getRed(),
            this.previousBackground.getGreen(), this.previousBackground.getBlue(), alpha));
      } else {
        var alpha = this.colorVariation == MButtonColorVariations.TRANSPARENT ? 200 : 255;
        this.previousBackground = this.getBackground();
        this.setBackground(new Color(this.previousBackground.getRed(),
            this.previousBackground.getGreen(), this.previousBackground.getBlue(), alpha));
      }
    });
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      this.hovered = true;
      var alpha = this.colorVariation == MButtonColorVariations.TRANSPARENT ? 200 : 225;

      if (!this.pressed) {
        this.componentHelper.setCursorToHand();
        this.previousBackground = this.getBackground();
        this.setBackground(new Color(this.previousBackground.getRed(),
            this.previousBackground.getGreen(), this.previousBackground.getBlue(), alpha));
      }
    });
  }

  @Override
  public void mouseExited(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      this.hovered = false;
      var alpha = this.colorVariation == MButtonColorVariations.TRANSPARENT ? 0 : 255;

      if (!this.pressed) {
        this.componentHelper.setCursorToDefault();

        this.previousBackground = this.getBackground();
        this.setBackground(new Color(this.previousBackground.getRed(),
            this.previousBackground.getGreen(), this.previousBackground.getBlue(), alpha));
      }
    });
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
