package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserMetadata;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.Avatar;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ReadOnlyScene implements Scene {
	private JLabel displayRoleLbl;
	private RoundedPanel personalDetailsPnl;
	private Avatar profilepicLbl;
	private JLabel displaynameLbl;
	private JButton editProfBtn;
	private JLabel namelbl;
	private JLabel genderlbl;
	private JPanel view;
	private String fullName;
	private String gender;
	private String role;
	private UserMetadata loggedInUser;
	private Profile profile;
	private String displayImageString;

	private boolean uiInitialized = false;

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
		view.setLayout(new MigLayout("fill", "[250][250][250]", "[30][grow][250][grow][grow]"));
		personalDetailsPnl = new RoundedPanel(20);

		namelbl = LabelFactory.createBoldLabel("", 18, Color.WHITE);
		personalDetailsPnl.add(namelbl, "cell 0 0,alignx center,aligny top");

		genderlbl = LabelFactory.createBoldLabel("", 18, Color.WHITE);
		personalDetailsPnl.add(genderlbl, "cell 0 1,alignx center,aligny top");

		displayRoleLbl = LabelFactory.createBoldLabel("", 18, Color.WHITE);
		personalDetailsPnl.add(displayRoleLbl, "cell 0 2,alignx center,aligny top");

		view.setBackground(Styles.SECONDARY_COLOR);
		personalDetailsPnl.setBackground(Styles.PRIMARY_COLOR);
		personalDetailsPnl.setAlignmentX(Component.CENTER_ALIGNMENT);
		personalDetailsPnl.setLayout(new MigLayout("wrap", "[grow,center]", "[][][][]"));
		view.add(personalDetailsPnl, "cell 1 4, growx, aligny top");

		/*
		 * System.out.println("Display image string: " + displayImageString);
		 * profilepicLbl = new Avatar(AssetManager.getOrLoadImage(displayImageString));
		 * view.add(profilepicLbl, "cell 1 2,alignx center");
		 */
		displaynameLbl = LabelFactory.createBoldLabel("", 64, Color.WHITE);
		view.add(displaynameLbl, "cell 1 3,alignx center,aligny top");

		editProfBtn = new JButton("Edit Profile");
		editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		view.add(editProfBtn, "cell 2 0,alignx right,growy");

		editProfBtn.addActionListener(e -> SceneNavigator.getInstance().navigateTo("home/profile/edit"));
	}

	@Override
	public void onCreate() {
		if (!uiInitialized) {
			try {
				initializeProfileUI();
				uiInitialized = true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onDestroy() {
		uiInitialized = false;
		return true;
	}

	@Override
	public void onHide() {
		fullName = null;
		gender = null;
		role = null;
	}

	@Override
	public void onShow() {
		refreshUI();
	}

	public void refreshUI() {
		profile = new Profile();
		loggedInUser = SessionManager.getInstance().getLoggedInUser();

		String firstName = loggedInUser.firstName();
		String lastName = loggedInUser.lastName();
		displayImageString = loggedInUser.displayImage();
		fullName = firstName + " " + lastName;
		gender = loggedInUser.gender().toString();
		role = loggedInUser.roles();

		if (firstName == null || lastName == null || "Unknown".equalsIgnoreCase(gender)) {
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
			displaynameLbl.setText(loggedInUser.displayName().toUpperCase());
		}
		if (displayRoleLbl != null) {
			displayRoleLbl.setText(role.toUpperCase());
		}

		// Update profilepicLbl
		if (profilepicLbl != null) {
			view.remove(profilepicLbl); // Remove the old label
		}
		try {
			profilepicLbl = new Avatar(AssetManager.getOrLoadImage(displayImageString));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Create a new Avatar
		view.add(profilepicLbl, "cell 1 2,alignx center"); // Add the new label

		if (view != null) {
			view.revalidate(); // Revalidate the panel
			view.repaint(); // Repaint the panel
		}
	}

}