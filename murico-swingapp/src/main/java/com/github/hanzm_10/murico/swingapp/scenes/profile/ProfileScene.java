package com.github.hanzm_10.murico.swingapp.scenes.profile;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;

import net.miginfocom.swing.MigLayout;


public class ProfileScene extends JPanel implements Scene{
	
	private JLabel displayRoleLbl;
	private JLabel roleLbl;
	private JLabel profilepicLbl;
	private JLabel displaynameLbl;
	private JLabel profileLogoLbl;
	private JButton editProfBtn;
	private JLabel profileLbl;
	private boolean uiInitialized = false;
	
	
	public ProfileScene() {
		setLayout(new MigLayout());
		System.out.println("Profile Scene instance created.");
		
	}
	@Override
	public String getSceneName() {
		// TODO Auto-generated method stub
		return "profile";
	}

	@Override
	public JPanel getSceneView() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void onCreate() {
		if(!uiInitialized) {
			initializeProfileUI();
			uiInitialized = true;
		}
		
	}
	@Override
	public void onShow() {
		System.out.println(getName() + ": onShow");
		if(!uiInitialized) {
			onCreate();
		}
	}
	
	@Override
	public void onHide() {
	        System.out.println(getName() + ": onHide");
	        // Pause activities if needed
	    }
	 @Override
	    public boolean onDestroy() {
		 removeAll();
		 uiInitialized = false;
		return true;
	 }
	private void initializeProfileUI() {
		Profile profile = new Profile();
		JPanel profilePnl = new JPanel();
		profilePnl.setBackground(Color.WHITE);
		//profilePnl.setPreferredSize(screenSize);
		profilePnl.setLayout(null);
		this.add(profilePnl, "name_265538866079209");
		
		displayRoleLbl = new JLabel();
		displayRoleLbl.setForeground(Color.WHITE);
		//displayRoleLbl.setText(profile.getRole(username));
		displayRoleLbl.setFont(new Font("Montserrat", Font.BOLD, 20));
		displayRoleLbl.setBounds(510, 439, 233, 30);
		displayRoleLbl.setOpaque(false);
		profilePnl.add(displayRoleLbl);
		
		roleLbl = new JLabel("");
		roleLbl.setIcon(new ImageIcon(ProfileScene.class.getResource("/imageSource/roleBoarderLabel.png")));
		roleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		roleLbl.setBounds(497, 425, 274, 56);
		profilePnl.add(roleLbl);
		
		profilepicLbl = new JLabel("");
		profilepicLbl.setBackground(new Color(33, 64, 107));
		profilepicLbl.setIcon(new ImageIcon(ProfileScene.class.getResource("/imageSource/profilepic.png")));
		profilepicLbl.setBounds(497, 64, 233, 229);
		profilePnl.add(profilepicLbl);
		
		displaynameLbl = new JLabel();
		displaynameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
		displaynameLbl.setForeground(Color.WHITE);
		displaynameLbl.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		//displaynameLbl.setText(username.toUpperCase());
		displaynameLbl.setFont(new Font("Montserrat", Font.BOLD, 64));
		displaynameLbl.setOpaque(false);
		displaynameLbl.setBounds(318, 305, 616, 84);
		profilePnl.add(displaynameLbl);
	
		
		profileLogoLbl = new JLabel("");
		profileLogoLbl.setIcon(new ImageIcon(ProfileScene.class.getResource("/imageSource/profileRectangle.png")));
		profileLogoLbl.setBounds(29, 202, 1180, 679);
		profilePnl.add(profileLogoLbl);
		//profilePnl.setLayout(null);
		
		editProfBtn = new JButton("");
		editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editProfBtn.setBorderPainted(false);
		editProfBtn.setIcon(new ImageIcon(ProfileScene.class.getResource("/imageSource/editProf.png")));
		editProfBtn.setBounds(838, 95, 371, 62);
		profilePnl.add(editProfBtn);
		
		profileLbl = new JLabel();
		//ProfileTxt.setEditable(false);
		profileLbl.setFont(new Font("Montserrat", Font.BOLD | Font.ITALIC, 64));
		profileLbl.setText("Profile");
		profileLbl.setBounds(897, 24, 233, 71);
		profileLbl.setForeground(new Color(72, 124, 141));
		profilePnl.add(profileLbl);
	}

}
