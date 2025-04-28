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
	public JPanel getView() {
		return scene.getView();
	}

	@Override
	public String getName() {
		return scene.getName();
	}

	@Override
	public boolean onDestroy() {
		LOGGER.info("onDestroy: " + scene.getName());

		return scene.onDestroy();
	}

	@Override
	public boolean onBeforeHide() {
		LOGGER.info("onBeforeHide: " + scene.getName());

		return scene.onBeforeHide();
	}

	@Override
	public boolean onBeforeShow() {
		LOGGER.info("onBeforeShow: " + scene.getName());

		return scene.onBeforeShow();
	}

	@Override
	public boolean onCreate() {
		LOGGER.info("onCreate: " + scene.getName());

		return scene.onCreate();
	}

	@Override
	public void onShow() {
		LOGGER.info("onShow: " + scene.getName());

		scene.onShow();
	}

	@Override
	public void onHide() {
		LOGGER.info("onHide: " + scene.getName());

		scene.onHide();
	}
}
