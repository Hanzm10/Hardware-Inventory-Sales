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
package com.github.hanzm_10.murico.swingapp.lib.navigation;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.hanzm_10.murico.swingapp.lib.cache.ObserverLRU;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;

/**
 * This class manages the navigation between different scenes in a Java Swing
 * application.
 *
 * <p>
 * It uses a {@link CardLayout} to switch between scenes, allowing for a
 * stack-like behavior where the most recently accessed scene is shown on top.
 *
 * <p>
 * It provides methods to navigate to a specific scene, destroy the current
 * scene, and unregister scenes. It also handles the lifecycle of scenes,
 * ensuring that they are properly created, shown, hidden, and destroyed.
 *
 * <p>
 * This class expects that its {@link #navigateTo(String)},
 * {@link #unregisterScene(String)}, {@link #destroy()} methods will be called
 * from the Event Dispatch Thread (EDT) to ensure thread safety. It is
 * recommended to use {@link SwingUtilities#invokeLater(Runnable)} to ensure
 * that the code is executed on the EDT. For example:
 *
 * <pre>
 * SwingUtilities.invokeLater(() -> {
 * 	// Your code here
 * })
 * </pre>
 */
public class SceneManager {

	private static final Logger LOGGER = MuricoLogger.getLogger(SceneManager.class);

	protected final JPanel rootContainer;
	protected final CardLayout cardLayout;

	protected final List<SceneEntry> sceneEntries;
	protected final ObserverLRU<String, Scene> sceneCache;

	protected String currentSceneName;

	public SceneManager() {
		cardLayout = new CardLayout();
		rootContainer = new JPanel(cardLayout);

		sceneEntries = new ArrayList<>();
		sceneCache = new ObserverLRU<>(10);

		sceneCache.subscribe((scene) -> {
			SwingUtilities.invokeLater(() -> {
				destroyScene(scene);
			});
		});
	}

	public JPanel getRootContainer() {
		return rootContainer;
	}

	public Scene getScene(@NotNull final String sceneName) {
		return sceneCache.get(sceneName);
	}

	protected void destroyScene(@NotNull Scene scene) throws IllegalStateException {
		if (scene == null) {
			return;
		}

		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("destroyScene must be called from the EDT.");
		}

		var view = scene.getView();

		if (view != null) {
			rootContainer.remove(view);
			cardLayout.removeLayoutComponent(view);
		} else {
			LOGGER.warning("Scene: " + scene.getName() + " has its view as null before being destroyed.");
		}

		scene.onDestroy();

		LOGGER.info("Scene: " + scene.getName() + " destroyed.");
	}

	/**
	 * Destroys all scenes in the cache and clears the cache.
	 *
	 * <p>
	 * This method should be called from the Event Dispatch Thread (EDT) to ensure
	 * thread safety.
	 *
	 * @throws IllegalStateException
	 *             if this method is called from a thread other than the EDT.
	 */
	public void destroy() throws IllegalStateException {
		for (var scene : sceneCache.values()) {
			if (scene != null) {
				destroyScene(scene);
			} else {
				LOGGER.warning("Reached an unexpected null scene in the cache.");
			}
		}

		sceneEntries.clear();
		sceneCache.clear();
		currentSceneName = null;

		LOGGER.info("SceneManager destroyed.");
	}

	/**
	 * Navigates to the specified scene.
	 *
	 * <p>
	 * This method should be called from the Event Dispatch Thread (EDT) to ensure
	 * thread safety.
	 *
	 * @param sceneName
	 * @return
	 * @throws Exception
	 *             errors thrown by {@link SceneFactory#createScene},
	 *             {@link SceneGuard#canAccess}.
	 */
	public boolean navigateTo(@NotNull final String sceneName) throws IllegalStateException, IllegalArgumentException {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("navigateTo must be called from the EDT.");
		}

		LOGGER.info("Navigating to scene: " + sceneName);

		Scene oldScene = null;

		if (currentSceneName == null) {
			currentSceneName = sceneName;
		} else {
			if (currentSceneName.equals(sceneName)) {
				// Maybe call refresh() on the current scene
				LOGGER.warning("Already in the scene: " + sceneName + ". No navigation needed.");
				return false;
			}

			oldScene = sceneCache.get(currentSceneName);

			if (oldScene == null) {
				LOGGER.severe("Current scene: " + currentSceneName
						+ " not found in cache despite currentSceneName being set.");
				return false;
			}

			if (!oldScene.onBeforeHide()) {
				return false;
			}
		}

		var sceneInCache = sceneCache.get(sceneName);

		if (sceneInCache != null) {
			return showScene(sceneInCache, oldScene);
		}

		for (var entry : sceneEntries) {
			var parameters = entry.matchMap(sceneName);

			if (parameters == null) {
				continue;
			}

			if (!entry.getSceneGuard().canAccess(parameters)) {
				LOGGER.warning("Scene guard denied access to scene: " + entry.getUnMatchedSceneName()
						+ " with parameters: " + parameters);
				return false;
			}

			var scene = new SceneWrapper(entry.getSceneFactory().createScene(parameters));
			var view = scene.getView();

			if (!scene.getName().equals(sceneName)) {
				LOGGER.warning("Scene name mismatch: expected " + sceneName + ", but got " + scene.getName());
				return false;
			}

			sceneCache.update(sceneName, scene);
			rootContainer.add(view);
			cardLayout.addLayoutComponent(view, sceneName);
			scene.onCreate();

			return showScene(scene, oldScene);
		}

		throw new IllegalArgumentException("Scene: " + sceneName + " not found.");
	}

	private boolean showScene(@NotNull final Scene newScene, @Nullable final Scene oldScene) {
		newScene.onBeforeShow();
		cardLayout.show(rootContainer, newScene.getName());

		if (oldScene != null) {
			oldScene.onHide();
		}

		newScene.onShow();
		currentSceneName = newScene.getName();

		LOGGER.info("Navigated to scene: " + newScene.getName());

		return true;
	}

	/**
	 * Registers a scene with the scene manager.
	 *
	 * <p>
	 * This scene's {@link SceneGuard} will always return true.
	 *
	 * @param sceneName
	 * @param sceneFactory
	 */
	public void registerScene(@NotNull final String sceneName, @NotNull final SceneFactory sceneFactory) {
		registerScene(sceneName, sceneFactory, _ -> true);
	}

	/**
	 * Registers a scene with a guard.
	 *
	 * @param sceneName
	 * @param sceneFactory
	 * @param sceneGuard
	 */
	public void registerScene(@NotNull final String sceneName, @NotNull final SceneFactory sceneFactory,
			@NotNull final SceneGuard sceneGuard) {
		if (sceneEntries.stream().anyMatch(entry -> entry.getUnMatchedSceneName().equals(sceneName))) {
			LOGGER.warning("Scene: " + sceneName + " is already registered.");
			return;
		}

		sceneEntries.add(new SceneEntry(sceneName, sceneFactory, sceneGuard));
		sceneEntries.sort(Comparator.comparingInt(e -> e.dynamicParamCount));

		LOGGER.info("Registered scene: " + sceneName);
	}

	/**
	 * @param sceneName
	 * @throws IllegalStateException
	 *             if this method is called from a thread other than the EDT.
	 */
	public void unregisterScene(@NotNull final String sceneName) throws IllegalStateException {
		var entry = sceneEntries.stream().filter(e -> e.getUnMatchedSceneName().equals(sceneName)).findFirst();

		if (entry.isEmpty()) {
			LOGGER.warning("Trying to unregister a scene that is not registered: " + sceneName);
			return;
		}

		var scene = sceneCache.get(sceneName);

		if (scene != null) {
			destroyScene(scene);
		}

		sceneEntries.remove(entry.get());
		sceneCache.remove(sceneName);

		LOGGER.info("Unregistered scene: " + sceneName);
	}
}
