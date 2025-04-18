package com.github.hanzm_10.murico.lookandfeel;

import java.util.Objects;
import java.util.logging.Logger;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import com.github.hanzm_10.murico.lookandfeel.ui.button.MuricoButtonUI;
import com.github.hanzm_10.murico.lookandfeel.ui.panel.MuricoPanelUI;
import com.github.hanzm_10.murico.os.OS;
import com.github.hanzm_10.murico.utils.MuricoLogUtils;

public class MuricoLookAndFeel extends BasicLookAndFeel {
    private static final Logger LOGGER = MuricoLogUtils.getLogger(MuricoLookAndFeel.class);

    private final LookAndFeel base;

    public MuricoLookAndFeel() {
        base = getBase();
    }

    private LookAndFeel currOrFallback(final LookAndFeel currLaf) {
        return Objects.requireNonNullElseGet(currLaf, MetalLookAndFeel::new);
    }

    private LookAndFeel getBase() {
        LookAndFeel baseLookAndFeel;

        if (OS.isWindows || OS.isLinux) {
            baseLookAndFeel = new MetalLookAndFeel();
        } else {
            final var systemLafClassName = UIManager.getSystemLookAndFeelClassName();
            final var currLaf = UIManager.getLookAndFeel();

            if (currLaf instanceof MuricoLookAndFeel) {
                baseLookAndFeel = ((MuricoLookAndFeel) currLaf).base;
            } else if (currLaf != null && systemLafClassName.equals(currLaf.getClass().getName())) {
                baseLookAndFeel = currOrFallback(currLaf);
            } else {
                try {
                    UIManager.setLookAndFeel(systemLafClassName);
                    baseLookAndFeel = currOrFallback(UIManager.getLookAndFeel());
                } catch (final Exception e) {
                    LOGGER.severe("Failed to set system look and feel: " + e.getMessage());
                    throw new IllegalStateException("Failed to set system look and feel", e);
                }
            }
        }

        return baseLookAndFeel;
    }

    @Override
    public UIDefaults getDefaults() {
        var defaults = base.getDefaults();

        defaults.put("ButtonUI", MuricoButtonUI.class.getName());
        defaults.put("ToggleButtonUI", MuricoButtonUI.class.getName());
        defaults.put("PanelUI", MuricoPanelUI.class.getName());

        return defaults;
    }

    @Override
    public String getDescription() {
        return "A custom look and feel for Murico.";
    }

    @Override
    public String getID() {
        return "muricolookandfeel";
    }

    @Override
    public String getName() {
        return "Murico Look and Feel";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return true;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

}
