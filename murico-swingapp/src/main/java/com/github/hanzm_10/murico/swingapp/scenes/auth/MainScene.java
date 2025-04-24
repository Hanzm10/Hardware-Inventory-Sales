package com.github.hanzm_10.murico.swingapp.scenes.auth;

import javax.swing.JButton;
import javax.swing.JPanel;
import com.github.hanzm_10.murico.swingapp.scenes.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.SceneNavigator;

public class MainScene implements Scene {
    private JPanel view;

    public MainScene() {}

    @Override
    public String getName() {
        return "main";
    }

    @Override
    public JPanel getView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        var button = new JButton("Login");

        button.addActionListener(_ -> {
            SceneNavigator.navigateTo("auth/login");
        });

        view.add(button);
        System.out.println("MainScene onCreate");
    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub
        System.out.println("MainScene onHide");
    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub
        System.out.println("MainScene onShow");
    }

}
