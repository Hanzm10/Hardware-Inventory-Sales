package com.murico.app.view.common.labels;

import com.murico.app.config.AppSettings;

public class MLabelSubtitle extends MLabel {

  /**
   * 
   */
  private static final long serialVersionUID = -6852872612466502910L;

  public MLabelSubtitle(String text) {
    super(text);

    this.labelType = MLabelTypes.SUBTITLE;
    this.setFont(AppSettings.getInstance().getAppFontSettings().getMainFontSubtitle());
  }

}
