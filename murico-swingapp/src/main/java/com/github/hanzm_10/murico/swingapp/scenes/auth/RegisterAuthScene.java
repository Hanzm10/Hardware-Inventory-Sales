package com.github.hanzm_10.murico.swingapp.scenes.auth;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

import net.miginfocom.swing.MigLayout;

public class RegisterAuthScene implements Scene {

    protected JPanel view;
    protected MigLayout viewLayout;

    @Override
    public String getSceneName() {
        return "register";
    }

    @Override
    public JPanel getSceneView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        viewLayout = new MigLayout();
        view.setLayout(viewLayout);
    }

}
