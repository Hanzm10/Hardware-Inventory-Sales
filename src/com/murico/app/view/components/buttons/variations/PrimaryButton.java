package com.murico.app.view.components.buttons.variations;

import com.murico.app.config.AppSettings;
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

    this.setBackground(AppSettings.getInstance().getAppColorSettings().getPrimaryColor());
    this.setForeground(AppSettings.getInstance().getAppColorSettings().getPrimaryForegroundColor());
  }
}
