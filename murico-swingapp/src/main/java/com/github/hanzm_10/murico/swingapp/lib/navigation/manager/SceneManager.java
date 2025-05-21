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
package com.github.hanzm_10.murico.swingapp.lib.navigation.manager;

import java.awt.CardLayout;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneEntry;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneWrapper;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;

public interface SceneManager {
	void destroy();

	/**
	 * @return The current {@link Scene} that is being displayed.
	 */
	Scene getCurrentScene();

	/**
	 *
	 * @return The name of the current {@link Scene} that is being displayed.
	 */
	String getCurrentSceneName();

	/**
	 *
	 * @return The {@link JPanel} that is used as the root container that contains
	 *         the {@link CardLayout} manager and the {@link Scene} that is being
	 *         displayed.
	 */
	JPanel getRootContainer();

	/**
	 * @param sceneName The name of the {@link Scene} to be retrieved.
	 *
	 * @return {@link Scene} - stored in the manager's scene cache with the given
	 *         name. Null if the scene is not registered, or if the scene is not
	 *         created yet.
	 */
	Scene getScene(@NotNull final String sceneName);

	/**
	 * <p>
	 * This will split the scene name by {@link ParsedSceneName#SEPARATOR} and first
	 * checks if the parent scene is already being displayed by the parent
	 * {@link SceneManager}.
	 *
	 * <p>
	 * If the parent scene is already being displayed, then it checks if scene name
	 * contains a child scene name. The following steps will then be performed:
	 *
	 * <ul>
	 * <li>If the child scene name is null, then it logs a warning and returns.
	 * <li>If the child scene name is not null, then it checks if the parent scene
	 * is a {@link SubSceneSupport}
	 * <li>If the parent scene is a {@link SubSceneSupport}, then it checks if the
	 * child scene is already being displayed by the parent scene's
	 * {@link SceneManager}.
	 * <li>If the child scene is already being displayed, then it logs a warning and
	 * returns.
	 * <li>If the child scene is not already being displayed, then it delegates the
	 * navigation to the parent scene's {@link SceneManager}.
	 * </ul>
	 *
	 * <p>
	 * If the parent scene is not already being displayed, then it first navigates
	 * to the parent scene. The following steps will then be performed:
	 *
	 * <ul>
	 * <li>Get the {@link SceneEntry} of the parent scene from the
	 * {@link SceneManager}.
	 * <li>If the {@link SceneEntry} is null, then it logs a warning and returns.
	 * <li>If the {@link SceneEntry} is not null, then it checks if the scene can be
	 * accessed by a boolean returned by the {@link SceneGuard}.
	 * <li>If the scene cannot be accessed, then it logs a warning and returns.
	 * <li>Gets the currently displayed {@link Scene} and checks if it can be hidden
	 * by calling the {@link Scene#canHide()} method.
	 * <li>If the scene cannot be hidden, then it calls the current scene's
	 * {@link Scene#onCannotHide()} method and returns.
	 * <li>Loads or creates the scene. The following steps are performed:
	 * <ul>
	 * <li>Get the {@link Scene} from cache using the parent scene name.
	 * <li>If the {@link Scene} is not null, then it returns it.
	 * <li>Otherwise, the following steps are performed:
	 * <ul>
	 * <li>Gets the {@link SceneFactory} of the parent scene from the
	 * {@link SceneEntry}.
	 * <li>If the name of the created {@link Scene} is not equal to the name we're
	 * trying to navigate to, then it logs a severe message while still continuing
	 * the operation.
	 * <li>Wraps the {@link Scene} in a {@link SceneWrapper}
	 * <li>Calls the {@link Scene#onCreate()} method.
	 * <li>Retrieves the view from the {@link Scene} and adds it to the root
	 * container and {@link CardLayout} manager.
	 * <li>If the {@link Scene} is a {@link SubSceneSupport}, then it calls the
	 * {@link SubSceneSupport#navigateToDefault()} method.
	 * <li>Updates the cache with the {@link Scene} using the parent scene name.
	 * </ul>
	 * </ul>
	 * <li>If the scene cannot be shown, then it calls the
	 * {@link Scene#onCannotShow()} method and returns.
	 * <li>Finally, it switches the old scene with the new scene and calling their
	 * respective hook lifecycle methods.
	 * <li>Lastly, if the parent scene is a {@link SubSceneSupport}, it tries to
	 * navigate to the child scene and repeats the same process as above.
	 * </ul>
	 *
	 *
	 * @param sceneName
	 */
	void navigateTo(@NotNull final String sceneName);

	void registerScene(@NotNull final String sceneName, @NotNull final SceneFactory sceneFactory);

	void registerScene(@NotNull final String sceneName, @NotNull final SceneFactory sceneFactory,
			@NotNull final SceneGuard sceneGuard);

	void unregisterScene(@NotNull final String sceneName);
}
