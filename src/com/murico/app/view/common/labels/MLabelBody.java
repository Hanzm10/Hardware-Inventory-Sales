package com.murico.app.view.common.labels;

import com.murico.app.config.UISettings;

public class MLabelBody extends MLabel {

  /**
   * 
   */
  private static final long serialVersionUID = -1600051499874783213L;

  public MLabelBody(String text) {
    super(text);

    this.labelType = MLabelTypes.BODY;
    this.setFont(UISettings.getInstance().getUIFont().getBodyFont());
  }
}
