package com.github.hanzm_10.murico.swingapp.scenes.home.profile;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.combobox_renderers.PlaceholderRenderer;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserGender;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.Avatar;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextFieldFactory;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

record ComboBoxItem(String label, String value) {
	@Override
	public String toString() {
		return label;
	}
}

public class EditProfileScene implements Scene {

	private static Logger LOGGER = MuricoLogger.getLogger(ReadOnlyProfileScene.class);

	private AtomicBoolean isDirty = new AtomicBoolean(true);

	private JPanel view;

	private JPanel backgroundPanel;

	private RoundedPanel backgroundPanelColor;
	private JPanel contentPanel;

	private Avatar avatar;
	private JPanel namePanel;
	private JTextField displayName;
	private JLabel displayNameError;
	private JTextField realName;
	private JLabel realNameError;
	private JPanel metadataPanel;

	private JPanel rolePanel;
	private JLabel roleLabel;
	private JLabel role;
	private JPanel genderPanel;
	private JLabel genderLabel;
	private JComboBox<ComboBoxItem> gender;
	private JLabel genderError;
	private JPanel biographyPanel;
	private JLabel biographyLabel;
	private JTextArea biography;
	private JLabel biographyError;
	private JPanel buttonPanel;

	private JButton cancelButton;
	private JButton saveButton;

	private void attachComponents() {
		backgroundPanel.add(Box.createVerticalGlue(), "cell 0 0, grow");
		backgroundPanel.add(backgroundPanelColor, "cell 0 1, grow");

		namePanel.add(displayName, "width :250px:");
		namePanel.add(displayNameError);
		namePanel.add(realName, "width :250px:");
		namePanel.add(realNameError);

		var container = new JPanel(new MigLayout("insets 0", "[center]32[center]", "[center]"));

		container.setOpaque(false);

		rolePanel.add(roleLabel);
		rolePanel.add(role);

		genderPanel.add(genderLabel);
		genderPanel.add(gender, "width :250px:");
		genderPanel.add(genderError, "wrap");

		biographyPanel.add(biographyLabel, "wrap");
		biographyPanel.add(biography, "width :350px:, height :75px:");
		biographyPanel.add(biographyError, "wrap");

		container.add(rolePanel);
		container.add(genderPanel);
		metadataPanel.add(container);
		metadataPanel.add(biographyPanel);

		buttonPanel.add(cancelButton);
		buttonPanel.add(saveButton);

		contentPanel.add(avatar);
		contentPanel.add(namePanel, "grow");
		contentPanel.add(metadataPanel, "grow");
		contentPanel.add(Box.createVerticalStrut(24), "grow");
		contentPanel.add(buttonPanel, "grow");

		view.add(contentPanel);
		view.add(backgroundPanel);
	}

	@Override
	public boolean canHide() {
		return !isDirty.get();
	}

	private void clearErrorMessages() {
		displayNameError.setText("");
		realNameError.setText("");
		genderError.setText("");
		biographyError.setText("");
	}

	private void createBackgroundPanel() {
		backgroundPanel = new JPanel(new MigLayout("fill", "[grow]", "[10%!,fill][90%,fill]"));

		backgroundPanelColor = new RoundedPanel(24);
		backgroundPanelColor.setBackground(Styles.SECONDARY_COLOR);
	}

	private void createComponents() {
		createBackgroundPanel();
		createContentPanel();

		var user = SessionManager.getInstance().getLoggedInUser();

		var role = user.roles() == null ? "N/A" : user.roles().toString();

		metadataPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow, center]", "[top]"));
		metadataPanel.setOpaque(false);

		rolePanel = new JPanel(new MigLayout("insets 0", "[center]16[center]"));
		rolePanel.setOpaque(false);
		roleLabel = LabelFactory.createLabel(HtmlUtils.wrapInHtml("Role: "), 16, Styles.SECONDARY_FOREGROUND_COLOR);
		this.role = LabelFactory.createBoldItalicLabel(HtmlUtils.wrapInHtml(role), 14,
				Styles.SECONDARY_FOREGROUND_COLOR);

		genderPanel = new JPanel(new MigLayout("insets 0", "[center]16[center]"));
		genderPanel.setOpaque(false);
		genderLabel = LabelFactory.createLabel(HtmlUtils.wrapInHtml("Gender: "), 16, Styles.SECONDARY_FOREGROUND_COLOR);
		this.gender = new JComboBox<ComboBoxItem>();
		this.gender.setFont(this.gender.getFont().deriveFont(14f));
		this.gender.setRenderer(new PlaceholderRenderer<ComboBoxItem>(this.gender));
		this.gender.addItem(new ComboBoxItem("Select a gender", null));
		genderError = LabelFactory.createErrorLabel("", 9);

		var selectedIdx = 0;

		var genders = new ComboBoxItem[] { new ComboBoxItem("Male", "male"), new ComboBoxItem("Female", "female"),
				new ComboBoxItem("Non-binary", "non_binary") };

		for (var i = 0; i < genders.length; ++i) {
			var gender = genders[i];

			this.gender.addItem(gender);

			if (user.gender() != null) {
				if (UserGender.fromString(gender.value()) == user.gender()) {
					selectedIdx = i + 1;
				}
			}
		}

		this.gender.setSelectedIndex(selectedIdx);

		biographyPanel = new JPanel(new MigLayout("insets 0", "[center]16[center]"));
		biographyPanel.setOpaque(false);
		biographyLabel = LabelFactory.createLabel(HtmlUtils.wrapInHtml("Biography: "), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		this.biography = TextFieldFactory.createTextArea(14);
		this.biography.setText(user.biography() == null ? "" : user.biography());

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
		buttonPanel.setOpaque(false);
		biographyError = LabelFactory.createErrorLabel("", 9);

		saveButton = StyledButtonFactory.createButton("Save", ButtonStyles.PRIMARY);
		saveButton.setPreferredSize(new Dimension(100, 32));
		saveButton.addActionListener(this::onSaveButtonClicked);

		cancelButton = StyledButtonFactory.createButton("Cancel", ButtonStyles.SECONDARY);
		cancelButton.setPreferredSize(new Dimension(100, 32));
		cancelButton.addActionListener(this::onCancelButtonClicked);
	}

	private void createContentPanel() {
		contentPanel = new JPanel(new MigLayout("insets 16, flowy", "[grow, center]", "[top][top]32[top][top][top]"));
		contentPanel.setOpaque(false);

		avatar = new Avatar(null);
		setAvatarSize();

		loadImage();

		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		displayName = TextFieldFactory.createTextField("Display name", 18);
		displayName.setText(loggedInUser.displayName());
		displayNameError = LabelFactory.createErrorLabel("", 9);
		String fullName = null;

		var firstNameEmpty = loggedInUser.firstName() == null || loggedInUser.firstName().isBlank();
		var lastNameEmpty = loggedInUser.lastName() == null || loggedInUser.lastName().isBlank();

		if (firstNameEmpty && lastNameEmpty) {
			fullName = loggedInUser.firstName() + " " + loggedInUser.lastName();
		} else if (firstNameEmpty) {
			fullName = loggedInUser.firstName();
		} else if (lastNameEmpty) {
			fullName = loggedInUser.lastName();
		}

		realName = TextFieldFactory.createTextField("First name & Last name", 16);
		realName.setText((fullName == null || fullName.isBlank()) ? null : fullName);
		realNameError = LabelFactory.createErrorLabel("", 9);

		namePanel = new JPanel(new MigLayout("insets 0, flowy", "[grow, center]", "[top]"));
		namePanel.setOpaque(false);
	}

	private void disableButtons() {
		cancelButton.setEnabled(false);
		saveButton.setEnabled(false);
	}

	private void enableButtons() {
		cancelButton.setEnabled(true);
		saveButton.setEnabled(true);
	}

	@Override
	public String getSceneName() {
		return "edit";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private boolean isValid() {
		var isValid = true;

		if (displayName.getText().isEmpty()) {
			SwingUtilities.invokeLater(() -> {
				displayNameError.setText("Display name is required.");
			});

			isValid = false;
		} else if (displayName.getText().length() > User.MAXIMUM_USERNAME_LENGTH) {
			SwingUtilities.invokeLater(() -> {
				displayNameError
						.setText("Display name must be at most " + User.MAXIMUM_USERNAME_LENGTH + " characters.");
			});

			isValid = false;
		} else if (displayName.getText().length() < User.MINIMUM_USERNAME_LENGTH) {
			SwingUtilities.invokeLater(() -> {
				displayNameError
						.setText("Display name must be at least " + User.MINIMUM_USERNAME_LENGTH + " characters.");
			});

			isValid = false;
		}

		var splitRealName = realName.getText().split(" ");

		if (splitRealName.length > 2) {
			SwingUtilities.invokeLater(() -> {
				realNameError.setText("Real name must be 2 words at most.");
			});

			isValid = false;
		} else if (splitRealName.length != 0) {
			var firstName = splitRealName[0];
			var lastName = splitRealName.length > 1 ? splitRealName[1] : "";

			if (!firstName.isEmpty()) {
				if (firstName.length() > User.MAXIMUM_FIRSTNAME_LENGTH) {
					SwingUtilities.invokeLater(() -> {
						realNameError.setText(
								"First name must at most be " + User.MAXIMUM_FIRSTNAME_LENGTH + " characters.");
					});

					isValid = false;
				} else if (firstName.length() < User.MINIMUM_FIRSTNAME_LENGTH) {
					SwingUtilities.invokeLater(() -> {
						realNameError.setText(
								"First name must at least be " + User.MINIMUM_FIRSTNAME_LENGTH + " characters.");
					});

					isValid = false;
				}
			} else if (!lastName.isEmpty()) {
				if (lastName.length() > User.MAXIMUM_LASTNAME_LENGTH) {
					SwingUtilities.invokeLater(() -> {
						realNameError
								.setText("Last name must at most be " + User.MAXIMUM_LASTNAME_LENGTH + " characters.");
					});

					isValid = false;
				} else if (lastName.length() < User.MINIMUM_LASTNAME_LENGTH) {
					SwingUtilities.invokeLater(() -> {
						realNameError.setText(
								"Last name is must at least be " + User.MINIMUM_LASTNAME_LENGTH + " characters.");
					});

					isValid = false;
				}
			}
		}

		if (biography.getText().length() > User.MAXIMUM_BIOGRAPHY_LENGTH)

		{
			SwingUtilities.invokeLater(() -> {
				biographyError.setText("Biography must be at most " + User.MAXIMUM_BIOGRAPHY_LENGTH + " characters.");
			});

			isValid = false;
		}

		return isValid;
	}

	private void loadImage() {
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();
		var displayImageUrl = (loggedInUser.displayImage() == null || loggedInUser.displayImage().isEmpty())
				? "images/avatar_placeholder.jpg"
				: loggedInUser.displayImage();

		SwingUtilities.invokeLater(() -> {
			try {
				avatar.setImage(AssetManager.getOrLoadImage(displayImageUrl));
			} catch (IOException | InterruptedException e) {
				LOGGER.warning("Failed to load avatar image: " + e.getMessage());
				try {
					avatar.setImage(AssetManager.getOrLoadImage("images/avatar_placeholder.png"));
				} catch (IOException | InterruptedException e1) {
					LOGGER.warning("Failed to load placeholder image: " + e1.getMessage());
				}
			}

			setAvatarSize();
			view.validate();
		});
	}

	@Override
	public void onBeforeShow() {
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		var isFirstNameEmpty = loggedInUser.firstName() == null || loggedInUser.firstName().isBlank();
		var isLastNameEmpty = loggedInUser.lastName() == null || loggedInUser.lastName().isBlank();
		var realName = (isFirstNameEmpty && isLastNameEmpty) ? ""
				: loggedInUser.firstName() + " " + loggedInUser.lastName();

		var role = loggedInUser.roles() == null ? "N/A" : loggedInUser.roles().toString();
		var uGender = loggedInUser.gender() == null ? "N/A" : loggedInUser.gender().toString();
		var biography = loggedInUser.biography() == null ? "" : loggedInUser.biography();

		displayName.setText(loggedInUser.displayName());
		this.realName.setText(realName);
		this.role.setText(HtmlUtils.wrapInHtml(role));

		var selectedIdx = 0;

		for (int i = 0; i < gender.getItemCount(); ++i) {
			var g = gender.getItemAt(i);

			if (uGender.equals(g.value())) {
				selectedIdx = i;
				break;
			}
		}

		gender.setSelectedIndex(selectedIdx);
		this.biography.setText(biography);
	}

	private void onCancelButtonClicked(ActionEvent ev) {
		isDirty.set(false);

		SceneNavigator.getInstance().navigateTo("home/profile/readonly");

		SwingUtilities.invokeLater(() -> {
			isDirty.set(true);
		});
	}

	@Override
	public void onCannotHide() {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(view,
					"Please save your changes or cancel the operation before leaving this page.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		});
	}

	@Override
	public void onCreate() {
		view.setLayout(new OverlayLayout(view));

		createComponents();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		if (cancelButton != null) {
			cancelButton.removeActionListener(this::onCancelButtonClicked);
		}

		if (saveButton != null) {
			saveButton.removeActionListener(this::onSaveButtonClicked);
		}

		return true;
	}

	@Override
	public void onHide() {
	}

	private void onSaveButtonClicked(ActionEvent ev) {
		SwingUtilities.invokeLater(() -> {
			clearErrorMessages();
		});

		if (!isValid()) {
			return;
		}

		SwingUtilities.invokeLater(() -> {
			disableButtons();
		});

		updateUserInfo();
	}

	private void setAvatarSize() {
		avatar.setPreferredSize(new Dimension(160, 160));
		avatar.setSize(new Dimension(160, 160));
	}

	private void updateUserInfo() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
		var userDao = factory.getUserDao();
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		var displayName = this.displayName.getText();
		var realName = this.realName.getText();
		var splitRealName = realName.split(" ");
		var firstName = splitRealName.length == 0 ? "" : splitRealName[0];
		var lastName = splitRealName.length > 1 ? splitRealName[1] : "";
		var gender = ((ComboBoxItem) this.gender.getSelectedItem()).value();
		var biography = this.biography.getText();

		try {
			var updatedUser = userDao.updateUser(loggedInUser._userId(), displayName, firstName, lastName,
					UserGender.fromString(gender), biography);

			if (updatedUser != null) {
				SessionManager.getInstance().updateUserMetadata(updatedUser);

				isDirty.set(false);

				SceneNavigator.getInstance().navigateTo("home/profile/readonly");

				SwingUtilities.invokeLater(() -> {
					isDirty.set(true);
				});
			} else {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(view, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
				});
			}
		} catch (SQLException | IOException e) {
			LOGGER.warning("Failed to update user: " + e.getMessage());
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(view, "Failed to update user: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			});
		} finally {
			SwingUtilities.invokeLater(() -> {
				enableButtons();
			});
		}
	}

}
