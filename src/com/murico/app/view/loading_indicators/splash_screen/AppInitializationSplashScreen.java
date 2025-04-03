package com.murico.app.view.loading_indicators.splash_screen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

public class AppInitializationSplashScreen extends JWindow {

  /**
   * 
   */
  private static final long serialVersionUID = -1712636901239751518L;

  private double initialiationProgress;
  private JLabel splashScreenLabel;
  private JProgressBar splashScreenProgressBar;

  public AppInitializationSplashScreen() {
    splashScreenLabel = new JLabel("Initializing Murico...");
    splashScreenProgressBar = new JProgressBar();

    splashScreenProgressBar.setIndeterminate(true);
    splashScreenProgressBar.setStringPainted(true);

    add(splashScreenLabel, BorderLayout.CENTER);
    add(splashScreenProgressBar = new JProgressBar(), BorderLayout.SOUTH);

    pack();
    setSize(
        new Dimension(1280, 720));
    setPreferredSize(new Dimension(1280, 720));
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public void setProgressLabel(String label) {
    this.splashScreenLabel.setText(label);
  }

}
