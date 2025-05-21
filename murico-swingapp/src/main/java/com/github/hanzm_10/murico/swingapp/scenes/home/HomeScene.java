package com.github.hanzm_10.murico.swingapp.scenes.home;

import javax.swing.JOptionPane;
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
import com.github.hanzm_10.murico.swingapp.scenes.home.components.Header;
import com.github.hanzm_10.murico.swingapp.scenes.home.components.Sidebar;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;

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
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();

		sceneManager.registerScene("profile", () -> new ProfileScene(), GUARD);
		sceneManager.registerScene("dashboard", () -> new DashboardScene(), GUARD);
		sceneManager.registerScene("reports", () -> new ReportsScene(), GUARD);
		sceneManager.registerScene("inventory", () -> new InventoryScene(), GUARD);
		sceneManager.registerScene("order menu", () -> new OrderMenuScene(), GUARD);
		sceneManager.registerScene("contacts", () -> {
			if (loggedInUser.roles().contains("admin")) {
				return new ContactScene();
			} else {
				JOptionPane.showMessageDialog(view, "You do not have permission to access this page.", "Access Denied",
						JOptionPane.ERROR_MESSAGE);
				System.err.println("Access denied to contacts scene for user: " + loggedInUser.displayName());
				return new ReadOnlyScene();
			}
		}, GUARD);
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

		view.setLayout(new MigLayout("insets 0", "[]16px[grow]", "[shrink 0]16px[grow]"));

		view.add(header.getContainer(), "cell 0 0 2, growx");
		view.add(sidebar.getContainer(), "cell 0 1, growy");
		view.add(sceneManager.getRootContainer(), "cell 1 1, grow");
	}

	@Override
	public boolean onDestroy() {
		header.destroy();
		sidebar.destroy();

		header = null;
		sidebar = null;

		return true;
	}

	@Override
	public void onHide() {
	}

	@Override
	public void onShow() {
	}
}
