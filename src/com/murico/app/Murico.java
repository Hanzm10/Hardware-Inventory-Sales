package com.murico.app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.murico.app.config.AppSettings;
import com.murico.app.view.MainWindow;

public class Murico extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = 5584823642462847023L;

  public Murico() {
    this.initBehavior();
    this.addComponents();
    this.finalConfig();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          new Murico();
        });
  }

  private void initBehavior() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setFont(AppSettings.getInstance().getMainFont());
  }

  private void addComponents() {
    var mainWindow = new MainWindow(this);

    this.add(mainWindow);
  }

  private void finalConfig() {
    this.setTitle(AppSettings.getInstance().getAppTitle());

    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
}
