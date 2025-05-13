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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.observer.Observer;
import com.github.hanzm_10.murico.swingapp.lib.observer.Subscriber;

public class SceneNavigator implements Observer<String> {
	private static final Logger LOGGER = MuricoLogger.getLogger(SceneNavigator.class);
	private static SceneNavigator instance;

	public static synchronized SceneNavigator getInstance() {
		if (instance == null) {
			instance = new SceneNavigator();
		}

		return instance;
	}

	private List<Subscriber<String>> navigationSubscribers = new ArrayList<Subscriber<String>>();

	private String currentFullSceneName = null;
	private SceneManager sceneManager;

	private boolean isInitialized = false;

	private SceneNavigator() {
	}

	public void destroy() {
		sceneManager.destroy();
		sceneManager = null;
		isInitialized = false;
	}

	public String getCurrentFullSceneName() {
		return currentFullSceneName;
	}

	public SceneManager getSceneManager() {
		if (!isInitialized) {
			throw new IllegalStateException("SceneNavigator is not initialized.");
		}

		return sceneManager;
	}

	public void initialize(@NotNull final SceneManager manager) {
		if (isInitialized) {
			throw new IllegalStateException("SceneNavigator is already initialized.");
		}

		sceneManager = manager;
		isInitialized = true;
	}

	/**
	 * This method is useful to globally navigate to a scene. It will throw an
	 * exception if the scene is not registered or if the scene is not a valid
	 * scene.
	 *
	 * <p>
	 * The <code>sceneName</code> has the format of
	 * <code>[parentSceneName]/[subSceneName]/...
	 * </code> and the scene name must be registered in the {@link SceneManager}.
	 * Each [parentSceneName]'s respective scene must handle the [subSceneName] in
	 * its own {@link SceneManager}. Navigates to the specified scene. This method
	 * will throw an exception if the scene is not registered or if the scene is not
	 * a valid scene.
	 *
	 * @param sceneName The name of the scene to navigate to.
	 * @throws IllegalArgumentException If the scene name is invalid.
	 * @throws WrongThreadException     If this method is called from a thread other
	 *                                  than the Event Dispatch Thread.
	 */
	public void navigateTo(@NotNull final String sceneName) {
		if (!isInitialized) {
			throw new IllegalStateException("SceneNavigator is not initialized.");
		}

		SwingUtilities.invokeLater(() -> {
			sceneManager.navigateTo(sceneName);

			LOGGER.info("Navigated to scene: " + sceneName);

			synchronized (sceneName) {
				currentFullSceneName = sceneName;
				notifySubscribers(sceneName);
			}
		});
	}

	@Override
	public synchronized void notifySubscribers(String value) {
		navigationSubscribers.forEach((cb) -> cb.notify(value));
	}

	@Override
	public synchronized void subscribe(Subscriber<String> subscriber) {
		navigationSubscribers.add(subscriber);
	}

	@Override
	public synchronized void unsubscribe(Subscriber<String> subscriber) {
		navigationSubscribers.remove(subscriber);
	}
}
