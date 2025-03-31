package com.murico.app.view.components.inputs;

import javax.swing.JTextField;
import javax.swing.text.Document;
import com.murico.app.view.borders.RoundedCornerBorder;

public class MTextField extends JTextField implements MTextFieldInterface {

  /**
   * 
   */
  private static final long serialVersionUID = -6319237328212530306L;

  public MTextField() {
    super();

    this.setDefaults();
  }

  public MTextField(String text) {
    super(text);

    this.setDefaults();
  }

  public MTextField(int columns) {
    super(columns);

    this.setDefaults();
  }

  public MTextField(String text, int columns) {
    super(text, columns);

    this.setDefaults();
  }

  public MTextField(Document doc, String text, int columns) {
    super(doc, text, columns);

    this.setDefaults();
  }

  private void setDefaults() {
    this.setOpaque(false);
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

  private RoundedCornerBorder assertBorderIsRoundedCornerBorder() {
    var border = this.getBorder();

    assert border instanceof RoundedCornerBorder : "Border is not an instance of RoundedCornerBorder";

    return (RoundedCornerBorder) border;
  }
}