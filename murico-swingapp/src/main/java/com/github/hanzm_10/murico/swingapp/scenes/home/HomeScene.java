package com.github.hanzm_10.murico.swingapp.scenes.home;

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
import com.github.hanzm_10.murico.swingapp.ui.components.Header;
import com.github.hanzm_10.murico.swingapp.ui.components.Sidebar;

import net.miginfocom.swing.MigLayout;

public class HomeScene implements Scene, SubSceneSupport {
	public static class HomeSceneGuard implements SceneGuard {
		@Override
		public boolean canAccess() {
			return SessionManager.getInstance().getSession() != null
					&& !SessionUtils.isSessionExpired(SessionManager.getInstance().getSession());
		}
	}

	public static final SceneGuard GUARD = new HomeSceneGuard();

	private SceneManager sceneManager;
	private JPanel view;
	private Sidebar sidebar;
	private Header header;

	private void createSceneManager() {
		sceneManager = new StaticSceneManager();

		sceneManager.registerScene("profile", () -> new ProfileScene(), GUARD);
		sceneManager.registerScene("dashboard", () -> new DashboardScene(), GUARD);
		sceneManager.registerScene("reports", () -> new ReportsScene(), GUARD);
		sceneManager.registerScene("inventory", () -> new InventoryScene(), GUARD);
		sceneManager.registerScene("order menu", () -> new OrderMenuScene(), GUARD);
		sceneManager.registerScene("contacts", () -> new ContactScene(), GUARD);
		sceneManager.registerScene("settings", () -> new SettingsScene(), GUARD);
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
		return "home";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void navigateTo(@NotNull String subSceneName) {
		// perform checks here (?)
		sceneManager.navigateTo(subSceneName);
	}

	@Override
	public void navigateToDefault() {
		SceneNavigator.getInstance().navigateTo(getSceneName() + ParsedSceneName.SEPARATOR + "profile");
	}

	@Override
	public void onCreate() {
		sidebar = new Sidebar();
		header = new Header();

		view.setLayout(
				new MigLayout("", "[96px::96px,center]24px[200px::,grow,center]", "[72px::72px, grow, center][grow]"));

		view.add(header.getContainer(), "cell 0 0 2, grow");
		view.add(sceneManager.getRootContainer(), "cell 1 1, grow");
		view.add(sidebar.getContainer(), "cell 0 1, grow");

		if (sceneManager.getCurrentSceneName() == null) {
		}
	}

	@Override
	public boolean onDestroy() {
		header.destroy();
		sidebar.destroy();

		return true;
	}

	@Override
	public void onShow() {
	}
}
