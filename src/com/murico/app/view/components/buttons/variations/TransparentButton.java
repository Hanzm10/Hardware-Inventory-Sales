package com.murico.app.view.components.buttons.variations;

import com.murico.app.config.AppSettings;
import com.murico.app.view.components.buttons.MButton;

public class TransparentButton extends MButton {
  /**
   * 
   */
  private static final long serialVersionUID = 4614211604691921688L;

  public TransparentButton(String text) {
    super(text);

    this.colorVariation = MButtonColorVariations.TRANSPARENT;
    this.setBackground(AppSettings.getInstance().getTransparentColor());
  }
}
