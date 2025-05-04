package com.github.hanzm_10.murico.swingapp.ui.components.inputs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.kitfox.svg.app.beans.SVGIcon;

import net.miginfocom.swing.MigLayout;

/**
 */
public class ToggleablePasswordFieldWrapper implements ActionListener {
    protected JPasswordField passwordField;
    protected JToggleButton toggleButton;
    protected SVGIcon toggledIcon;
    protected SVGIcon untoggledIcon;
    protected char echoChar;

    public ToggleablePasswordFieldWrapper(
        JPasswordField passwordField, JToggleButton toggleButton
    ) {
        this.passwordField = passwordField;
        this.toggleButton = toggleButton;

        try {
            this.toggledIcon = AssetManager.getOrLoadIcon("icons/eye-off.svg");
            this.untoggledIcon = AssetManager.getOrLoadIcon("icons/eye.svg");
            setButtonIcon();
         } catch(Exception e) {
            System.err.println(e);
        }

        this.echoChar = passwordField.getEchoChar();

        init();
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JToggleButton getToggleButton() {
        return toggleButton;
    }

    protected void setButtonIcon() {
        if (toggleButton.getModel().isSelected()) {
            toggleButton.setIcon(toggledIcon);
        } else {
            toggleButton.setIcon(untoggledIcon);
        }
    }

    protected void init() {
        toggleButton.addActionListener(this);
        toggleButton.setPreferredSize(new Dimension(48, 48));
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
}

