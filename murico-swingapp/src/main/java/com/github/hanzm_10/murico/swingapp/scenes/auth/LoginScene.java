package com.github.hanzm_10.murico.swingapp.scenes.auth;

import javax.swing.JLabel;
import javax.swing.JPanel;
import com.github.hanzm_10.murico.swingapp.scenes.Scene;

public class LoginScene implements Scene {
    private JPanel view;

    public LoginScene() {}

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public JPanel getView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        var hi = new JLabel("Login");

        view.add(hi);
        System.out.println("LoginScene onCreate");
    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onShow() {
        System.out.println("LoginScene onShow");
    }

}
