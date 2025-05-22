package com.github.hanzm_10.murico.swingapp.lib.navigation.scene;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;

public interface SubSceneSupport {
	SceneManager getSceneManager();

	boolean navigateTo(@NotNull String subSceneName);

	void navigateToDefault();
}
