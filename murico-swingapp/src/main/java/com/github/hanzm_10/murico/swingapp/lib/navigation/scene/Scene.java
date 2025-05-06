package com.github.hanzm_10.murico.swingapp.lib.navigation.scene;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;

public interface Scene {
	default boolean canHide() {
		return true;
	}

	default boolean canShow() {
		return true;
	}

	/**
	 *
	 * @return The name that {@link SceneManager} will use for accessing this scene
	 *         in its cache for navigation.
	 */
	String getSceneName();

	/**
	 * This is where a {@link Scene} should typically create its view if it doesn't
	 * exist yet.
	 *
	 * @return
	 */
	JPanel getSceneView();

	default Scene getSelf() {
		return this;
	}

	default void onBeforeHide() {
	}

	default void onBeforeShow() {
	}

	default void onCannotHide() {
	}

	void onCreate();

	default boolean onDestroy() {
		return true;
	}

	default void onHide() {
	}

	default void onShow() {
	}

	default boolean supportsSubScenes() {
		return this instanceof SubSceneSupport;
	}

}
