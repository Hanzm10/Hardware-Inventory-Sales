package com.github.hanzm_10.murico.swingapp.scenes.home;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.EditProfileScene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.ReadOnlyProfileScene;

import net.miginfocom.swing.MigLayout;

public class ProfileScene implements Scene, SubSceneSupport {

	private SceneManager sceneManager;

	private JPanel view;

	private void createSceneManager() {
		sceneManager = new StaticSceneManager();

		sceneManager.registerScene("readonly", () -> new ReadOnlyProfileScene(), HomeScene.GUARD);
		sceneManager.registerScene("edit", () -> new EditProfileScene(), HomeScene.GUARD);
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
		return "profile";
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
		SceneNavigator.getInstance().navigateTo(
				"home" + ParsedSceneName.SEPARATOR + getSceneName() + ParsedSceneName.SEPARATOR + "readonly");
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
	public void onShow() {
	}

}
