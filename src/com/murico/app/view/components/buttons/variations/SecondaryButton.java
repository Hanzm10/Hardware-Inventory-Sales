package com.murico.app.view.components.buttons.variations;

import com.murico.app.config.UISettings;
import com.murico.app.view.components.buttons.MButton;

public class SecondaryButton extends MButton {
  /**
   * 
   */
  private static final long serialVersionUID = -3303654769638476114L;

  public SecondaryButton() {
    this("Secondary Button");
  }

  public SecondaryButton(String text) {
    super(text);

    this.colorVariation = MButtonColorVariations.SECONDARY;

    this.setBackground(UISettings.getInstance().getUIColor().getSecondaryColor());
    this.setForeground(UISettings.getInstance().getUIColor().getSecondaryForegroundColor());
  }
}
