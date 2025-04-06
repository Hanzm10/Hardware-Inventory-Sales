package com.murico.app.view;

import javax.swing.JFrame;
import com.murico.app.config.AppSettings;
import com.murico.app.config.UISettings;

public class AppWindow extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = 1571125293617363458L;

  public AppWindow() {
    setTitle(AppSettings.getInstance().getAppTitle());
    setFont(UISettings.getInstance().getUIFont().getBodyFont());

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    add(new AppContainer());

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
