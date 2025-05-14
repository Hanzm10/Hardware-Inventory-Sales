package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;
import com.github.hanzm_10.murico.swingapp.lib.utils.SessionUtils;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;

import net.miginfocom.swing.MigLayout;

public class ProfileScene implements Scene, SubSceneSupport {
	public static class ProfileSceneGuard implements SceneGuard {
		@Override
		public boolean canAccess() {
			return SessionManager.getInstance().getSession() != null
					&& !SessionUtils.isSessionExpired(SessionManager.getInstance().getSession());
		}
	}
	public static final SceneGuard GUARD = new ProfileSceneGuard();
	
	private SceneManager sceneManager;
	private JPanel view;
	
	private void createSceneManager() {
		sceneManager = new StaticSceneManager();
		
		sceneManager.registerScene("readonly", () -> new ReadOnlyScene(), GUARD);
		sceneManager.registerScene("edit", () -> new EditProfileScene(), GUARD);
	}
	
	@Override
	public SceneManager getSceneManager() {
		if (sceneManager == null) {
			createSceneManager();
		}
		
		return sceneManager;
	}

	@Override
	public void navigateTo(@NotNull String subSceneName) {
		sceneManager.navigateTo(subSceneName);
		
	}

	@Override
	public void navigateToDefault() {
		SceneNavigator.getInstance().navigateTo(getSceneName() + ParsedSceneName.SEPARATOR + "readonly");
		
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
		view.add(sceneManager.getRootContainer());
		if (sceneManager.getCurrentScene() == null) {
			sceneManager.navigateTo("readonly");
		}
		
	}
	@Override
	public boolean onDestroy() {
		sceneManager.destroy();
		return true;
		
	}
	@Override
	public void onShow() {}
		
}

