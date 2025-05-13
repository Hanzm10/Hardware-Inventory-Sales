package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;

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

<<<<<<< HEAD
	public ProfileScene() {
		// setLayout(new MigLayout());
		//onCreate();
	}

=======
>>>>>>> branch 'main' of https://github.com/alyastanga/Hardware-Inventory-Sales.git
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

		view.setLayout(new MigLayout("fill", "[][413.00][][]", "[][66.00][88.00][]"));

		displayRoleLbl = new JLabel();
		displayRoleLbl.setForeground(Color.WHITE);
		String username = SessionManager.getInstance().getLoggedInUser().displayName();
		displayRoleLbl.setText(profile.getRoleByUsername(username));
		displayRoleLbl.setFont(new Font("Montserrat", Font.BOLD, 20));
		displayRoleLbl.setOpaque(false);
		view.add(displayRoleLbl);
		view.add(displayRoleLbl,  "cell 2 3,alignx center,aligny top");

		roleLbl = new JLabel("");
		image = AssetManager.getOrLoadImage("images/roleBoarderLabel.png");
		roleLbl.setIcon(new ImageIcon(image));
		roleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		roleLbl.setBounds(497, 425, 274, 56);
		view.add(roleLbl, "cell 2 3,alignx center,aligny top");

		profilepicLbl = new JLabel("");
		profilepicLbl.setBackground(new Color(33, 64, 107));
		profilepicLbl.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profilepic.png")));

		profilepicLbl.setBounds(497, 64, 233, 229);
		view.add(profilepicLbl, "cell 2 0, alignx center, aligny center");

		displaynameLbl = new JLabel();
		displaynameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
		displaynameLbl.setForeground(Color.WHITE);
		displaynameLbl.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displaynameLbl.setText(username.toUpperCase());
		displaynameLbl.setFont(new Font("Montserrat", Font.BOLD, 64));
		displaynameLbl.setOpaque(false);
		view.add(displaynameLbl, "cell 2 2,alignx center,aligny top");

		profileLogoLbl = new JLabel("");
		profileLogoLbl.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profileRectangle.png")));
		view.add(profileLogoLbl, "cell 1 1 4 4,alignx center,aligny top");
		// profilePnl.setLayout(null);

		editProfBtn = new JButton("");
		editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editProfBtn.setBorderPainted(false);
		editProfBtn.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/editProf.png")));
		view.add(editProfBtn, "cell 3 0,alignx right,growy");

		
		editProfBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isAdmin = profile.isAdmin(username);
				if(isAdmin) {
					EditProfile scene = new EditProfile();
					scene.onCreate();
					scene.onShow();
					
				}else {
					System.out.print("error");
				}
			}
		});
	}

	@Override
	public void onCreate() {
		if(!uiInitialized) {
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
