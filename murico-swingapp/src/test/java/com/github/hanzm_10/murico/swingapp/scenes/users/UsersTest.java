package com.github.hanzm_10.murico.swingapp.scenes.users;

import javax.swing.JFrame;

public class UsersTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		UsersScene usersScene = new UsersScene();
		frame.add(usersScene);
		usersScene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		usersScene.onShow();
	}

}
