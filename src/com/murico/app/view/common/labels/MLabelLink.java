package com.murico.app.view.common.labels;

import com.murico.app.config.UISettings;

public class MLabelLink extends MLabel{

  /**
   * 
   */
  private static final long serialVersionUID = -3909623055849812198L;

  public MLabelLink(String text) {
    super(text);
    
    this.setFont(UISettings.getInstance().getUIFont().getH6Font());
    this.labelType = MLabelTypes.LINK;
  }

}
