package com.github.hanzm_10.murico.swingapp.scenes.home.inventory;

import javax.swing.JFrame;

import com.github.hanzm_10.murico.lookandfeel.MuricoLightFlatLaf;
import com.github.hanzm_10.murico.swingapp.scenes.home.InventoryScene;

public class InventoryTest {
	public static void main(String[] args) {
		MuricoLightFlatLaf.setup();
		JFrame frame = new JFrame();
		InventoryScene scene = new InventoryScene();
		frame.add(scene.getSceneView());
		scene.onCreate();
		frame.setDefaultCloseOperation(2);
		frame.pack();
		frame.setVisible(true);
		scene.onShow();
	}
}
