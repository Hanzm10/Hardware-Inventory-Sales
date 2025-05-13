package com.github.hanzm_10.murico.swingapp.scenes;

import javax.swing.JFrame;

import com.github.hanzm_10.murico.swingapp.scenes.home.EditProfile;

public class editProf {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		EditProfile scene = new EditProfile();
		frame.add(scene.getSceneView());
		scene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		scene.onShow();
		
		
	}
}
