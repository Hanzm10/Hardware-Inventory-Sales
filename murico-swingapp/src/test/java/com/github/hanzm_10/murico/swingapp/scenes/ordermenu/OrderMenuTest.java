package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

import javax.swing.JFrame;

import com.github.hanzm_10.murico.swingapp.scenes.inventory.InventoryScene;

public class OrderMenuTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		OrderMenuScene scene = new OrderMenuScene();
		frame.add(scene);
		scene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		scene.onShow();
	}

}
