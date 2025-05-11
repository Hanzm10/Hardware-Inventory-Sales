package com.github.hanzm_10.murico.swingapp.ui.components.reports;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.constants.Styles;

import net.miginfocom.swing.MigLayout;

public final class ReportsHeader {
    private JPanel container;

    public ReportsHeader() {
        container = new JPanel();
        container.setLayout(new MigLayout(""));
        container.setBackground(Styles.SECONDARY_COLOR);

        container.add(new JLabel("HII"));
    }

    public JPanel getContainer() {
        return container;
    }

    public void destroy() {}
}

