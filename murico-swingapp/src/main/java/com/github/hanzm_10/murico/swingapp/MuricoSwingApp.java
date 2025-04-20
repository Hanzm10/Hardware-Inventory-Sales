package com.github.hanzm_10.murico.swingapp;

import javax.swing.SwingUtilities;
import com.github.hanzm_10.murico.swingapp.exceptions.handlers.GlobalUncaughtExceptionHandler;

public class MuricoSwingApp {
    private static void initialize() {
        System.out.println("Murico Swing Application Initialized");
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());
        SwingUtilities.invokeLater(MuricoSwingApp::initialize);
    }
}
