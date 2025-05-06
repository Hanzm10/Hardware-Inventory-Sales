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

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.lib.exceptions.MuricoError;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.utils.Debouncer;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.lib.validator.PasswordValidator;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.listeners.TogglePasswordFieldVisibilityListener;
import com.github.hanzm_10.murico.swingapp.service.database.SessionService;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.ImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.Line;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextFieldFactory;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextPlaceholder;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class LoginAuthScene implements Scene, ActionListener {
	private static final Logger LOGGER = MuricoLogger.getLogger(LoginAuthScene.class);

	/** A flag so that the components are aware whether this scene is busy or not */
	protected final AtomicBoolean isLoggingIn = new AtomicBoolean(false);

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
	protected Line leftLine;
	protected JLabel orText;
	protected Line rightLine;

	/** To avoid multiple calls of login */
	protected Debouncer loginDebouncer = new Debouncer(50);

	protected Thread loginThread;

	@Override
	public void actionPerformed(ActionEvent ev) {
		loginDebouncer.call(this::tryLogin);
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

		btnSeparator.add(leftLine, "cell 0 0, grow");
		btnSeparator.add(orText, "cell 1 0");
		btnSeparator.add(rightLine, "cell 2 0, grow");

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

	private void clearErrorMessage() {
		errorMessageName.setText("");
		errorMessagePassword.setText("");
	}

	private void createComponents() {
		leftComponent = new JPanel();

		logo = new ImagePanel(logoImage);
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
		errorMessageName = LabelFactory.createErrorLabel("", 10);

		passwordInput = TextFieldFactory.createPasswordField();
		passwordPlaceholder = new TextPlaceholder("Password", passwordInput);
		errorMessagePassword = LabelFactory.createErrorLabel("", 10);
		passwordToggleRevealButton = new JToggleButton();

		loginBtn = StyledButtonFactory.createButton("Log In", ButtonStyles.SECONDARY);

		btnSeparator = new JPanel();
		var fractions = new float[] { 0f, 1f };
		leftLine = Line.builder().setColors(new Color[] { new Color(0x00, true), Color.BLACK }).setFractions(fractions)
				.build();
		rightLine = Line.builder().setColors(new Color[] { Color.BLACK, new Color(0x00, true) }).setFractions(fractions)
				.build();
		orText = new JLabel("or");

		registerBtn = StyledButtonFactory.createButton("Create an account", ButtonStyles.SECONDARY);
	}

	private void createListeners() {
		navigationListener = new ButtonSceneNavigatorListener(isLoggingIn);
		changePasswordVisibilityListener = new TogglePasswordFieldVisibilityListener(passwordInput,
				passwordToggleRevealButton);
	}

	private void disableNavigationButtons() {
		backBtn.setEnabled(false);
		registerBtn.setEnabled(false);
	}

	private void enableNavigationButtons() {
		backBtn.setEnabled(true);
		registerBtn.setEnabled(true);
	}

	@Override
	public String getSceneName() {
		return "login";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void hideLoadingIndicator() {
		loginBtn.setText("Log In");
	}

	private boolean isInputValid(@NotNull final String name, @NotNull final char[] password) {
		var isValid = true;

		if (name.isBlank()) {
			errorMessageName.setText(HtmlUtils.wrapInHtml("Username must not be empty"));
			isValid = false;
		} else if (name.length() < User.MINIMUM_USERNAME_LENGTH) {
			errorMessageName.setText(
					HtmlUtils.wrapInHtml("Username must be > " + User.MINIMUM_USERNAME_LENGTH + " characters long."));
			isValid = false;
		} else if (name.length() > User.MAXIMUM_USERNAME_LENGTH) {
			errorMessageName.setText(
					HtmlUtils.wrapInHtml("Username must be < " + User.MAXIMUM_USERNAME_LENGTH + " characters long."));
			isValid = false;
		}

		if (!PasswordValidator.isPasswordValid(password, PasswordValidator.STRONG_PASSWORD)) {
			errorMessagePassword.setText(HtmlUtils.wrapInHtml(PasswordValidator.STRONG_PASSWORD_ERROR_MESSAGE));
			isValid = false;
		}

		return isValid;
	}

	private void loadImages() {
		try {
			logoImage = AssetManager.getOrLoadImage("images/logo.png");
			rightComponentImage = AssetManager.getOrLoadImage("images/auth_login.png");
			backBtnIcon = AssetManager.getOrLoadIcon("icons/move-left.svg");
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
		loginDebouncer.cancel();

		if (loginThread != null) {
			loginThread.interrupt();
		}

		namePlaceholder.destroy();
		passwordPlaceholder.destroy();

		loginBtn.removeActionListener(this);
		registerBtn.removeActionListener(navigationListener);
		backBtn.removeActionListener(navigationListener);
		passwordToggleRevealButton.removeActionListener(changePasswordVisibilityListener);

		return true;
	}

	@Override
	public void onHide() {
		loginDebouncer.cancel();
	}

	private void setLayouts() {
		view.setLayout(new MigLayout("", "[290px::424px,grow,right]24[390px::560px,grow,left]", "[grow,center]"));
		leftComponent.setLayout(new MigLayout("", "[72px::96px,left][280px,right][48px::,right]",
				"[72px::96px]32[]16[50px::]2[]12[48px::]2[]20[48px::]8[12px::]24[48px::]"));
		btnSeparator.setLayout(new MigLayout("", "[grow][][grow]", "[center]"));
	}

	private void showLoadingIndicator() {
		loginBtn.setText("Logging in...");
	}

	private void tryLogin() {
		if (!isLoggingIn.compareAndSet(false, true)) {
			return;
		}

		clearErrorMessage();

		var name = nameInput.getText();
		var password = passwordInput.getPassword();

		if (!isInputValid(name, password)) {
			Arrays.fill(password, '\0');
			isLoggingIn.set(false);
			return;
		}

		disableNavigationButtons();
		showLoadingIndicator();

		loginThread = new Thread(() -> {
			try {
				SessionService.loginUser(name, password);

				SwingUtilities.invokeLater(() -> {
					SceneNavigator.navigateTo("home/profile");
				});
			} catch (MuricoError e) {
				switch (e.getErrorCode()) {
				case INVALID_CREDENTIALS: {
					errorMessageName.setText(HtmlUtils.wrapInHtml(e.getErrorCode().getDefaultMessage()));
					errorMessagePassword.setText(HtmlUtils.wrapInHtml(e.getErrorCode().getDefaultMessage()));
				}
					break;
				default: {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.toString(),
							"Failed to log in", JOptionPane.ERROR_MESSAGE));
					LOGGER.log(Level.SEVERE, "Failed to log in", e);
				}
				}
			} finally {
				SwingUtilities.invokeLater(() -> {
					enableNavigationButtons();
					hideLoadingIndicator();
				});

				Arrays.fill(password, '\0');
				isLoggingIn.set(false);
			}
		});

		loginThread.start();
	}
}
