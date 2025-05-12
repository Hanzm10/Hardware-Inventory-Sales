package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;

import net.miginfocom.swing.MigLayout;

public class ProfileScene implements Scene {

	private JLabel displayRoleLbl;
	private JLabel roleLbl;
	private JLabel profilepicLbl;
	private JLabel displaynameLbl;
	private JLabel profileLogoLbl;
	private JButton editProfBtn;
	private JLabel profileLbl;
	private boolean uiInitialized = false;
	private JPanel view;
	private Image image;

	public ProfileScene() {
		// setLayout(new MigLayout());
		System.out.println("Profile Scene instance created.");

	}

	@Override
	public String getSceneName() {
		// TODO Auto-generated method stub
		return "profile";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;

	}

	private void initializeProfileUI() throws IOException, InterruptedException {
		Profile profile = new Profile();
		view.setBackground(Color.WHITE);

		view.setLayout(new MigLayout("", "[742px][67px][371px]", "[71px][62px][45px][91px][12px][84px][36px][456px]"));

		displayRoleLbl = new JLabel();
		displayRoleLbl.setForeground(Color.WHITE);
		// displayRoleLbl.setText(profile.getRole(username));
		displayRoleLbl.setFont(new Font("Montserrat", Font.BOLD, 20));
		displayRoleLbl.setOpaque(false);
		view.add(displayRoleLbl);
		view.add(displayRoleLbl, "cell 0 7,alignx right,aligny top");

		roleLbl = new JLabel("");
		image = AssetManager.getOrLoadImage("images/roleBoarderLabel.png");
		roleLbl.setIcon(new ImageIcon(image));
		roleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		roleLbl.setBounds(497, 425, 274, 56);
		view.add(roleLbl, "cell 0 7,alignx right,aligny top");

		profilepicLbl = new JLabel("");
		profilepicLbl.setBackground(new Color(33, 64, 107));
		profilepicLbl.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profilepic.png")));

		profilepicLbl.setBounds(497, 64, 233, 229);
		view.add(profilepicLbl, "cell 0 0 1 4,alignx right,aligny bottom");

		displaynameLbl = new JLabel();
		displaynameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
		displaynameLbl.setForeground(Color.WHITE);
		displaynameLbl.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		// displaynameLbl.setText(username.toUpperCase());
		displaynameLbl.setFont(new Font("Montserrat", Font.BOLD, 64));
		displaynameLbl.setOpaque(false);
		view.add(displaynameLbl, "cell 0 5 3 1,alignx center,growy");

		profileLogoLbl = new JLabel("");
		profileLogoLbl.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profileRectangle.png")));
		view.add(profileLogoLbl, "cell 0 3 3 5,grow");
		// profilePnl.setLayout(null);

		editProfBtn = new JButton("");
		editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editProfBtn.setBorderPainted(false);
		editProfBtn.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/editProf.png")));
		view.add(editProfBtn, "cell 2 1,alignx left,growy");

		profileLbl = new JLabel();
		// ProfileTxt.setEditable(false);
		profileLbl.setFont(new Font("Montserrat", Font.BOLD | Font.ITALIC, 64));
		profileLbl.setText("Profile");
		profileLbl.setForeground(new Color(72, 124, 141));
		view.add(profileLbl, "cell 2 0,alignx center,growy");
	}

	@Override
	public void onCreate() {

	}

	@Override
	public boolean onDestroy() {
		uiInitialized = false;
		return true;
	}

	@Override
	public void onHide() {
		System.out.println(getSceneName() + ": onHide");
		// Pause activities if needed
	}

	@Override
	public void onShow() {
		System.out.println(getSceneName() + ": onShow");
		if (!uiInitialized) {
			onCreate();
		}
	}

}
