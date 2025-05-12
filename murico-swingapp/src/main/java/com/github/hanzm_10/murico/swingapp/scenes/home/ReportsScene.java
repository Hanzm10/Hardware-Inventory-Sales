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
package com.github.hanzm_10.murico.swingapp.scenes.home;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.InventoryReportsScene;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.SalesReportsScene;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsHeader;

import net.miginfocom.swing.MigLayout;

public class ReportsScene implements Scene, SubSceneSupport {
	private JPanel view;

	private SceneManager sceneManager;

	private ReportsHeader header;

	private Thread reportsThread;

	private void createSceneManager() {
		sceneManager = new StaticSceneManager();

		sceneManager.registerScene("sales reports", () -> new SalesReportsScene(), HomeScene.GUARD);
		sceneManager.registerScene("inventory reports", () -> new InventoryReportsScene(), HomeScene.GUARD);
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
		return "reports";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void navigateTo(@NotNull String subSceneName) {
		sceneManager.navigateTo(subSceneName);
	}

	@Override
	public void navigateToDefault() {
		SceneNavigator.getInstance().navigateTo(
				"home" + ParsedSceneName.SEPARATOR + getSceneName() + ParsedSceneName.SEPARATOR + "sales reports");
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("", "[grow, left]", "[72px::72px, grow, top][grow, top]"));
		header = new ReportsHeader();

		view.add(header.getContainer(), "cell 0 0, grow");
		view.add(sceneManager.getRootContainer(), "cell 0 1, grow");
	}

	@Override
	public boolean onDestroy() {
		header.destroy();

		return true;
	}

	@Override
	public void onHide() {
		header.destroyListeners();
	}

	@Override
	public void onShow() {
		header.attachListeners();
	}
}
