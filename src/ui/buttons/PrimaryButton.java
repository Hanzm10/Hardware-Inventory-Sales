package ui.buttons;

import utils.Settings;

import javax.swing.*;
import java.awt.*;

public class PrimaryButton extends JButton {
    private final Color primaryBackgroundColor = Color.decode(Settings.get("COLOR_PRIMARY"));
    private final Color primaryForegroundColor = Color.decode(Settings.get("COLOR_PRIMARY_FOREGROUND"));

    public PrimaryButton(String text) {
        super(text);

        this.setBackground(this.primaryBackgroundColor);
        this.setForeground(this.primaryForegroundColor);
    }

    public Color getPrimaryBackgroundColor() {
        return this.primaryBackgroundColor;
    }

    public Color getPrimaryForegroundColor() {
        return this.primaryForegroundColor;
    }
}
