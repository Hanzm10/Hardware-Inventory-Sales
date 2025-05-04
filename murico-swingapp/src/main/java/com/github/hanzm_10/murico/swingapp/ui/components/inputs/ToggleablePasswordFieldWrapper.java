package com.github.hanzm_10.murico.swingapp.ui.components.inputs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JToggleButton;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.kitfox.svg.app.beans.SVGIcon;

/**
 */
public class ToggleablePasswordFieldWrapper implements ActionListener {
	protected JPasswordField passwordField;
	protected JToggleButton toggleButton;
	protected SVGIcon toggledIcon;
	protected SVGIcon untoggledIcon;
	protected char echoChar;

	public ToggleablePasswordFieldWrapper(JPasswordField passwordField, JToggleButton toggleButton) {
		this.passwordField = passwordField;
		this.toggleButton = toggleButton;

		try {
			this.toggledIcon = AssetManager.getOrLoadIcon("icons/eye-off.svg");
			this.untoggledIcon = AssetManager.getOrLoadIcon("icons/eye.svg");
			setButtonIcon();
		} catch (Exception e) {
			System.err.println(e);
		}

		this.echoChar = passwordField.getEchoChar();

		init();
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		setButtonIcon();

		if (toggleButton.getModel().isSelected()) {
			passwordField.setEchoChar((char) 0);
		} else {
			passwordField.setEchoChar(echoChar);
		}
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JToggleButton getToggleButton() {
		return toggleButton;
	}

	protected void init() {
		toggleButton.addActionListener(this);
		toggleButton.setPreferredSize(new Dimension(48, 48));
	}

	protected void setButtonIcon() {
		if (toggleButton.getModel().isSelected()) {
			toggleButton.setIcon(toggledIcon);
		} else {
			toggleButton.setIcon(untoggledIcon);
		}
	}
}
