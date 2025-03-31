package com.murico.app.view;

import java.awt.Dimension;
import javax.swing.JPanel;
import com.murico.app.Murico;
import com.murico.app.config.AppSettings;
import com.murico.app.view.pages.auth.MainAuthPage;

public class MainWindow extends JPanel {
  /**
   * 
   */
  private static final long serialVersionUID = -2591977387415502564L;
  private final Murico murico;

  public MainWindow(Murico murico) {
    this.murico = murico;

    this.setPanelSize();
    this.render();
  }

  public Murico getMurico() {
    return murico;
  }

  public void render() {
    switch (CurrentPage.getCurrentPage()) {
      case MAIN:
        this.removeAll();
        new MainAuthPage(this);
        break;
      case LOGIN:
        break;
      case REGISTER:
        break;
    }
  }

  private void setPanelSize() {
    var size = new Dimension(AppSettings.getInstance().getAppMainScreenWidth(),
        AppSettings.getInstance().getAppMainScreenHeight());

    this.setPreferredSize(size);
  }
}
