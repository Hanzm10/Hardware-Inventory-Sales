package com.github.hanzm_10.murico.swingapp.scenes.inventory;

import javax.swing.JFrame;

public class InventoryTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		InventoryScene scene = new InventoryScene();
		frame.add(scene);
		scene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		scene.onShow();
	}
	
}
