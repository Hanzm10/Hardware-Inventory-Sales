/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.cache.ObserverLRU;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneEntry;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;

public class StaticSceneManager implements SceneManager {
	private static final Logger LOGGER = MuricoLogger.getLogger(StaticSceneManager.class);

	protected final JPanel rootContainer;
	protected final CardLayout cardLayout;

	protected final HashMap<String, SceneEntry> registeredSceneEntries;
	protected final ObserverLRU<String, Scene> sceneCache;

	protected String currentSceneName;

	public StaticSceneManager() {
		super();

		cardLayout = new CardLayout();
		cardLayout.setHgap(0);
		cardLayout.setVgap(0);
		rootContainer = new JPanel(cardLayout);
		registeredSceneEntries = new HashMap<>();
		sceneCache = new ObserverLRU<>(10);
		currentSceneName = null;

		sceneCache.subscribe((scene) -> SwingUtilities.invokeLater(() -> destroyScene(scene)));
	}

	private void destroyScene(Scene scene) {
		if (scene == null) {
			return;
		}

		throwIfWrongThread();

		var view = scene.getSceneView();

		if (view != null) {
			rootContainer.remove(view);
		} else {
			LOGGER.severe("Scene view is null before destroy of scene: " + scene.getSceneName());
		}

		sceneCache.remove(scene.getSceneName());
		rootContainer.remove(scene.getSceneView());
		scene.onDestroy();

		LOGGER.info("Scene destroyed: " + scene.getSceneName());
	}

	private void throwIfWrongThread() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new WrongThreadException("This method must be called on the Event Dispatch Thread.");
		}
	}

	private Scene loadOrCreateScene(@NotNull final String sceneName, @NotNull final SceneEntry sceneEntry) {
		var scene = getScene(sceneName);

		if (scene == null) {
			scene = sceneEntry.sceneFactory().createScene();

			if (scene == null) {
				LOGGER.severe("Received null scene from factory of: " + sceneName);
			}

			if (!scene.getSceneName().equals(sceneName)) {
				LOGGER.severe(
						"Scene name does not match the scene's name: " + sceneName + " != " + scene.getSceneName());
			}

			var view = scene.getSceneView();
			scene.onCreate();
		}

		sceneCache.update(sceneName, scene);

		return scene;
	}

	private void navigateToSubScenesIfPossible(@NotNull final ParsedSceneName parsedSceneName) {
		var currentScene = getScene(currentSceneName);

		if (currentScene == null) {
			LOGGER.severe("Current scene is set to a scene that does not exist: " + currentSceneName);
			return;
		}

		if (currentScene.supportsSubScenes()) {
			((SubSceneSupport) currentScene.getSelf()).navigateTo(parsedSceneName.subSceneName());
		} else {
			LOGGER.warning("Current scene does not support sub-scenes: " + currentSceneName);
			return;
		}

		return;
	}

	@Override
	public synchronized void navigateTo(@NotNull String sceneName) {
		throwIfWrongThread();

		var parsedSceneName = ParsedSceneName.parse(sceneName);

		if (parsedSceneName.parentSceneName().equals(currentSceneName)) {
			if (parsedSceneName.subSceneName() == null) {
				LOGGER.warning("Cannot navigate to the same scene: " + sceneName);
				return;
			}

			navigateToSubScenesIfPossible(parsedSceneName);
			return;
		}

		var sceneEntry = registeredSceneEntries.get(parsedSceneName.parentSceneName());

		if (sceneEntry == null) {
			LOGGER.severe("Scene entry not found: " + parsedSceneName.parentSceneName());
			return;
		}

		if (!sceneEntry.sceneGuard().canAccess()) {
			LOGGER.warning("Scene guard denied access: " + sceneName);
			return;
		}

		var oldScene = currentSceneName == null ? null : getScene(currentSceneName);

		if (oldScene != null && !oldScene.canHide()) {
			oldScene.onCannotHide();
			return;
		}

		var scene = loadOrCreateScene(parsedSceneName.parentSceneName(), sceneEntry);

		if (!scene.canShow()) {
			return;
		}

		switchScenes(scene, oldScene);

		LOGGER.info("Navigated to scene: " + sceneName);
	}

	private void switchScenes(@NotNull Scene newScene, Scene oldScene) {
		if (oldScene != null) {
			oldScene.onBeforeHide();
		}

		var newSceneName = newScene.getSceneName();

		newScene.onBeforeShow();
		rootContainer.add(newScene.getSceneView(), newSceneName);
		cardLayout.show(rootContainer, newSceneName);

		if (oldScene != null) {
			rootContainer.remove(oldScene.getSceneView());
			oldScene.onHide();
		}

		newScene.onShow();
		currentSceneName = newSceneName;
	}

	@Override
	public synchronized void registerScene(@NotNull String sceneName, @NotNull SceneFactory sceneFactory) {
		registerScene(sceneName, sceneFactory, () -> true);
	}

	@Override
	public synchronized void registerScene(@NotNull String sceneName, @NotNull SceneFactory sceneFactory,
			@NotNull SceneGuard sceneGuard) {
		if (registeredSceneEntries.containsKey(sceneName)) {
			LOGGER.warning("Scene already registered: " + sceneName);
			return;
		}

		registeredSceneEntries.put(sceneName, new SceneEntry(sceneFactory, sceneGuard));
		LOGGER.info("Scene registered: " + sceneName);
	}

	@Override
	public synchronized void unregisterScene(@NotNull String sceneName) {
		throwIfWrongThread();

		if (!registeredSceneEntries.containsKey(sceneName)) {
			LOGGER.warning("Trying to unregister a scene that is not registered: " + sceneName);
			return;
		}

		destroyScene(getScene(sceneName));
		registeredSceneEntries.remove(sceneName);

		LOGGER.info("Scene unregistered: " + sceneName);
	}

	@Override
	public synchronized void destroy() {
		throwIfWrongThread();

		for (var scene : sceneCache.values()) {
			destroyScene(scene);
		}

		registeredSceneEntries.clear();
		sceneCache.clear();
		currentSceneName = null;

		rootContainer.removeAll();

		LOGGER.info("Scene manager destroyed.");
	}

	@Override
	public synchronized Scene getScene(@NotNull String sceneName) {
		return sceneCache.get(sceneName);
	}

	@Override
	public String getCurrentSceneName() {
		return currentSceneName;
	}

	@Override
	public JPanel getRootContainer() {
		return rootContainer;
	}
}
