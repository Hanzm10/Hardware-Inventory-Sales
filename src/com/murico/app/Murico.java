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
    initBehavior();
    addComponents();
    finalConfig();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new Murico();
    });
  }

  private void initBehavior() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setFont(AppSettings.getInstance().getMainFont());
  }

  private void addComponents() {
    var mainWindow = new MainWindow(this);

    add(mainWindow);
  }

  private void finalConfig() {
    setTitle(AppSettings.getInstance().getAppTitle());

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
