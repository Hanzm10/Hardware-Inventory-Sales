package com.murico.app.view;

import com.murico.app.Murico;
import com.murico.app.config.AppSettings;
import com.murico.app.view.pages.auth.MainAuthPage;
import java.awt.*;
import javax.swing.*;

public class MainWindow extends JPanel {
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
        this.add(new MainAuthPage());
        break;
      case LOGIN:
        break;
      case REGISTER:
        break;
    }
  }

  private void setPanelSize() {
    Dimension size =
        new Dimension(
            AppSettings.getInstance().getAppMainScreenWidth(),
            AppSettings.getInstance().getAppMainScreenHeight());

    this.setPreferredSize(size);
  }
}
