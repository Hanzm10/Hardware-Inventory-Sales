package com.murico.app;

import com.murico.app.config.AppSettings;
import com.murico.app.view.MainWindow;
import javax.swing.*;

public class Murico extends JFrame {

  public Murico() {
    this.initBehavior();
    this.addComponents();
    this.finalConfig();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          Murico murico = new Murico();
        });
  }

  private void initBehavior() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setFont(AppSettings.getInstance().getMainFont());
  }

  private void addComponents() {
    MainWindow mainWindow = new MainWindow(this);

    this.add(mainWindow);
  }

  private void finalConfig() {
    this.setTitle(AppSettings.getInstance().getAppTitle());

    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
}
