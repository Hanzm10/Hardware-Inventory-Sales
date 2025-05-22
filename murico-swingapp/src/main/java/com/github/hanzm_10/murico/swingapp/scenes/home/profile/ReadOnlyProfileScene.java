package com.github.hanzm_10.murico.swingapp.scenes.home.profile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.Avatar;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ReadOnlyProfileScene implements Scene {

	private static Logger LOGGER = MuricoLogger.getLogger(ReadOnlyProfileScene.class);

	private JPanel view;

	private JPanel backgroundPanel;
	private RoundedPanel backgroundPanelColor;

	private JPanel contentPanel;
	private Avatar avatar;
	private JPanel namePanel;
	private JLabel displayName;
	private JLabel realName;

	private JPanel metadataPanel;
	private JPanel rolePanel;
	private JLabel roleLabel;
	private JLabel role;
	private JPanel genderPanel;
	private JLabel genderLabel;
	private JLabel gender;
	private JPanel biographyPanel;
	private JLabel biographyLabel;
	private JLabel biography;

	private JPanel buttonPanel;
	private JButton editButton;

	private void attachComponents() {
		backgroundPanel.add(Box.createVerticalGlue(), "cell 0 0, grow");
		backgroundPanel.add(backgroundPanelColor, "cell 0 1, grow");

		namePanel.add(displayName);
		namePanel.add(realName);

		var container = new JPanel(new MigLayout("insets 0", "[center]32[center]", "[center]"));

		container.setOpaque(false);

		rolePanel.add(roleLabel);
		rolePanel.add(role);

		genderPanel.add(genderLabel);
		genderPanel.add(gender);

		biographyPanel.add(biographyLabel, "wrap");
		biographyPanel.add(biography);

		container.add(rolePanel);
		container.add(genderPanel);
		metadataPanel.add(container);
		metadataPanel.add(biographyPanel);

		buttonPanel.add(editButton);

		contentPanel.add(avatar);
		contentPanel.add(namePanel, "grow");
		contentPanel.add(metadataPanel, "grow");
		contentPanel.add(Box.createVerticalStrut(24), "grow");
		contentPanel.add(buttonPanel, "grow");

		view.add(contentPanel);
		view.add(backgroundPanel);
	}

	private void createBackgroundPanel() {
		backgroundPanel = new JPanel(new MigLayout("fill", "[grow]", "[10%!,fill][90%,fill]"));

		backgroundPanelColor = new RoundedPanel(24);
		backgroundPanelColor.setBackground(Styles.SECONDARY_COLOR);
	}

	private void createComponents() {
		createBackgroundPanel();
		createContentPanel();

		metadataPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow, center]", "[top]16[top]"));
		metadataPanel.setOpaque(false);

		var user = SessionManager.getInstance().getLoggedInUser();

		var role = user.roles() == null ? "N/A" : user.roles().toString();
		var gender = user.gender() == null ? "N/A" : user.gender().toString();
		var biography = user.biography() == null ? "N/A" : user.biography();

		rolePanel = new JPanel(new MigLayout("insets 0", "[center]16[center]"));
		rolePanel.setOpaque(false);
		roleLabel = LabelFactory.createLabel(HtmlUtils.wrapInHtml("Role: "), 20, Styles.SECONDARY_FOREGROUND_COLOR);
		this.role = LabelFactory.createBoldItalicLabel(HtmlUtils.wrapInHtml(role), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);

		genderPanel = new JPanel(new MigLayout("insets 0", "[center]16[center]"));
		genderPanel.setOpaque(false);
		genderLabel = LabelFactory.createLabel(HtmlUtils.wrapInHtml("Gender: "), 20, Styles.SECONDARY_FOREGROUND_COLOR);
		this.gender = LabelFactory.createBoldItalicLabel(HtmlUtils.wrapInHtml(gender), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);

		biographyPanel = new JPanel(new MigLayout("insets 0", "[center]16[center]"));
		biographyPanel.setOpaque(false);
		biographyLabel = LabelFactory.createLabel(HtmlUtils.wrapInHtml("Biography: "), 20,
				Styles.SECONDARY_FOREGROUND_COLOR);
		this.biography = LabelFactory.createBoldItalicLabel(HtmlUtils.wrapInHtml(biography), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setOpaque(false);

		editButton = new JButton("Edit Profile");
		editButton.setPreferredSize(new Dimension(160, 40));
		editButton.addActionListener(this::onEditButtonClicked);
	}

	private void createContentPanel() {
		contentPanel = new JPanel(new MigLayout("insets 16, flowy", "[grow, center]", "[top]"));
		contentPanel.setOpaque(false);

		avatar = new Avatar(null);
		setAvatarSize();

		loadImage();

		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		displayName = LabelFactory.createLabel(loggedInUser.displayName(), 64, Styles.SECONDARY_FOREGROUND_COLOR);
		var fullName = "";

		if (loggedInUser.firstName() != null && loggedInUser.lastName() != null) {
			fullName = loggedInUser.firstName() + " " + loggedInUser.lastName();
		} else if (loggedInUser.firstName() != null) {
			fullName = loggedInUser.firstName();
		} else if (loggedInUser.lastName() != null) {
			fullName = loggedInUser.lastName();
		}

		realName = LabelFactory.createLabel(fullName, 32, Color.GRAY);

		displayName.setHorizontalTextPosition(JLabel.CENTER);
		realName.setHorizontalTextPosition(JLabel.CENTER);

		namePanel = new JPanel(new MigLayout("insets 0, flowy", "[grow, center]", "[top]"));
		namePanel.setOpaque(false);
	}

	@Override
	public String getSceneName() {
		return "readonly";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
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
		var user = SessionManager.getInstance().getLoggedInUser();

		if (user == null) {
			LOGGER.warning("User is null");
			return;
		}

		var displayName = user.displayName() == null ? "N/A" : user.displayName();
		var firstName = user.firstName() == null ? "" : user.firstName();
		var lastName = user.lastName() == null ? "" : user.lastName();
		var gender = user.gender() == null ? "N/A" : user.gender().toString();
		var biography = (user.biography() == null || user.biography().isBlank()) ? "N/A" : user.biography();

		this.displayName.setText(HtmlUtils.wrapInHtml(displayName));
		this.realName.setText(HtmlUtils.wrapInHtml(firstName + " " + lastName));
		this.gender.setText(HtmlUtils.wrapInHtml(gender));
		this.biography.setText(HtmlUtils.wrapInHtml(biography));
	}

	@Override
	public void onCreate() {
		view.setLayout(new OverlayLayout(view));

		createComponents();
		attachComponents();
	}

	private void onEditButtonClicked(ActionEvent ev) {
		SceneNavigator.getInstance().navigateTo("home/profile/edit");
	}

	@Override
	public void onShow() {
	}

	private void setAvatarSize() {
		avatar.setPreferredSize(new Dimension(160, 160));
		avatar.setSize(new Dimension(160, 160));
	}

}
