package com.murico.app.view.components.buttons.variations;

import com.murico.app.config.UISettings;
import com.murico.app.view.components.buttons.MButton;

public class TransparentButton extends MButton {
  /**
   * 
   */
  private static final long serialVersionUID = 4614211604691921688L;

  public TransparentButton() {
    this("Transparent Button");
  }

  public TransparentButton(String text) {
    super(text);

    this.colorVariation = MButtonColorVariations.TRANSPARENT;
    this.setBackground(UISettings.getInstance().getUIColor().getTransparentColor());
  }
}
