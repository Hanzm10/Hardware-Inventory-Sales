package com.murico.app.view.common.labels;

import com.murico.app.config.UISettings;

public class MLabelCaption extends MLabel {

  /**
   * 
   */
  private static final long serialVersionUID = 2956349635626501889L;

  public MLabelCaption(String text) {
    super(text);

    this.setFont(UISettings.getInstance().getUIFont().getCaptionFont());
    this.labelType = MLabelTypes.CAPTION;
  }

}
