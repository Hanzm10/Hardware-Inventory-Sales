package com.murico.app.view.components.inputs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JPasswordField;
import com.murico.app.config.UISettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;
import com.murico.app.view.components.inputs.listeners.MTextFieldFocusListener;
import com.murico.app.view.utilities.RenderingUtilities;

public class MPasswordField extends JPasswordField
    implements MTextFieldInterface, RoundedCornerBorderComponentInterface {

  /**
   * 
   */
  private static final long serialVersionUID = -5740382440961826239L;

  protected String placeholderText;
  protected Color placeholderColor;

  public MPasswordField() {
    super();

    setDefaults();
  }

  public MPasswordField(String text) {
    super(text);

    setDefaults();
  }

  public MPasswordField(int columns) {
    super(columns);

    setDefaults();
  }

  public MPasswordField(String text, int columns) {
    super(text, columns);

    setDefaults();
  }

  public MPasswordField(String text, String placeholderText) {
    super(text);

    setDefaults();

    this.placeholderText = placeholderText;
  }

  public MPasswordField(String text, String placeholderText, int columns) {
    super(text, columns);

    setDefaults();

    this.placeholderText = placeholderText;
  }

  private void setDefaults() {
    setOpaque(false);
    setBackground(new Color(0x0, true));
    setFont(UISettings.getInstance().getUIFont().getBodyFont());

    setBorder(new RoundedCornerBorder(true));
    setEchoChar('*');

    placeholderText = "Password";
    placeholderColor = UISettings.getInstance().getUIColor().getPlaceholderColor();

    addFocusListener(new MTextFieldFocusListener());
  }

  // === MTextFieldInterface ===

  @Override
  public String getPlaceholderText() {
    return placeholderText;
  }

  @Override
  public void setPlaceholderText(String placeholderText) {
    this.placeholderText = placeholderText;

    repaint();
  }

  @Override
  public Color getPlaceholderColor() {
    return placeholderColor;
  }

  @Override
  public void setPlaceholderColor(Color placeholderColor) {
    this.placeholderColor = placeholderColor;

    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    var b = getBorder();
    Insets insets;

    if (b instanceof RoundedCornerBorder border) {
      if (!isOpaque()) {
        RenderingUtilities.paintBackgroundWithRoundedCornerBorder((Graphics2D) g.create(), this,
            border);
      }

      insets = border.getBorderInsets(this);
    } else {
      insets = getInsets();
    }

    if (this.getPassword().length == 0) {
      RenderingUtilities.paintPlaceholderText(g, this, insets, getFont(), getHeight());
    }

    super.paintComponent(g);
  }
}
