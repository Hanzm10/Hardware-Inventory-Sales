package com.murico.app.view.components.inputs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPasswordField;
import com.murico.app.config.AppSettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;

public class MPasswordField extends JPasswordField
    implements MTextFieldInterface, RoundedCornerBorderComponentInterface, FocusListener {

  /**
   * 
   */
  private static final long serialVersionUID = -5740382440961826239L;

  protected String placeholderText;
  protected Color placeholderColor;

  public MPasswordField() {
    super();

    this.setDefaults();
  }

  public MPasswordField(String text) {
    super(text);

    this.setDefaults();
  }

  public MPasswordField(int columns) {
    super(columns);

    this.setDefaults();
  }

  public MPasswordField(String text, int columns) {
    super(text, columns);

    this.setDefaults();
  }

  public MPasswordField(String text, String placeholderText) {
    super(text);

    this.setDefaults();

    this.placeholderText = placeholderText;
  }

  public MPasswordField(String text, String placeholderText, int columns) {
    super(text, columns);

    this.setDefaults();

    this.placeholderText = placeholderText;
  }

  private void setDefaults() {
    this.setOpaque(false);
    this.setFont(AppSettings.getInstance().getMainFontBody());

    this.setBorder(new RoundedCornerBorder(true));

    this.placeholderText = "Password";
    this.placeholderColor = AppSettings.getInstance().getPlaceholderColor();

    this.setEchoChar('*');
  }

  // === MTextFieldInterface ===

  @Override
  public String getPlaceholderText() {
    return this.placeholderText;
  }

  @Override
  public void setPlaceholderText(String placeholderText) {
    this.placeholderText = placeholderText;

    this.revalidate();
    this.repaint();
  }

  @Override
  public Color getPlaceholderColor() {
    return this.placeholderColor;
  }

  @Override
  public void setPlaceholderColor(Color placeholderColor) {
    this.placeholderColor = placeholderColor;

    this.repaint();
  }

  // === RoundedCornerBorderComponentInterface ===

  @Override
  public void repaintBorder() {
    this.revalidate();
    this.repaint();
  }

  @Override
  public RoundedCornerBorder getRoundedCornerBorder() throws AssertionError {
    var border = this.getBorder();

    assert border instanceof RoundedCornerBorder : "Border is not an instance of RoundedCornerBorder";

    return (RoundedCornerBorder) border;
  }

  // === FocusListener ===

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

  // === MouseListener ===

  @Override
  protected void paintComponent(Graphics g) {
    var b = this.getBorder();
    Insets insets;

    if (b instanceof RoundedCornerBorder border) {
      if (!this.isOpaque()) {
        var g2d = (java.awt.Graphics2D) g.create();

        var borderWidth = border.getBorderWidth();
        var borderX = borderWidth / 2;
        var borderY = borderWidth / 2;
        var borderW = this.getWidth() - borderWidth;
        var borderH = this.getHeight() - borderWidth;

        g2d.setColor(this.getBackground());
        g2d.fill(border.createBorder(borderX, borderY, borderW, borderH));
        g2d.dispose();
      }

      insets = border.getBorderInsets(this);
    } else {
      insets = this.getInsets();
    }

    if (this.isFocusOwner()) {
      this.setBorderColor(AppSettings.getInstance().getPrimaryColor());
    } else {
      this.setBorderColor(AppSettings.getInstance().getBorderColor());
    }

    if (this.getPassword().length == 0) {
      g.setColor(this.placeholderColor);

      g.drawString(this.placeholderText, insets.left,
          getHeight() / 2 + getFont().getSize() / 2 - insets.top / 2);
    }

    super.paintComponent(g);
  }
}
