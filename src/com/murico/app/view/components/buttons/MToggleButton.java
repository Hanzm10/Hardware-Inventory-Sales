package com.murico.app.view.components.buttons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import com.murico.app.config.AppSettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;
import com.murico.app.view.components.buttons.variations.MButtonColorVariations;
import com.murico.app.view.components.helper.ComponentHelper;
import com.murico.app.view.utilities.RenderingUtilities;

public class MToggleButton extends JToggleButton implements MButtonInterface,
    RoundedCornerBorderComponentInterface, MouseListener, FocusListener {

  private static final long serialVersionUID = 3411237617423863266L;

  protected final ComponentHelper<MToggleButton> componentHelper = new ComponentHelper<>(this);

  protected MButtonColorVariations colorVariation = MButtonColorVariations.TRANSPARENT;

  protected boolean hovered;
  protected boolean pressed;

  private Color previousBackground;

  public MToggleButton() {
    this("T");
  }

  public MToggleButton(String text) {
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

    this.setBackground(AppSettings.getInstance().getTransparentColor());
    this.setRolloverEnabled(true);
  }

  /** === MButtonInterface === */

  @Override
  public MButtonColorVariations getColorVariation() {
    return this.colorVariation;
  }

  /** === RoundedCornerBorderComponentInterface === */

  @Override
  public void repaintBorder() {
    this.revalidate();
    this.repaint();
  }

  @Override
  public RoundedCornerBorder getRoundedCornerBorder() {
    var border = this.getBorder();

    assert border instanceof RoundedCornerBorder : "Border is not an instance of RoundedCornerBorder";

    return (RoundedCornerBorder) border;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    System.out.println(this.isSelected());
  }

  @Override
  public void mousePressed(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      this.pressed = true;
      var val = this.hovered ? 175 : 125;
      var alpha = this.isSelected() ? val : 75;

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
        var alpha = this.isSelected() ? 100 : 0;
        this.componentHelper.setCursorToDefault();
        this.previousBackground = this.getBackground();
        this.setBackground(new Color(this.previousBackground.getRed(),
            this.previousBackground.getGreen(), this.previousBackground.getBlue(), alpha));
      } else {
        var alpha = this.isSelected() ? 175 : 100;
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
      var alpha = this.isSelected() ? 225 : 100;

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
      var alpha = this.isSelected() ? 100 : 0;

      if (!this.pressed) {
        this.componentHelper.setCursorToDefault();

        this.previousBackground = this.getBackground();
        this.setBackground(new Color(this.previousBackground.getRed(),
            this.previousBackground.getGreen(), this.previousBackground.getBlue(), alpha));
      }
    });
  }

  /** === FocusListener === */

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

  /** === PaintComponent === */

  @Override
  protected void paintComponent(Graphics g) {
    var b = this.getBorder();

    if (!this.isOpaque() && b instanceof RoundedCornerBorder) {
      RenderingUtilities.paintBackgroundWithRoundedCornerBorder((Graphics2D) g.create(), this,
          (RoundedCornerBorder) b);
    }

    if (this.isFocusOwner()) {
      this.getRoundedCornerBorder().setDrawBorder(true);
    } else {
      this.getRoundedCornerBorder().setDrawBorder(false);
    }

    super.paintComponent(g);
  }

}
