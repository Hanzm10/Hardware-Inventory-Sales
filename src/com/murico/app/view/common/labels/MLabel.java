package com.murico.app.view.common.labels;

import javax.swing.JLabel;
import com.murico.app.config.UISettings;

public class MLabel extends JLabel {
  protected MLabelTypes labelType;

  /**
   * 
   */
  private static final long serialVersionUID = 4222724410623595274L;

  public MLabel(String text) {
    super(text);

    this.setFont(UISettings.getInstance().getUIFont().getBodyFont());
    this.labelType = MLabelTypes.DEFAULT;
  }

  public MLabelTypes getLabelType() {
    return this.labelType;
  }
}
