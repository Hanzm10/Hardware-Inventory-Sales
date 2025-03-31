package com.murico.app.view.common.labels;

public class MLabelFactory {
  public static MLabel createLabel(String text) {
    return new MLabel(text);
  }  public static MLabel createLabel(String text, MLabelTypes labelType) {
    return new MLabel(text);
  }
}
