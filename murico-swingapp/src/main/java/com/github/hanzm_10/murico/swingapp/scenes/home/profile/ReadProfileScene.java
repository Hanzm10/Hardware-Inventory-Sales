package com.github.hanzm_10.murico.swingapp.scenes.home.profile;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.Avatar;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ReadProfileScene implements Scene {

	private static Logger LOGGER = MuricoLogger.getLogger(ReadProfileScene.class);

	private JPanel view;

	private JPanel backgroundPanel;
	private RoundedPanel backgroundPanelColor;

	private JPanel contentPanel;
	private Avatar avatar;
	private JPanel namePanel;
	private JLabel displayName;
	private JLabel realName;

	private JLabel role;
	private JLabel nameJLabel;
	private JLabel biography;

	private JPanel container;

	private void attachComponents() {
		backgroundPanel.add(Box.createVerticalGlue(), "cell 0 0, grow");
		backgroundPanel.add(backgroundPanelColor, "cell 0 1, grow");

		contentPanel.add(avatar);
		contentPanel.add(namePanel, "grow");

		namePanel.add(displayName);
		namePanel.add(realName);

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
		return "read_profile";
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
	public void onCreate() {
		view.setLayout(new OverlayLayout(view));

		createComponents();
		attachComponents();
	}

	@Override
	public void onShow() {
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		/*
		 * role.setText(HtmlUtils.wrapInHtml("Role: " + loggedInUser.roles()));
		 * nameJLabel.setText(HtmlUtils.wrapInHtml(loggedInUser.displayName()));
		 * biography.setText(HtmlUtils.wrapInHtml(loggedInUser.biography() != null ?
		 * loggedInUser.biography() : ""));
		 */
	}

	private void setAvatarSize() {
		avatar.setPreferredSize(new Dimension(140, 140));
		avatar.setSize(new Dimension(140, 140));
	}

}
