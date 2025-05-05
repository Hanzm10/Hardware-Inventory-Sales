/**
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.scenes.auth;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.listeners.TogglePasswordFieldVisibilityListener;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.ImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextFieldFactory;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextPlaceholder;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class LoginAuthScene implements Scene, ActionListener {
	private static final Logger LOGGER = MuricoLogger.getLogger(LoginAuthScene.class);

	/** A flag so that the components are aware whether this scene is busy or not */
	protected boolean isLoggingIn;

	protected ButtonSceneNavigatorListener navigationListener;
	protected TogglePasswordFieldVisibilityListener changePasswordVisibilityListener;

	protected JPanel view;

	protected JPanel leftComponent;
	protected Image rightComponentImage;
	protected RoundedImagePanel rightComponent;

	protected Image logoImage;
	protected ImagePanel logo;
	protected ImageIcon backBtnIcon;
	protected JButton backBtn;

	protected JLabel signInLabel;
	protected TextPlaceholder namePlaceholder;
	protected JTextField nameInput;
	protected JLabel errorMessageName;
	protected TextPlaceholder passwordPlaceholder;
	protected JPasswordField passwordInput;
	protected JToggleButton passwordToggleRevealButton;
	protected JLabel errorMessagePassword;
	protected JButton loginBtn;
	protected JButton registerBtn;
	protected JPanel btnSeparator;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (isLoggingIn) {
			return;
		}

		isLoggingIn = true;

		try {
			loginBtn.setIcon(AssetManager.getOrLoadIcon("icons/pulse-rings-3.svg"));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void attachComponents() {
		leftComponent.add(logo, "cell 0 0");
		leftComponent.add(backBtn, "cell 1 0 2");
		leftComponent.add(signInLabel, "cell 0 1 3, grow");
		leftComponent.add(nameInput, "cell 0 2 3, grow");
		leftComponent.add(errorMessageName, "cell 0 3 3, grow");
		leftComponent.add(passwordInput, "cell 0 4 2, grow");
		leftComponent.add(passwordToggleRevealButton, "cell 2 4, grow");
		leftComponent.add(errorMessagePassword, "cell 0 5 3, grow");
		leftComponent.add(loginBtn, "cell 0 6 3, grow");
		leftComponent.add(btnSeparator, "cell 0 7 3, grow");
		leftComponent.add(registerBtn, "cell 0 8 3, grow");

		view.add(leftComponent);
		view.add(rightComponent);
	}

	private void attachListeners() {
		// call login controller, loading indicator, etc.
		loginBtn.addActionListener(this);
		registerBtn.addActionListener(navigationListener);
		backBtn.addActionListener(navigationListener);
		passwordToggleRevealButton.addActionListener(changePasswordVisibilityListener);

		registerBtn.setActionCommand("auth/register");
		backBtn.setActionCommand("auth/main");
	}

	private void createComponents() {
		leftComponent = new JPanel();
		leftComponent.setPreferredSize(new Dimension(720, 560));

		logo = new ImagePanel(logoImage);
		logo.setPreferredSize(new Dimension(96, 96));
		backBtn = StyledButtonFactory.createButton(" Back", ButtonStyles.TRANSPARENT);
		backBtn.setIcon(backBtnIcon);

		createFormComponents();

		rightComponent = new RoundedImagePanel(rightComponentImage, 300);
	}

	private void createFormComponents() {
		signInLabel = new JLabel("Sign in");
		signInLabel.setFont(signInLabel.getFont().deriveFont(Font.BOLD, 48));

		nameInput = TextFieldFactory.createTextField();
		namePlaceholder = new TextPlaceholder("Username", nameInput);
		errorMessageName = LabelFactory.createErrorLabel("", 8);

		passwordInput = TextFieldFactory.createPasswordField();
		passwordPlaceholder = new TextPlaceholder("Password", passwordInput);
		errorMessagePassword = LabelFactory.createErrorLabel("", 8);
		passwordToggleRevealButton = new JToggleButton();

		loginBtn = StyledButtonFactory.createButton("Log In", ButtonStyles.SECONDARY);

		btnSeparator = new JPanel();

		registerBtn = StyledButtonFactory.createButton("Create an account", ButtonStyles.SECONDARY);
	}

	private void createListeners() {
		navigationListener = new ButtonSceneNavigatorListener(() -> isLoggingIn);
		changePasswordVisibilityListener = new TogglePasswordFieldVisibilityListener(passwordInput,
				passwordToggleRevealButton);
	}

	@Override
	public String getSceneName() {
		return "login";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void loadImages() {
		try {
			logoImage = AssetManager.getOrLoadImage("images/logo.png");
			rightComponentImage = AssetManager.getOrLoadImage("images/auth_login.png");
			backBtnIcon = AssetManager.getOrLoadIcon("icons/arrow-left.svg");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error loading image", e);
		}
	}

	@Override
	public void onCreate() {
		loadImages();
		createComponents();
		createListeners();
		setLayouts();
		attachListeners();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		namePlaceholder.destroy();
		passwordPlaceholder.destroy();

		loginBtn.removeActionListener(this);
		registerBtn.removeActionListener(navigationListener);
		backBtn.removeActionListener(navigationListener);
		passwordToggleRevealButton.removeActionListener(changePasswordVisibilityListener);

		return true;
	}

	private void setLayouts() {
		view.setLayout(new MigLayout("", "[300px::424px,grow,right]16[500px::540px,grow,left]", "[grow]"));
		leftComponent.setLayout(new MigLayout("", "[254px,left]0[122px,right]0[48px::,right]",
				"[72px::]32[]16[50px::]2[]12[48px::]2[]20[48px::]12[32px::]24[48px::]"));
	}
}
