package com.github.hanzm_10.murico.swingapp.scenes.home;

import javax.swing.*;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.ui.buttons.LogoutButton;

public class SettingsScene implements Scene {
    private JPanel view;

    @Override
    public String getSceneName() {
        return "settings";
    }

    @Override
    public JPanel getSceneView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
    	
        JButton logoutButton = new LogoutButton();
        logoutButton.setText("Logout");
       view.add(logoutButton);
        
    }
    @Override
    public boolean onDestroy() {
        return true;
    }
}
