package ui.buttons;

import utils.Settings;

import javax.swing.*;
import java.awt.*;

public class SecondaryButton extends JButton {
    private final Color secondaryBackgroundColor = Color.decode(Settings.get("COLOR_SECONDARY"));
    private final Color secondaryForegroundColor = Color.decode(Settings.get("COLOR_SECONDARY_FOREGROUND"));

    public SecondaryButton(String text) {
        super(text);

        this.setBackground(this.secondaryBackgroundColor);
        this.setForeground(this.secondaryForegroundColor);
    }

    public Color getSecondaryBackgroundColor() {
        return this.secondaryBackgroundColor;
    }

    public Color getSecondaryForegroundColor() {
        return this.secondaryForegroundColor;
    }
}
