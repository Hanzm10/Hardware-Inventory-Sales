package com.murico.app.view.components.inputs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPasswordField;
import com.murico.app.config.AppSettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;
import com.murico.app.view.utilities.RenderingUtilities;

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
    this.setBorderColor(AppSettings.getInstance().getPrimaryColor());
  }

  @Override
  public void focusLost(FocusEvent e) {
    this.setBorderColor(AppSettings.getInstance().getBorderColor());
  }

  // === MouseListener ===

  @Override
  protected void paintComponent(Graphics g) {
    var b = this.getBorder();
    Insets insets;

    if (b instanceof RoundedCornerBorder border) {
      if (!this.isOpaque()) {
        RenderingUtilities.paintBackgroundWithRoundedCornerBorder((Graphics2D) g.create(), this,
            border);
      }

      insets = border.getBorderInsets(this);
    } else {
      insets = this.getInsets();
    }

    if (this.getPassword().length == 0) {
      RenderingUtilities.paintPlaceholderText(g, this, insets, getFont(), getHeight());
    }

    super.paintComponent(g);
  }
}
