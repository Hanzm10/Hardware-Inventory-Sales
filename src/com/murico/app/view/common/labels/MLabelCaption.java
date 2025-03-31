package com.murico.app.view.common.labels;

import com.murico.app.config.AppSettings;

public class MLabelCaption extends MLabel {

  /**
   * 
   */
  private static final long serialVersionUID = 2956349635626501889L;

  public MLabelCaption(String text) {
    super(text);

    this.setFont(AppSettings.getInstance().getMainFontCaption());
    this.labelType = MLabelTypes.CAPTION;
  }

}
