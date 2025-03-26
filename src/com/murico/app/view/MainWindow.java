package com.murico.app.view;

import com.murico.app.Murico;
import com.murico.app.config.AppSettings;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JPanel {
    private final Murico murico;

    private Dimension size;

    public MainWindow(Murico murico) {
        this.murico = murico;

        this.setPanelSize();
    }

    public Murico getMurico() {
        return murico;
    }

    private void setPanelSize() {
        this.size = new Dimension(
                AppSettings.getInstance().getAppMainScreenWidth(),
                AppSettings.getInstance().getAppMainScreenHeight()
        );

        this.setPreferredSize(this.size);
    }
}
