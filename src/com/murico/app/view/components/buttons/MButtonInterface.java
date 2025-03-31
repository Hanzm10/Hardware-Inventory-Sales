package com.murico.app.view.components.buttons;

import com.murico.app.view.components.buttons.variations.MButtonColorVariations;

public interface MButtonInterface {
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


  int getBorderWidth();

  void setBorderWidth(int borderWidth);

  MButtonColorVariations getColorVariation();
}
