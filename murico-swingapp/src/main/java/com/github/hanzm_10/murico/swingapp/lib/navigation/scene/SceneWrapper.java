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

import java.util.logging.Logger;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class SceneWrapper implements Scene {
	private static final Logger LOGGER = MuricoLogger.getLogger(Scene.class);
	private final Scene scene;

	public SceneWrapper(Scene scene) {
		this.scene = scene;
	}

	@Override
	public boolean canHide() {
		var currSubScene = getCurrentSubScene();

		if (currSubScene != null) {
			return scene.canHide() && currSubScene.canHide();
		}

		return scene.canHide();
	}

	@Override
	public boolean canShow() {
		var currSubScene = getCurrentSubScene();

		if (currSubScene != null) {
			return scene.canShow() && currSubScene.canShow();
		}

		return scene.canShow();
	}

	private Scene getCurrentSubScene() {
		if (supportsSubScenes()) {
			var sceneManager = ((SubSceneSupport) scene).getSceneManager();

			if (sceneManager == null) {
				return null;
			}

			var currentSubScene = sceneManager.getCurrentScene();

			return currentSubScene;
		}

		return null;
	}

	@Override
	public String getSceneName() {
		return scene.getSceneName();
	}

	@Override
	public JPanel getSceneView() {
		return scene.getSceneView();
	}

	@Override
	public Scene getSelf() {
		return scene;
	}

	@Override
	public void onBeforeHide() {
		var currSubScene = getCurrentSubScene();

		if (currSubScene != null) {
			currSubScene.onBeforeHide();
		}

		scene.onBeforeHide();
	}

	@Override
	public void onBeforeShow() {
		var currSubScene = getCurrentSubScene();

		if (currSubScene != null) {
			currSubScene.onBeforeShow();
		}

		scene.onBeforeShow();
	}

	@Override
	public void onCannotHide() {
		var currSubScene = getCurrentSubScene();

		if (currSubScene != null) {
			currSubScene.onCannotHide();
		}

		scene.onCannotHide();
	}

	@Override
	public void onCreate() {
		var currSubScene = getCurrentSubScene();

		if (currSubScene != null) {
			currSubScene.onCreate();
		}

		scene.onCreate();
	}

	@Override
	public boolean onDestroy() {
		if (supportsSubScenes()) {
			var sceneManager = ((SubSceneSupport) scene).getSceneManager();

			if (sceneManager != null) {
				sceneManager.destroy();
			}
		}

		return scene.onDestroy();
	}

	@Override
	public void onHide() {
		var currSubScene = getCurrentSubScene();

		if (currSubScene != null) {
			currSubScene.onHide();
		}

		scene.onHide();
	}

	@Override
	public void onShow() {
		var currSubScene = getCurrentSubScene();

		if (currSubScene != null) {
			currSubScene.onShow();
		}

		scene.onShow();
	}

	@Override
	public boolean supportsSubScenes() {
		return scene.supportsSubScenes();
	}
}