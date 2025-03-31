package com.murico.app.view.components.buttons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import com.murico.app.config.AppSettings;
import com.murico.app.view.borders.RoundedCornerBorder;
import com.murico.app.view.components.buttons.variations.MButtonColorVariations;
import com.murico.app.view.components.helper.ComponentHelper;

/**
 * MButton is a custom button class that extends JButton and implements MButtonInterface and
 * MouseListener. It provides a rounded button with color variations and hover/press effects.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class MButton extends JButton implements MButtonInterface, MouseListener, FocusListener {

  /**
   * 
   */
  private static final long serialVersionUID = -5038858231143921204L;

  protected final ComponentHelper<MButton> componentHelper = new ComponentHelper<>(this);

  protected MButtonColorVariations colorVariation;

  protected boolean hovered;
  protected boolean pressed;

  private Color previousBackground;

  public MButton(String text) {
    super(text);

    this.disableDefaultButtonStyle();
    this.setDefaults();

    this.addMouseListener(this);
  }

  private void disableDefaultButtonStyle() {
    this.setFocusPainted(false);
    this.setContentAreaFilled(false);
  }

  private void setDefaults() {
    this.setFont(AppSettings.getInstance().getMainFontButton());

    this.setBorder(new RoundedCornerBorder());
  }

  private RoundedCornerBorder assertBorderIsRoundedCornerBorder() {
    var border = this.getBorder();

    assert border instanceof RoundedCornerBorder : "Border is not an instance of RoundedCornerBorder";

    return (RoundedCornerBorder) border;
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      this.pressed = true;
      var alpha = this.colorVariation == MButtonColorVariations.TRANSPARENT ? 50 : 200;

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
        var alpha = this.colorVariation == MButtonColorVariations.TRANSPARENT ? 75 : 255;
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
      var alpha = this.colorVariation == MButtonColorVariations.TRANSPARENT ? 75 : 225;

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
    this.assertBorderIsRoundedCornerBorder().setBorderRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public void setBorderRadius(int topLeft, int topRight, int bottomLeft, int bottomRight) {
    this.assertBorderIsRoundedCornerBorder().setBorderRadius(topLeft, topRight, bottomLeft,
        bottomRight);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderTopLeftRadius() {
    return this.assertBorderIsRoundedCornerBorder().getBorderTopLeftRadius();
  }

  @Override
  public void setBorderTopLeftRadius(int radius) {
    this.assertBorderIsRoundedCornerBorder().setBorderTopLeftRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderTopRightRadius() {
    return this.assertBorderIsRoundedCornerBorder().getBorderTopRightRadius();
  }

  @Override
  public void setBorderTopRightRadius(int radius) {
    this.assertBorderIsRoundedCornerBorder().setBorderTopRightRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderBottomLeftRadius() {
    return this.assertBorderIsRoundedCornerBorder().getBorderBottomLeftRadius();
  }

  @Override
  public void setBorderBottomLeftRadius(int radius) {
    this.assertBorderIsRoundedCornerBorder().setBorderBottomLeftRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderBottomRightRadius() {
    return this.assertBorderIsRoundedCornerBorder().getBorderBottomRightRadius();
  }

  @Override
  public void setBorderBottomRightRadius(int radius) {
    this.assertBorderIsRoundedCornerBorder().setBorderBottomRightRadius(radius);

    this.revalidate();
    this.repaint();
  }

  @Override
  public int getBorderWidth() {
    return this.assertBorderIsRoundedCornerBorder().getBorderWidth();
  }

  @Override
  public void setBorderWidth(int borderWidth) {
    this.assertBorderIsRoundedCornerBorder().setBorderWidth(borderWidth);

    this.revalidate();
    this.repaint();
  }

  @Override
  public MButtonColorVariations getColorVariation() {
    return this.colorVariation;
  }

  @Override
  public void focusGained(FocusEvent e) {
    this.revalidate();
    this.repaint();
  }

  @Override
  public void focusLost(FocusEvent e) {
    this.revalidate();
    this.repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (this.isFocusOwner()) {
      this.assertBorderIsRoundedCornerBorder().setDrawBorder(true);
    } else {
      this.assertBorderIsRoundedCornerBorder().setDrawBorder(false);
    }
  }
}
