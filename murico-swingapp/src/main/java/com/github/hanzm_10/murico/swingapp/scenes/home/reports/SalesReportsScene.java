package com.github.hanzm_10.murico.swingapp.scenes.home.reports;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

public final class SalesReportsScene implements Scene {
    private JPanel view;

    @Override
    public String getSceneName() {
        return "sales reports";
    }

    @Override
    public JPanel getSceneView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {

    }
}

