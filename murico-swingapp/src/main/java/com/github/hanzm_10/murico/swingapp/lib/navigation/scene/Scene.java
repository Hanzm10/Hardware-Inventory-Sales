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
package com.github.hanzm_10.murico.swingapp.lib.navigation.scene;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;

/**
 * Represents a navigable screen or view in the application. Scenes are managed
 * by the {@link SceneManager} and can implement lifecycle hooks such as
 * creation, display, and hiding logic.
 */
public interface Scene {

	/**
	 * Determines whether this scene can be hidden at the moment. Override to
	 * perform checks before allowing navigation away.
	 *
	 * @return true if the scene can be hidden, false otherwise.
	 */
	default boolean canHide() {
		return true;
	}

	/**
	 * Determines whether this scene can be shown at the moment. Override to block
	 * showing the scene under certain conditions.
	 *
	 * @return true if the scene can be shown, false otherwise.
	 */
	default boolean canShow() {
		return true;
	}

	/**
	 * @return The name used by {@link SceneManager} to store and access this scene
	 *         in its internal cache for navigation.
	 */
	String getSceneName();

	/**
	 * Called to obtain the view component of the scene. This is typically where the
	 * layout is initialized if it hasn't been created yet.
	 *
	 * @return The root panel for this scene.
	 */
	JPanel getSceneView();

	/**
	 * Returns the instance of this scene. Useful for delegation or decoration
	 * patterns.
	 *
	 * @return This scene instance.
	 */
	default Scene getSelf() {
		return this;
	}

	/**
	 * Hook called before the scene is hidden. Use this to pause resources or stop
	 * animations.
	 */
	default void onBeforeHide() {
	}

	/**
	 * Hook called before the scene is shown. Use this to prepare data or refresh UI
	 * elements.
	 */
	default void onBeforeShow() {
	}

	/**
	 * Hook called if the scene cannot be hidden due to {@link #canHide()} returning
	 * false. Useful for showing warnings or validation messages.
	 */
	default void onCannotHide() {
	}

	/**
	 * Called once when the scene is first created. This is the recommended place to
	 * allocate resources or set up state.
	 */
	void onCreate();

	/**
	 * Called when the scene is being destroyed. Override to release resources.
	 * Returning false cancels the destruction.
	 *
	 */
	default boolean onDestroy() {
		return true;
	}

	/**
	 * Called after the scene is hidden. Use this to clean up temporary state or
	 * stop background processes.
	 */
	default void onHide() {
	}

	/**
	 * Called after the scene is shown. Use this to start animations, load data, or
	 * activate event listeners.
	 */
	default void onShow() {
	}

	/**
	 * Indicates whether the scene supports sub-scene navigation. Defaults to true
	 * if the scene implements {@link SubSceneSupport}.
	 *
	 * @return true if sub-scenes are supported, false otherwise.
	 */
	default boolean supportsSubScenes() {
		return this instanceof SubSceneSupport;
	}
}
