package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

import net.miginfocom.swing.MigLayout;

public class ContactScene implements Scene {
	private JPanel view;
	protected JPanel leftComponent;
	
	protected JPanel nameRows;
	protected JLabel conLabel;
	protected Image logoImage;

	@Override
	public String getSceneName() {
		return "contacts";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
	  JPanel nameRows = new JPanel();
	  MigLayout layout = new MigLayout("wrap 4");
	  
	  nameRows.setLayout(layout);
	  view.add(nameRows);
	  nameRows.add(new JLabel("Username"));
	  nameRows.add(new JLabel("Contact Number"));
	  
	  ///ADD DATABASES CONDITION
	  nameRows.add(new JLabel("E-mail"));
	  ////
	  
	  nameRows.add(new JLabel("Role"));
	}

}
