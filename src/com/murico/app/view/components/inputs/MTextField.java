package com.murico.app.view.components.inputs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.Document;
import com.murico.app.config.AppSettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;

/**
 * MTextField is a custom text field class that extends JTextField and implements
 * MTextFieldInterface and FocusListener. It provides a rounded text field with custom border
 * properties and focus effects.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class MTextField extends JTextField
    implements MTextFieldInterface,
    com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface, FocusListener {

  /**
   * 
   */
  private static final long serialVersionUID = -6319237328212530306L;

  protected String placeholderText;
  protected Color placeholderColor;

  public MTextField() {
    super("");

    this.setDefaults();
  }

  public MTextField(String text) {
    super(text);

    this.setDefaults();
  }

  public MTextField(String text, String placeholderText) {
    super(text);

    this.setDefaults();

    this.placeholderText = placeholderText;
  }

  public MTextField(int columns) {
    super(columns);

    this.setDefaults();
  }

  public MTextField(String text, int columns) {
    super(text, columns);

    this.setDefaults();
  }

  public MTextField(String text, String placeholderText, int columns) {
    super(text, columns);

    this.setDefaults();

    this.placeholderText = placeholderText;
  }

  public MTextField(Document doc, String text, int columns) {
    super(doc, text, columns);

    this.setDefaults();
  }

  public MTextField(Document doc, String text, String placeholderText, int columns) {
    super(doc, text, columns);

    this.setDefaults();

    this.placeholderText = placeholderText;
  }

  private void setDefaults() {
    this.setOpaque(false);
    this.setFont(AppSettings.getInstance().getMainFontBody());

    this.setBorder(new RoundedCornerBorder(true));

    this.placeholderText = this.getText().isEmpty() ? "Enter a text here..." : this.getText();
    this.placeholderColor = AppSettings.getInstance().getPlaceholderColor();
  }

  /**
   * === MTextFieldInterface ===
   */

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

  /**
   * === RoundedCornerBorderComponentInterface ===
   */

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

  /**
   * === FocusListener ===
   */

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

  /**
   * === PaintComponent ===
   */

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

    if (this.getText().isEmpty()) {
      g.setColor(this.placeholderColor);

      g.drawString(this.placeholderText, insets.left,
          getHeight() / 2 + getFont().getSize() / 2 - insets.top / 2);
    }

    super.paintComponent(g);
  }

}