package com.github.hanzm_10.murico.swingapp.ui.labels;

import java.awt.Font;

import javax.swing.JLabel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.constants.Styles;

public class LabelFactory {
    public static JLabel createErrorLabel() {
        return createErrorLabel("");
    }

    public static JLabel createErrorLabel(@NotNull final String msg) {
        return createErrorLabel(msg, 16);
    }

    public static JLabel createErrorLabel(@NotNull final String msg, @NotNull final int fontSize) {
        var label = new JLabel();

        label.setFont(label.getFont().deriveFont(Font.BOLD, fontSize));
        label.setForeground(Styles.DANGER_COLOR);

        return label;
    }
}

