package com.github.hanzm_10.murico.swingapp.lib.navigation.manager;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

public interface SceneManager {
	void destroy();

	String getCurrentSceneName();

	JPanel getRootContainer();

	Scene getScene(@NotNull final String sceneName);

	void navigateTo(@NotNull final String sceneName);

	void registerScene(@NotNull final String sceneName, @NotNull final SceneFactory sceneFactory);

	void registerScene(@NotNull final String sceneName, @NotNull final SceneFactory sceneFactory,
			@NotNull final SceneGuard sceneGuard);

	void unregisterScene(@NotNull final String sceneName);
}
