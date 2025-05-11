package com.github.hanzm_10.murico.swingapp.scenes.home;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

public class OrderMenuScene implements Scene {
	private JPanel view;

	@Override
	public String getSceneName() {
		return "order menu";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {

	}

}
