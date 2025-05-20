package com.github.hanzm_10.murico.swingapp.scenes;

import javax.swing.JFrame;

import com.github.hanzm_10.murico.swingapp.scenes.home.profile.EditProfileScene;

public class editProf {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		EditProfileScene scene = new EditProfileScene();
		frame.add(scene.getSceneView());
		scene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		scene.onShow();
		
		
	}
}
