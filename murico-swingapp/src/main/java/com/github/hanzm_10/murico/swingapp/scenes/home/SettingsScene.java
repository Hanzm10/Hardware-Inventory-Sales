package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.ui.buttons.LogoutButton;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class SettingsScene implements Scene {

	private JPanel view;

	private JPanel logoutPanel;
	private JPanel logoutHeaderPanel;
	private JLabel logoutTitle;
	private JLabel logoutSubtitle;
	private JPanel logoutButtonPanel;
	private LogoutButton logoutButton;

	private void attachComponents() {
		logoutHeaderPanel.add(logoutTitle);
		logoutHeaderPanel.add(logoutSubtitle);

		logoutPanel.add(logoutHeaderPanel, "growx");
		logoutPanel.add(logoutButtonPanel, "pushx,growx");

		logoutButtonPanel.add(logoutButton);

		view.add(logoutPanel, "growx");
	}

	private void createComponents() {
		createLogoutPanel();
	}

	private void createLogoutPanel() {
		logoutPanel = new JPanel(new MigLayout("insets 0, flowx", "[grow]", "[center]"));
		logoutHeaderPanel = new JPanel(new MigLayout("insets 16, flowy", "[grow, left]", "[center]"));
		logoutTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Logout of current account"), 16);
		logoutSubtitle = LabelFactory
				.createBoldItalicLabel(
						HtmlUtils.wrapInHtml(
								"This will log you out of the current account and return you to the login screen."
										+ "You will need to enter your credentials again to log in again."),
						12, Color.GRAY);

		logoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		logoutButton = new LogoutButton();
		logoutButton.setText("Logout");
	}

	@Override
	public String getSceneName() {
		return "settings";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 16, flowy", "[grow, center]", "[top]"));

		createComponents();

		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		return true;
	}
}
