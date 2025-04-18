package com.github.hanzm_10.murico.lookandfeel.ui.panel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PanelUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class MuricoPanelUI extends BasicPanelUI {
    private static PanelUI instance;

    public static ComponentUI createUI(final JComponent c) {
        if (instance == null) {
            instance = new MuricoPanelUI();
        }

        return instance;
    }
}