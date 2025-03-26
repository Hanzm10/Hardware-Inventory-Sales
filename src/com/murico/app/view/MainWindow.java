package com.murico.app.view;

import com.murico.app.Murico;
import com.murico.app.config.AppSettings;
import com.murico.app.view.components.buttons.MButton;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JPanel {
    private final Murico murico;

    public MainWindow(Murico murico) {
        this.murico = murico;

        MButton btn = new MButton("HI");

        btn.setBackground(AppSettings.getInstance().getPrimaryColor());
        btn.setForeground(AppSettings.getInstance().getPrimaryForegroundColor());

        this.add(btn);
        this.setPanelSize();
    }

    public Murico getMurico() {
        return murico;
    }

    private void setPanelSize() {
        Dimension size = new Dimension(
                AppSettings.getInstance().getAppMainScreenWidth(),
                AppSettings.getInstance().getAppMainScreenHeight()
        );

        this.setPreferredSize(size);
    }
}
