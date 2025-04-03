package com.murico.app.view.components.inputs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.text.Document;
import com.murico.app.config.AppSettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;
import com.murico.app.view.components.inputs.listeners.MTextFieldFocusListener;
import com.murico.app.view.utilities.RenderingUtilities;

/**
 * MTextField is a custom text field class that extends JTextField and implements
 * MTextFieldInterface and FocusListener. It provides a rounded text field with custom border
 * properties and focus effects.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class MTextField extends JTextField
    implements MTextFieldInterface, RoundedCornerBorderComponentInterface {

  /**
   * 
   */
  private static final long serialVersionUID = -6319237328212530306L;

  protected String placeholderText;
  protected Color placeholderColor;

  public MTextField() {
    super("");

    setDefaults();
  }

  public MTextField(String text) {
    super(text);

    setDefaults();
  }

  public MTextField(String text, String placeholderText) {
    super(text);

    setDefaults();

    this.placeholderText = placeholderText;
  }

  public MTextField(int columns) {
    super(columns);

    setDefaults();
  }

  public MTextField(String text, int columns) {
    super(text, columns);

    setDefaults();
  }

  public MTextField(String text, String placeholderText, int columns) {
    super(text, columns);

    setDefaults();

    this.placeholderText = placeholderText;
  }

  public MTextField(Document doc, String text, int columns) {
    super(doc, text, columns);

    setDefaults();
  }

  public MTextField(Document doc, String text, String placeholderText, int columns) {
    super(doc, text, columns);

    setDefaults();

    this.placeholderText = placeholderText;
  }

  private void setDefaults() {
    setOpaque(false);
    setFont(AppSettings.getInstance().getAppFontSettings().getMainFontBody());

    setBorder(new RoundedCornerBorder(true));

    placeholderText = this.getText().isEmpty() ? "Enter a text here..." : this.getText();
    placeholderColor = AppSettings.getInstance().getAppColorSettings().getPlaceholderColor();

    addFocusListener(new MTextFieldFocusListener());
  }

  /**
   * === MTextFieldInterface ===
   */

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

  /**
   * === PaintComponent ===
   */

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

    if (getText().isEmpty()) {
      RenderingUtilities.paintPlaceholderText(g, this, insets, getFont(), getHeight());
    }

    super.paintComponent(g);
  }

}