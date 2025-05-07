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
import com.github.hanzm_10.murico.swingapp.lib.exceptions.MuricoErrorCodes;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.utils.Debouncer;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.lib.validator.EmailValidator;
import com.github.hanzm_10.murico.swingapp.lib.validator.PasswordValidator;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.listeners.TogglePasswordFieldVisibilityListener;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.ImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.Line;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextFieldFactory;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextPlaceholder;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class RegisterAuthScene implements Scene, ActionListener {
	private static final Logger LOGGER = MuricoLogger.getLogger(RegisterAuthScene.class);

	/** A flag so that the components are aware whether this scene is busy or not */
	protected final AtomicBoolean isRegistering = new AtomicBoolean(false);

	protected ButtonSceneNavigatorListener navigationListener;
	protected TogglePasswordFieldVisibilityListener changePasswordVisibilityListener;

	protected JPanel view;

	protected Image leftComponentImage;
	protected RoundedImagePanel leftComponent;
	protected JPanel rightComponent;

	protected JButton backBtn;
	protected ImageIcon backBtnIcon;
	protected Image logoImage;
	protected ImagePanel logo;

	protected JLabel createAccountLabel;
	protected TextPlaceholder namePlaceholder;
	protected JTextField nameInput;
	protected JLabel errorMessageName;
	protected TextPlaceholder emailPlaceholder;
	protected JTextField emailInput;
	protected JLabel errorMessageEmail;
	protected TextPlaceholder passwordPlaceholder;
	protected JPasswordField passwordInput;
	protected JLabel errorMessagePassword;
	protected JToggleButton passwordToggleRevealButton;
	protected JButton registerBtn;
	protected JButton loginBtn;

	protected JPanel btnSeparator;
	protected Line leftLine;
	protected JLabel orText;
	protected Line rightLine;

	/** To avoid multiple calls of register */
	protected Debouncer registerDebouncer = new Debouncer(50);
	protected Thread registerThread;

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (!isRegistering.compareAndSet(false, true)) {
			return;
		}

		clearErrorMessage();

		var name = nameInput.getText();
		var email = emailInput.getText();
		var password = passwordInput.getPassword();

		if (!isInputValid(name, email, password)) {
			Arrays.fill(password, '\0');
			isRegistering.set(false);
			return;
		}

		disableNavigationButtons();
		showLoadingIndicator();

		registerThread = new Thread(() -> {
			try {
				// register
				throw new MuricoError(MuricoErrorCodes.UNSUPPORTED_OPERATION);
			} catch (MuricoError e) {
				switch (e.getErrorCode()) {
				case INVALID_CREDENTIALS: {
					SwingUtilities.invokeLater(() -> {
						errorMessageName.setText(HtmlUtils.wrapInHtml(e.getErrorCode().getDefaultMessage()));
						errorMessagePassword.setText(HtmlUtils.wrapInHtml(e.getErrorCode().getDefaultMessage()));
					});
				}
					break;
				default: {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.toString(),
							"Failed to register", JOptionPane.ERROR_MESSAGE));
					LOGGER.log(Level.SEVERE, "Failed to register", e);
				}
				}
			} finally {
				SwingUtilities.invokeLater(() -> {
					enableNavigationButtons();
					hideLoadingIndicator();
				});

				Arrays.fill(password, '\0');
				isRegistering.set(false);
			}
		});

		registerThread.start();

	}

	private void attachComponents() {
		rightComponent.add(backBtn, "cell 0 0 2");
		rightComponent.add(logo, "cell 2 0");
		rightComponent.add(createAccountLabel, "cell 0 1 3, grow");
		rightComponent.add(nameInput, "cell 0 2 3, grow");
		rightComponent.add(errorMessageName, "cell 0 3 3, grow");
		rightComponent.add(emailInput, "cell 0 4 3, grow");
		rightComponent.add(errorMessageEmail, "cell 0 5 3, grow");
		rightComponent.add(passwordInput, "cell 1 6 2, grow");
		rightComponent.add(passwordToggleRevealButton, "cell 0 6, grow");
		rightComponent.add(errorMessagePassword, "cell 0 7 3, grow");
		rightComponent.add(registerBtn, "cell 0 8 3, grow");
		rightComponent.add(btnSeparator, "cell 0 9 3, grow");
		rightComponent.add(loginBtn, "cell 0 10 3, grow");

		btnSeparator.add(leftLine, "cell 0 0, grow");
		btnSeparator.add(orText, "cell 1 0");
		btnSeparator.add(rightLine, "cell 2 0, grow");

		view.add(leftComponent);
		view.add(rightComponent);
	}

	private void attachListeners() {
		registerBtn.addActionListener(this);
		loginBtn.addActionListener(navigationListener);
		backBtn.addActionListener(navigationListener);
		passwordToggleRevealButton.addActionListener(changePasswordVisibilityListener);

		loginBtn.setActionCommand("auth/login");
		backBtn.setActionCommand("auth/main");
	}

	private void clearErrorMessage() {
		errorMessageName.setText("");
		errorMessageEmail.setText("");
		errorMessagePassword.setText("");
	}

	private void createComponents() {
		leftComponent = new RoundedImagePanel(leftComponentImage, 300);

		rightComponent = new JPanel();

		logo = new ImagePanel(logoImage);
		backBtn = StyledButtonFactory.createButton("Back ", ButtonStyles.TRANSPARENT);
		backBtn.setIcon(backBtnIcon);
		backBtn.setHorizontalTextPosition(JButton.LEFT);

		createFormComponents();
	}

	private void createFormComponents() {
		createAccountLabel = new JLabel(HtmlUtils.wrapInHtml("Create an account"));
		createAccountLabel.setFont(createAccountLabel.getFont().deriveFont(Font.BOLD, 32));

		nameInput = TextFieldFactory.createTextField();
		namePlaceholder = new TextPlaceholder("Username", nameInput);
		errorMessageName = LabelFactory.createErrorLabel("", 10);

		emailInput = TextFieldFactory.createTextField();
		emailPlaceholder = new TextPlaceholder("Email", emailInput);
		errorMessageEmail = LabelFactory.createErrorLabel("", 10);

		passwordInput = TextFieldFactory.createPasswordField();
		passwordPlaceholder = new TextPlaceholder("Password", passwordInput);
		errorMessagePassword = LabelFactory.createErrorLabel("", 10);
		passwordToggleRevealButton = new JToggleButton();

		registerBtn = StyledButtonFactory.createButton("Create account", ButtonStyles.SECONDARY);

		btnSeparator = new JPanel();
		var fractions = new float[] { 0f, 1f };
		leftLine = Line.builder().setColors(new Color[] { new Color(0x00, true), Color.BLACK }).setFractions(fractions)
				.build();
		rightLine = Line.builder().setColors(new Color[] { Color.BLACK, new Color(0x00, true) }).setFractions(fractions)
				.build();
		orText = new JLabel("or");

		loginBtn = StyledButtonFactory.createButton("Log In", ButtonStyles.SECONDARY);
	}

	private void createListeners() {
		navigationListener = new ButtonSceneNavigatorListener(isRegistering);
		changePasswordVisibilityListener = new TogglePasswordFieldVisibilityListener(passwordInput,
				passwordToggleRevealButton);
	}

	private void disableNavigationButtons() {
	}

	private void enableNavigationButtons() {
	}

	@Override
	public String getSceneName() {
		return "register";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void hideLoadingIndicator() {
	}

	private boolean isInputValid(@NotNull final String name, @NotNull final String email,
			@NotNull final char[] password) {
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

		if (email.isBlank()) {
			errorMessageEmail.setText(HtmlUtils.wrapInHtml("Email must not be empty."));
			isValid = false;
		} else if (!EmailValidator.isEmailValid(email, EmailValidator.EMAIL_REGEX)) {
			errorMessageEmail.setText(HtmlUtils.wrapInHtml("Email format not accepted."));
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
			backBtnIcon = AssetManager.getOrLoadIcon("icons/move-right.svg");
			leftComponentImage = AssetManager.getOrLoadImage("images/auth_register.png");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to load image/s", e);
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
		registerDebouncer.cancel();

		if (registerThread != null) {
			registerThread.interrupt();
		}

		namePlaceholder.destroy();
		emailPlaceholder.destroy();
		passwordPlaceholder.destroy();

		registerBtn.removeActionListener(this);
		loginBtn.removeActionListener(navigationListener);
		backBtn.removeActionListener(navigationListener);
		passwordToggleRevealButton.removeActionListener(changePasswordVisibilityListener);

		return true;
	}

	@Override
	public void onHide() {
		registerDebouncer.cancel();
	}

	private void setLayouts() {
		view.setLayout(new MigLayout("", "[390px::560px,grow,right]24[290px::424px,grow,left]", "[grow,center]"));
		rightComponent.setLayout(new MigLayout("", "[48px::48px,left][280px,left][72px::96px,right]",
				"[72px::96px]32[]16[48px::]2[]12[48px::]2[]12[48px::]2[]20[48px::]12[12px::]24[48px::]"));
		btnSeparator.setLayout(new MigLayout("", "[grow][][grow]", "[center]"));
	}

	private void showLoadingIndicator() {
	}
}
