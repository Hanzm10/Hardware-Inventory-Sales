package com.github.hanzm_10.murico.swingapp.scenes.home.profile;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.GradientRoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ReadProfileScene implements Scene {

	private JPanel view;
	private JLabel role;
	private JLabel nameJLabel;
	private JLabel biography;

	private JPanel container;

	@Override
	public String getSceneName() {
		return "read_profile";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));

		container = new JPanel();
		container.setBackground(new Color(0x00, true));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		var wrapper = new GradientRoundedPanel(Styles.SECONDARY_COLOR, Styles.TERTIARY_COLOR, 20);

		wrapper.setLayout(new MigLayout("insets 16 24 16 24", "[grow]", "[grow]"));

		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		role = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Role: " + loggedInUser.roles()), 18,
				Styles.SECONDARY_FOREGROUND_COLOR);
		nameJLabel = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(loggedInUser.displayName()), 32,
				Styles.SECONDARY_FOREGROUND_COLOR);
		biography = LabelFactory.createLabel(
				HtmlUtils.wrapInHtml(loggedInUser.biography() != null ? loggedInUser.biography() : ""), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);

		container.add(nameJLabel);
		container.add(Box.createVerticalStrut(12));
		container.add(role);
		container.add(Box.createVerticalStrut(4));
		container.add(biography);

		wrapper.add(container, "grow");

		view.add(wrapper, "grow");
	}

	@Override
	public void onShow() {
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		role.setText(HtmlUtils.wrapInHtml("Role: " + loggedInUser.roles()));
		nameJLabel.setText(HtmlUtils.wrapInHtml(loggedInUser.displayName()));
		biography.setText(HtmlUtils.wrapInHtml(loggedInUser.biography() != null ? loggedInUser.biography() : ""));
	}

}
