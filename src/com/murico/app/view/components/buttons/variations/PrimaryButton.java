package com.murico.app.view.components.buttons.variations;

import com.murico.app.config.AppSettings;
import com.murico.app.view.components.buttons.MButton;

public class PrimaryButton extends MButton {
  public PrimaryButton(String text) {
    super(text);

    this.colorVariation = MButtonColorVariations.PRIMARY;

    this.setBackground(AppSettings.getInstance().getPrimaryColor());
    this.setForeground(AppSettings.getInstance().getPrimaryForegroundColor());
  }
}
