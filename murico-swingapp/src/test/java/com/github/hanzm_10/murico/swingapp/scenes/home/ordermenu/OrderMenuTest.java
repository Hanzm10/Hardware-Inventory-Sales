package com.github.hanzm_10.murico.swingapp.scenes.home.ordermenu;

import javax.swing.JFrame;

import com.github.hanzm_10.murico.swingapp.scenes.home.OrderMenuScene;

public class OrderMenuTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		OrderMenuScene scene = new OrderMenuScene();
		frame.add(scene.getSceneView());
		scene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		scene.onShow();
	}

}
