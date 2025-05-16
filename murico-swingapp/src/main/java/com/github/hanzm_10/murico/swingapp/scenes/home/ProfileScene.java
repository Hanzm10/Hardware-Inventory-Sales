package com.github.hanzm_10.murico.swingapp.scenes.home;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;

import net.miginfocom.swing.MigLayout;

public class ProfileScene implements Scene, SubSceneSupport {
	private JPanel view;

	private SceneManager sceneManager;

	private void createSceneManager() {
		sceneManager = new StaticSceneManager();

		sceneManager.registerScene("readonly", () -> new ReadOnlyScene(), HomeScene.GUARD);
		sceneManager.registerScene("edit", () -> new EditProfileScene(), HomeScene.GUARD);
			
	}
	@Override
	public void navigateTo(@NotNull String subSceneName) {
		sceneManager.navigateTo(subSceneName);
		
	}
	@Override
	public SceneManager getSceneManager() {
		if (sceneManager == null) {
			createSceneManager();
		}
		return sceneManager;
	}

	@Override
	public void navigateToDefault() {
		SceneNavigator.getInstance().navigateTo("home" + ParsedSceneName.SEPARATOR + getSceneName() + ParsedSceneName.SEPARATOR + "readonly");
	}

	@Override
	public String getSceneName() {
		return "profile";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));
		view.add(sceneManager.getRootContainer(), "grow");
	}

	@Override
	public boolean onDestroy() {
		sceneManager.destroy();
		return true;
		
	}

	@Override
	public void onShow() {}
		
}

