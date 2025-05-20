package com.github.hanzm_10.murico.swingapp.scenes.home.profile;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ReadOnlyScene implements Scene {
	private JLabel displayRoleLbl;
	private RoundedPanel personalDetailsPnl;
	private JLabel profilepicLbl;
	private JLabel displaynameLbl;
	private JLabel profileLogoLbl;
	private JButton editProfBtn;
	private JLabel profileLbl;
	private boolean uiInitialized = false;
	private JPanel view;
	private Image image;
	private SceneManager sceneManager;
	private String username;
	private JLabel namelbl;
	private JLabel genderlbl;
	private JPanel userinfoPnl;
	private String fullName;
	private String gender;
	private String role;

	public ReadOnlyScene() {
	}

	@Override
	public String getSceneName() {
		return "readonly";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new RoundedPanel(20)) : view;

	}

	private void initializeProfileUI() throws IOException, InterruptedException {
		refreshUI();
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();
		fullName = loggedInUser.firstName() + " " + loggedInUser.lastName();
		gender = loggedInUser.gender().toString();
		username = loggedInUser.displayName();
		role = loggedInUser.roles();

		Profile profile = new Profile();
		view.setLayout(new MigLayout("fill", "[250][grow][grow]", "[grow][grow][grow][grow][grow]"));
		personalDetailsPnl = new RoundedPanel(20);

		namelbl = LabelFactory.createBoldLabel("", 18, Color.WHITE);
		personalDetailsPnl.add(namelbl, "cell 0 0,alignx center,aligny top");

		genderlbl = LabelFactory.createBoldLabel("Gender: " + gender.toUpperCase(), 18, Color.WHITE);
		personalDetailsPnl.add(genderlbl, "cell 0 1,alignx center,aligny top");

		displayRoleLbl = LabelFactory.createBoldLabel("Role: " + role.toUpperCase(), 18, Color.WHITE);
		displayRoleLbl.setForeground(Color.WHITE);
		personalDetailsPnl.add(displayRoleLbl, "cell 0 2,alignx center,aligny top");

		view.setBackground(Styles.SECONDARY_COLOR);
		personalDetailsPnl.setBackground(Styles.PRIMARY_COLOR);
		personalDetailsPnl.setAlignmentX(Component.CENTER_ALIGNMENT);
		personalDetailsPnl.setLayout(new MigLayout("wrap", "[grow,center]", "[][][][]"));
		view.add(personalDetailsPnl, "cell 1 4, growx, aligny top");

		profilepicLbl = new JLabel("");
		profilepicLbl.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profilepic.png")));
		view.add(profilepicLbl, "cell 1 2,alignx center,growy");

		displaynameLbl = LabelFactory.createBoldLabel(username.toUpperCase(), 64, Color.WHITE);
		displaynameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

		view.add(displaynameLbl, "cell 1 3,alignx center,aligny top");

		editProfBtn = new JButton("Edit Profile");
		editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		view.add(editProfBtn, "cell 2 0,alignx right,growy");

		editProfBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SceneNavigator.getInstance().navigateTo("home/profile/edit");

			}
		});

	}

	@Override
	public void onCreate() {
		if (!uiInitialized) {
			try {
				initializeProfileUI();
				uiInitialized = true;
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onDestroy() {
		uiInitialized = false;
		username = null;

		return true;
	}

	@Override
	public void onHide() {
		System.out.println(getSceneName() + ": onHide");
		username = null;
		fullName = null;
		gender = null;
		role = null;
		namelbl = null;
		genderlbl = null;

	}

	@Override
	public void onShow() {
		System.out.println(getSceneName() + ": onShow");
		if (!uiInitialized) {
			try {
				initializeProfileUI();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			refreshUI();
		}
	}

	public void refreshUI() {
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		fullName = loggedInUser.firstName() + " " + loggedInUser.lastName();
		gender = loggedInUser.gender().toString();
		username = loggedInUser.displayName();
		role = loggedInUser.roles();

		if (loggedInUser.firstName() == null || loggedInUser.lastName() == null || "Unknown".equalsIgnoreCase(gender)) {
			fullName = "Set name";
			gender = "Set gender";
		}

		if (namelbl != null) {
			namelbl.setText(fullName.toUpperCase());
		}
		if (genderlbl != null) {
			genderlbl.setText(gender.toUpperCase());
		}
		if (displaynameLbl != null) {
			displaynameLbl.setText(HtmlUtils.wrapInHtml(username.toUpperCase()));
		}
		if (displayRoleLbl != null) {
			displayRoleLbl.setText(role.toUpperCase());
		}

		// Force the view to re-render
		if (view != null) {
			view.revalidate();
			view.repaint();
		}
	}

}
