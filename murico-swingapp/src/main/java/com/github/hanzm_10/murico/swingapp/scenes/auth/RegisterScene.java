package com.github.hanzm_10.murico.swingapp.scenes.auth;

import javax.swing.JPanel;
import com.github.hanzm_10.murico.swingapp.scenes.Scene;

public class RegisterScene implements Scene {
    private JPanel view;

    public RegisterScene() {}

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public JPanel getView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub

    }

}
