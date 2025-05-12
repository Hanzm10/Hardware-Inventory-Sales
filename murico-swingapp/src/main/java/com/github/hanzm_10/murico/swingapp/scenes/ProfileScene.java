package com.github.hanzm_10.murico.swingapp.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.listeners.TogglePasswordFieldVisibilityListener;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.ImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.Line;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextFieldFactory;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextPlaceholder;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ProfileScene implements Scene {
	
	protected JPanel view;
	protected JPanel leftComponent;
	
	protected JLabel profLabel;
	protected Image logoImage;
	protected ImagePanel logo;
	protected JLabel usernameLabel;

	@Override
	public String getSceneName() {
		return "Profile";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
		view = new JPanel(new MigLayout("fill", "[grow]", "[grow]")); 
		leftComponent = new JPanel(new MigLayout("", "[grow]", "[]20[]")); 

		createProfComponents();
		attachComponents(); 

		view.add(leftComponent, "grow"); 
	}
	
	private void createProfComponents() {
		profLabel = new JLabel("Profile");
		profLabel.setFont(profLabel.getFont().deriveFont(Font.BOLD, 32));

		usernameLabel = new JLabel("Name Placeholder");
		usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.BOLD, 24));
		usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
	}
	
	public void setUsername(String username) {
		usernameLabel.setText(username);
	}
	
	private void attachComponents() {
		leftComponent.add(usernameLabel, "cell 0 2 3, grow");
	}
}
