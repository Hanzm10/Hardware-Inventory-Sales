package com.github.hanzm_10.murico.swingapp.scenes;

import javax.swing.JFrame;

import com.github.hanzm_10.murico.swingapp.scenes.inventory.InventoryScene;

public class ProfileSceneTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		ProfileScene scene = new ProfileScene();
		frame.add(scene.getSceneView());
		scene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		scene.onShow();
	}
}
