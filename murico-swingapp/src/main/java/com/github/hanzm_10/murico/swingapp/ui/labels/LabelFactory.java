package com.github.hanzm_10.murico.swingapp.ui.labels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.UIManager;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.constants.Styles;

public class LabelFactory {
	public static JLabel createBoldItalicLabel(@NotNull final int fontSize) {
		return createBoldItalicLabel("", fontSize, UIManager.getColor("foreground"));
	}

	public static JLabel createBoldItalicLabel(@NotNull final int fontSize, @NotNull final Color color) {
		return createBoldItalicLabel("", fontSize, color);
	}

	public static JLabel createBoldItalicLabel(@NotNull final String msg) {
		return createBoldItalicLabel(msg, 16, null);
	}

	public static JLabel createBoldItalicLabel(@NotNull final String msg, @NotNull final int fontSize) {
		return createBoldItalicLabel(msg, fontSize, null);
	}

	public static JLabel createBoldItalicLabel(@NotNull final String msg, @NotNull final int fontSize,
			@NotNull Color color) {
		var label = new JLabel(msg);

		label.setFont(label.getFont().deriveFont(Font.BOLD | Font.ITALIC, fontSize));

		if (color != null) {
			label.setForeground(color);
		}

		return label;
	}

	public static JLabel createBoldLabel() {
		return createBoldLabel("");
	}

	public static JLabel createBoldLabel(@NotNull final String msg) {
		return createBoldLabel(msg, 16, null);
	}

	public static JLabel createBoldLabel(@NotNull final String msg, @NotNull final int fontSize) {
		return createBoldLabel(msg, fontSize, null);
	}

	public static JLabel createBoldLabel(@NotNull final String msg, @NotNull final int fontSize, final Color color) {
		var label = new JLabel(msg);

		label.setFont(label.getFont().deriveFont(Font.BOLD, fontSize));

		if (color != null) {
			label.setForeground(color);
		}

		return label;
	}

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

	public static JLabel createLabel() {
		return createLabel("");
	}

	public static JLabel createLabel(@NotNull final String msg) {
		return createLabel(msg, 16);
	}

	public static JLabel createLabel(@NotNull final String msg, @NotNull final int fontSize) {
		return createLabel(msg, fontSize, null);
	}

	public static JLabel createLabel(@NotNull final String msg, @NotNull final int fontSize, @NotNull Color color) {
		var label = new JLabel(msg);

		label.setFont(label.getFont().deriveFont((float) fontSize));

		if (color != null) {
			label.setForeground(color);
		}

		return label;
	}
}
