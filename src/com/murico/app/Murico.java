package com.murico.app;

import com.murico.app.config.AppSettings;
import com.murico.app.view.MainWindow;

import javax.swing.*;

public class Murico extends JFrame {
    private MainWindow mainWindow;

    public Murico() {
        this.initClasses();
        this.initBehavior();
        this.addComponents();
        this.finalConfig();
    }

    private void initClasses() {
        this.mainWindow = new MainWindow(this);
    }

    private void initBehavior() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addComponents() {
        this.add(this.mainWindow);
    }

    private void finalConfig() {
        this.setTitle(AppSettings.getInstance().getAppTitle());

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Murico murico = new Murico();
        });
    }
}
