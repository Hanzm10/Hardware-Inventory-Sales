package com.github.hanzm_10.murico.swingapp.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPasswordField;
import javax.swing.JToggleButton;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.kitfox.svg.app.beans.SVGIcon;

public class TogglePasswordFieldVisibilityListener implements ActionListener {
	private static final Logger LOGGER = MuricoLogger.getLogger(TogglePasswordFieldVisibilityListener.class);

	private final JPasswordField passwordField;
	private final JToggleButton toggleButton;
	private SVGIcon toggledIcon;
	private SVGIcon untoggledIcon;
	private char defaultChar;

	public TogglePasswordFieldVisibilityListener(JPasswordField passwordField, JToggleButton toggleButton) {
		this.passwordField = passwordField;
		this.toggleButton = toggleButton;
		this.defaultChar = passwordField.getEchoChar();

		try {
			this.toggledIcon = AssetManager.getOrLoadIcon("icons/eye-off.svg");
			this.untoggledIcon = AssetManager.getOrLoadIcon("icons/eye.svg");

			if (toggleButton.getModel().isSelected()) {
				toggleButton.setIcon(toggledIcon);
			} else {
				toggleButton.setIcon(untoggledIcon);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error loading toggle icons", e);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (toggleButton.getModel().isSelected()) {
			toggleButton.setIcon(toggledIcon);
			passwordField.setEchoChar((char) 0);
		} else {
			toggleButton.setIcon(untoggledIcon);
			passwordField.setEchoChar(defaultChar);
		}
	}
}
