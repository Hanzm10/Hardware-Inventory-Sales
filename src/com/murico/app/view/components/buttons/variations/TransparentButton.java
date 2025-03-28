package com.murico.app.view.components.buttons.variations;

import com.murico.app.view.components.buttons.MButton;

public class TransparentButton extends MButton {
  public TransparentButton(String text) {
    super(text);

    this.colorVariation = MButtonColorVariations.TRANSPARENT;
  }
}
