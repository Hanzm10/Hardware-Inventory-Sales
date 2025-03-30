package com.murico.app.view;

import com.murico.app.Murico;
import com.murico.app.config.AppSettings;
import com.murico.app.view.pages.auth.MainAuthPage;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JPanel {
  private final Murico murico;
  private final GridBagConstraints gbc;

  public MainWindow(Murico murico) {
    this.murico = murico;

    this.gbc = new GridBagConstraints();

    this.initLayout();
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
        this.add(new MainAuthPage(), this.gbc);
        break;
      case LOGIN:
        break;
      case REGISTER:
        break;
    }
  }

  private void initLayout() {
    this.setLayout(new GridBagLayout());

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.CENTER;
  }

  private void setPanelSize() {
    Dimension size = new Dimension(AppSettings.getInstance().getAppMainScreenWidth(),
        AppSettings.getInstance().getAppMainScreenHeight());

    this.setPreferredSize(size);
  }
}
