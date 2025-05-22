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
package com.github.hanzm_10.murico.swingapp.scenes.auth;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;
import com.github.hanzm_10.murico.swingapp.lib.utils.SessionUtils;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;

import net.miginfocom.swing.MigLayout;

public class AuthScene implements Scene, SubSceneSupport {
	public static class AuthSceneGuard implements SceneGuard {
		@Override
		public boolean canAccess() {
			return SessionManager.getInstance().getSession() == null
					|| SessionUtils.isSessionExpired(SessionManager.getInstance().getSession());
		}
	}

	public static final SceneGuard GUARD = new AuthSceneGuard();
	protected SceneManager sceneManager;

	protected JPanel view;

	private void createSceneManager() {
		sceneManager = new StaticSceneManager();
		sceneManager.registerScene("main", () -> new MainAuthScene(), GUARD);
		sceneManager.registerScene("login", () -> new LoginAuthScene(), GUARD);
		sceneManager.registerScene("register", () -> new RegisterAuthScene(), GUARD);
	}

	@Override
	public SceneManager getSceneManager() {
		if (sceneManager == null) {
			createSceneManager();
		}

		return sceneManager;
	}

	@Override
	public String getSceneName() {
		return "auth";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public boolean navigateTo(@NotNull String subSceneName) {
		return sceneManager.navigateTo(subSceneName);
	}

	@Override
	public void navigateToDefault() {
		SceneNavigator.getInstance().navigateTo(getSceneName() + ParsedSceneName.SEPARATOR + "login");

	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("", "[grow, center]", "[grow, center]"));

		var rootContainer = sceneManager.getRootContainer();
		view.add(rootContainer, "cell 0 0");

		if (sceneManager.getCurrentSceneName() == null) {
		}
	}

	@Override
	public boolean onDestroy() {
		view.removeAll();
		view.revalidate();
		view.repaint();
		view = null;

		return true;
	}

	@Override
	public void onShow() {
	}
}
