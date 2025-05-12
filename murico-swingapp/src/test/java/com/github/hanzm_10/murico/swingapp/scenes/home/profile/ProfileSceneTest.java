package com.github.hanzm_10.murico.swingapp.scenes.home.profile;

import javax.swing.JFrame;


public class ProfileSceneTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		ProfileScene usersScene = new ProfileScene();
		frame.add(usersScene);
		usersScene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		usersScene.onShow();
	}

}