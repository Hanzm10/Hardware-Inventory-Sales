package com.github.hanzm_10.murico.swingapp.lib.navigation.scene;

import javax.swing.JPanel;

public interface SceneComponent {
	public void destroy();

	public JPanel getView();

	public void initializeComponents();

	public boolean isInitialized();

	default public void performBackgroundTask() {
	};
}
