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
 * The SubSceneSupport interface is used to show a sub scene. A sub scene is a
 * scene that is displayed on top of another scene. It is used to display
 * additional information or options without navigating away from the current
 * scene.
 *
 * <p>
 * Sub scenes are identified by their names, which must be unique among all sub
 * scenes. The name of the sub scene is used to navigate to it.
 *
 * <p>
 * An example of a sub scene name is "home/settings" or
 * "home/settings/advanced".
 */
public interface SubSceneSupport {
	SceneManager getSubSceneManager();

	/**
	 * This method is used to show a sub scene. The sub scene is identified by its
	 * name.
	 *
	 * @param subSceneName
	 *            The name of the sub scene.
	 * @throws IllegalArgumentException
	 *             If a sub scene with the provided name does not exist.
	 * @throws IllegalStateException
	 *             If this is called from a non-EDT thread.
	 * @return true if the sub scene was shown successfully, false otherwise.
	 */
	boolean navigateTo(@NotNull final String subSceneName) throws IllegalArgumentException, IllegalStateException;
}
