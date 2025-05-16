package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ReadOnlyScene implements Scene {
	private JLabel displayRoleLbl;
	private RoundedPanel rolePnl;
	private JLabel profilepicLbl;
	private JLabel displaynameLbl;
	private	JLabel profileLogoLbl;
	private JButton editProfBtn;
	private JLabel profileLbl;
	private boolean uiInitialized = false;
	private JPanel view;
	private Image image;
	private SceneManager sceneManager;
	private String username;
	

	public ReadOnlyScene() {
		// setLayout(new MigLayout());
		//onCreate();
	}

	@Override
	public String getSceneName() {
		// TODO Auto-generated method stub
		return "readonly";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;

	}

	private void initializeProfileUI() throws IOException, InterruptedException {
		Profile profile = new Profile();

		view.setLayout(new MigLayout("fill", "[grow][grow][grow]", "[grow][grow][grow][grow]"));
		
		rolePnl = new RoundedPanel(20);
		view.setBackground(Styles.SECONDARY_COLOR);
		rolePnl.setBackground(Styles.PRIMARY_COLOR);
		rolePnl.setAlignmentX(Component.CENTER_ALIGNMENT);
		rolePnl.setBounds(497, 425, 274, 56);
		view.add(rolePnl, "cell 1 3,growx,aligny top");
		
		displayRoleLbl = new JLabel();
		displayRoleLbl.setForeground(Color.WHITE);
		username = SessionManager.getInstance().getLoggedInUser().displayName();
		displayRoleLbl.setText(profile.getRoleByUsername(username));
		view.add(displayRoleLbl);
		rolePnl.add(displayRoleLbl,  "cell 1 3, alignx right,aligny top");

		profilepicLbl = new JLabel("");
		profilepicLbl.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profilepic.png")));

		//profilepicLbl.setBounds(497, 64, 233, 229);
		view.add(profilepicLbl, "cell 1 1,alignx center,growy");

		displaynameLbl = LabelFactory.createBoldLabel(username.toUpperCase(), 64, Color.WHITE);
		displaynameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
	
		view.add(displaynameLbl, "cell 1 2,alignx center,aligny top");
//
//		profileLogoLbl = new JLabel();
//		//profileLogoLbl.setBackground(new Color));
//		profileLogoLbl.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profileRectangle.png")));
//		view.add(profileLogoLbl, "cell 0 1 4 4,alignx center,aligny top");
//		// profilePnl.setLayout(null);

		editProfBtn = new JButton("Edit Profile");
		editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		//editProfBtn.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/editProf.png")));
		view.add(editProfBtn, "cell 2 0,alignx right,growy");
		
		
		editProfBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			SceneNavigator.getInstance().navigateTo("home/profile/edit");
					
				
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

