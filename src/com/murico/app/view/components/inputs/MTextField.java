package com.murico.app.view.components.inputs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.Document;
import com.murico.app.config.AppSettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorder;
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

    this.addFocusListener(this);
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
    this.setBorderColor(AppSettings.getInstance().getPrimaryColor());
  }

  @Override
  public void focusLost(FocusEvent e) {
    this.setBorderColor(AppSettings.getInstance().getBorderColor());
  }

  /**
   * === PaintComponent ===
   */

  @Override
  protected void paintComponent(Graphics g) {
    System.out.println("MTextField.paintComponent()");
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

    if (this.getText().isEmpty()) {
      RenderingUtilities.paintPlaceholderText(g, this, insets, getFont(), getHeight());
    }

    super.paintComponent(g);
  }

}