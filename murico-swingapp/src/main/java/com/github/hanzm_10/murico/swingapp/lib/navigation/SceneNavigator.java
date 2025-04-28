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

import org.jetbrains.annotations.NotNull;

/**
 * This class manages the navigation between different scenes in a Java Swing
 * application. This is a global class that is used to navigate between scenes
 * for ease of use.
 *
 * <p>
 * It uses {@link SceneManager} to switch between scenes.
 */
public class SceneNavigator {
	private static SceneManager sceneManager;
	private static boolean isInitialized = false;

	public static void initialize(@NotNull final SceneManager manager) {
		if (isInitialized) {
			throw new IllegalStateException("SceneNavigator is already initialized.");
		}

		sceneManager = manager;
		isInitialized = true;
	}

	/**
	 * Navigates to the specified scene. This method is a global method that can be
	 * used to navigate to any scene in the application.
	 *
	 * @param sceneName
	 *            The name of the scene to navigate to.
	 * @return {@code true} if the navigation was successful, {@code false}
	 *         otherwise.
	 * @throws IllegalStateException
	 *             If the SceneNavigator is not initialized or if this is called
	 *             from a non-EDT thread.
	 * @throws IllegalArgumentException
	 *             If the sceneName is null or empty or if the {@code sceneName} is
	 *             not registered.
	 */
	public static boolean navigateTo(@NotNull final String sceneName)
			throws IllegalStateException, IllegalArgumentException {
		if (!isInitialized) {
			throw new IllegalStateException("SceneNavigator is not initialized.");
		}

		var sceneNameParts = sceneName.split("/", 2);
		var parentSceneName = sceneNameParts[0];
		var result = sceneManager.navigateTo(parentSceneName);

		if (sceneNameParts.length == 1) {
			return result;
		}

		var subSceneName = sceneNameParts[1];

		if (subSceneName == null || subSceneName.isEmpty()) {
			return false;
		}

		if (sceneManager.getScene(sceneName) instanceof SubSceneSupport subSceneSupport) {
			return subSceneSupport.navigateTo(subSceneName);
		}

		return false;
	}
}
