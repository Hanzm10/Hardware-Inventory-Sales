package com.github.hanzm_10.murico.app.loading;

import javax.swing.JPanel;
import javax.swing.JWindow;

public class SplashScreenFactory {
    /**
     * Creates a splash screen using the provided JPanel.
     * 
     * Doesn't set the size of the JPanel, so it should be set before calling this method. Doesn't
     * show the splash screen, so it should be shown after calling this method.
     *
     * @param screen the JPanel to be used as the splash screen
     * @return a JWindow containing the splash screen
     */
    public static JWindow createSplashScreenJWindow(JPanel screen) {
        var window = new JWindow();

        window.setContentPane(screen);
        window.setSize(screen.getPreferredSize());
        window.setPreferredSize(screen.getPreferredSize());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setAlwaysOnTop(true);

        return window;
    }
}
