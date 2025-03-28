package com.murico.app.view.components.buttons;

import com.murico.app.view.components.buttons.variations.MButtonColorVariations;

public interface MButtonInterface {
  void mouseClicked();

  void mousePressed();

  void mouseReleased();

  void mouseEntered();

  void mouseExited();

  void setBorderRadius(int radius);

  void setBorderRadius(int topLeft, int topRight, int bottomLeft, int bottomRight);

  int getBorderTopLeftRadius();

  void setBorderTopLeftRadius(int radius);

  int getBorderTopRightRadius();

  void setBorderTopRightRadius(int radius);

  int getBorderBottomLeftRadius();

  void setBorderBottomLeftRadius(int radius);

  int getBorderBottomRightRadius();

  void setBorderBottomRightRadius(int radius);

  MButtonColorVariations getColorVariation();
}
