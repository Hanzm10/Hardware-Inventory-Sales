package com.murico.app.view.common.labels;

import com.murico.app.config.AppSettings;

public class MLabelFootnote extends MLabel {

  /**
   * 
   */
  private static final long serialVersionUID = -5415145561632823354L;

  public MLabelFootnote(String text) {
    super(text);
    
    this.setFont(AppSettings.getInstance().getAppFontSettings().getMainFontFootnote());
    this.labelType = MLabelTypes.FOOTNOTE;
  }

}
