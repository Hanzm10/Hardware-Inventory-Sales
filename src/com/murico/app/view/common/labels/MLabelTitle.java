package com.murico.app.view.common.labels;

import com.murico.app.config.AppSettings;

public class MLabelTitle extends MLabel {


  /**
   * 
   */
  private static final long serialVersionUID = 9139359077016680841L;

  public MLabelTitle(String text) {
    super(text);

    this.labelType = MLabelTypes.TITLE;
    this.setFont(AppSettings.getInstance().getMainFontTitle());
  }
}
