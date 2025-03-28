package com.murico.app.view;

import com.murico.app.Murico;
import com.murico.app.config.AppSettings;
import java.awt.*;
import javax.swing.*;

public class MainWindow extends JPanel {
  private final Murico murico;

  public MainWindow(Murico murico) {
    this.murico = murico;

    this.setPanelSize();
  }

  public Murico getMurico() {
    return murico;
  }

  private void setPanelSize() {
    Dimension size =
        new Dimension(
            AppSettings.getInstance().getAppMainScreenWidth(),
            AppSettings.getInstance().getAppMainScreenHeight());

    this.setPreferredSize(size);
  }
}
