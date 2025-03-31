package com.murico.app.view.common.labels;

import com.murico.app.config.AppSettings;

public class MLabelLink extends MLabel{

  /**
   * 
   */
  private static final long serialVersionUID = -3909623055849812198L;

  public MLabelLink(String text) {
    super(text);
    
    this.setFont(AppSettings.getInstance().getMainFontLink());
    this.labelType = MLabelTypes.LINK;
  }

}
