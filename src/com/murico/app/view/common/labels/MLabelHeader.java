package com.murico.app.view.common.labels;

import com.murico.app.config.AppSettings;

public class MLabelHeader extends MLabel {
  /**
   * 
   */
  private static final long serialVersionUID = 1392136324235426313L;

  public MLabelHeader(String text) {
    super(text);

    this.setFont(AppSettings.getInstance().getMainFontHeader());
    this.labelType = MLabelTypes.HEADER;
  }
}
