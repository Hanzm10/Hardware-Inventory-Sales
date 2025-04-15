/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.lookandfeel;

import java.awt.Font;
import java.util.Objects;
import java.util.logging.Logger;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import com.github.hanzm_10.murico.platform.SystemInfo;
import com.github.hanzm_10.murico.utils.MuricoLogUtils;

public class MuricoLookAndFeel extends BasicLookAndFeel {

    private static final long serialVersionUID = 9057846770190092129L;
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

        if (SystemInfo.IS_WINDOWS || SystemInfo.IS_LINUX) {
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

        var baseFont = new Font("Montserrat", Font.PLAIN, 12);
        var buttonFont = new Font("Montserrat", Font.BOLD, 12);
        var labelFont = new Font("Montserrat", Font.PLAIN, 14);

        defaults.put("defaultFont", baseFont);

        // Apply to common components
        Object[] fontDefaults = {
                "Button.font", buttonFont, "Label.font", labelFont,
                "TextField.font", baseFont,
                "TextArea.font", baseFont,
                "CheckBox.font", baseFont,
                "RadioButton.font", baseFont,
                "ComboBox.font", baseFont,
                "List.font", baseFont,
                "Table.font", baseFont,
                "Tree.font", baseFont,
                "Menu.font", baseFont,
                "MenuItem.font", baseFont,
                "TabbedPane.font", baseFont,
                "ToolTip.font", baseFont,
                "TitledBorder.font", baseFont,
                "OptionPane.messageFont", baseFont,
                "OptionPane.buttonFont", baseFont,
                // Add more as needed
        };

        defaults.putDefaults(fontDefaults);

        return defaults;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Murico Look and Feel";
    }

    @Override
    public String getID() {
        // TODO Auto-generated method stub
        return "muricolookandfeel";
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "MuricoLookAndFeel";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        // TODO Auto-generated method stub
        return true;
    }
}
