package com.github.hanzm_10.murico.swingapp.ui;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ErrorDialog {
	public static void showErrorDialog(Component parentComponent, String message) {
		showErrorDialog(parentComponent, message, "Error");
	}

	public static void showErrorDialog(Component parentComponent, String message, String title) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(parentComponent, title, message, JOptionPane.ERROR_MESSAGE);
		});
	}
}
