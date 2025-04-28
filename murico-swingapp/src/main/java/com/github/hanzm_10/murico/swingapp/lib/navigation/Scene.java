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

import javax.swing.JPanel;

/**
 * The base interface for all scenes in the application. A scene is a view that
 * can be navigated to and from. It is responsible for creating its own view and
 * handling its own events.
 *
 * <p>
 * Scenes are registered with the {@link SceneManager}, which is responsible for
 * managing the navigation between scenes.
 *
 * <p>
 * Scenes are identified by their names, which must be unique among all scenes.
 * The name of the scene is used to navigate to it.
 *
 * <p>
 * An example of a scene name is "home" or "settings". The name must be in lower
 * case. For sub scenes, the name must be in lower case and separated by
 * slashes. An example of a sub scene name is "home/settings" or
 * "home/settings/advanced". There are four life-cycle methods that are called
 * in the following order:
 *
 * <ul>
 * <li>onCreate: Called when the scene is created. This is where the view is
 * created and initialized.
 * <li>onShow: Called when the scene is shown. This is where the scene should be
 * updated. This is where the scene should be updated with data.
 * <li>onBeforeHide: Called before the scene is hidden. This is where the scene
 * should save its state and such.
 * <li>onHide: Called when the scene is hidden. This is where the scene should
 * stop all its operations if there are any.
 * <li>onDestroy: Called when the scene is destroyed. This is where the scene
 * should be cleaned up and reset all its state.
 * </ul>
 */
public interface Scene {
	/**
	 * Useful for when a scene is <code>dirty</code> or has a sub scene that is
	 * <code>dirty</code>.
	 *
	 * <p>
	 * Possibly save the state of the scene or sub scene before navigating away from
	 * it, or prompt the user to save the state of the scene or sub scene before
	 * navigating away from it.
	 */
	default boolean onBeforeHide() {
		return true;
	};

	/**
	 * Useful for showing skeleton loaders or loading indicators before the scene is
	 * shown. So that, when {@link CardLayout#show(JPanel, String)} is called, the
	 * scene is already prepared to be shown.
	 *
	 * @return true if the scene is successfully prepared to be shown, false
	 *         otherwise.
	 */
	default boolean onBeforeShow() {
		return true;
	}

	/**
	 * The name of the scene. This is used to identify the scene in the
	 * {@link SceneManager}. This name must be unique among all scenes.
	 *
	 * @return The name of the scene in lower case.
	 */
	String getName();

	JPanel getView();

	/**
	 * This hook is useful to lazily initialize the contents of the scene.
	 *
	 * @return true if the scene was created successfully, false otherwise.
	 */
	boolean onCreate();

	/** This hook is useful so the scene can have knowledge of when it's shown. */
	void onShow();

	/**
	 * This hook is useful for cleanup tasks when we want to remove the scene.
	 *
	 * @return true if the scene was destroyed successfully, false otherwise.
	 */
	boolean onDestroy();

	/** This hook is useful so the scene can have knowledge of when it's hidden. */
	void onHide();
}
