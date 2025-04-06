package com.murico.app.view.components.buttons.variations;

import com.murico.app.config.UISettings;
import com.murico.app.view.components.buttons.MButton;

public class PrimaryButton extends MButton {
  /**
   * 
   */
  private static final long serialVersionUID = -899374905136552556L;

  public PrimaryButton() {
    this("Primary Button");
  }

  public PrimaryButton(String text) {
    super(text);

    this.colorVariation = MButtonColorVariations.PRIMARY;

    this.setBackground(UISettings.getInstance().getUIColor().getPrimaryColor());
    this.setForeground(UISettings.getInstance().getUIColor().getPrimaryForegroundColor());
  }
}
