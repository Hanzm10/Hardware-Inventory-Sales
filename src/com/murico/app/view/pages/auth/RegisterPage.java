package com.murico.app.view.pages.auth;

import javax.swing.JPanel;
import com.murico.app.view.MainWindow;

public class RegisterPage extends JPanel {

  private static final long serialVersionUID = 1L;

  private final MainWindow mainWindow;

  /**
   * Create the panel.
   */
  public RegisterPage(MainWindow mainWindow) {
    this.mainWindow = mainWindow;
    setBackground(mainWindow.getBackground());

  }

}
