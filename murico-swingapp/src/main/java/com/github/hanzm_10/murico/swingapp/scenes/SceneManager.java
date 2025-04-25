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
package com.github.hanzm_10.murico.swingapp.scenes;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

/**
 * A class that manages the navigation between scenes in the application. It
 * uses a CardLayout to switch between scenes and keeps track of the current
 * scene and the history of scenes for backward navigation. An example usage:
 *
 * <pre>
 *
 * SceneManager sceneManager = new SceneManager();
 * sceneManager.register("home", params -> new HomeScene());
 * sceneManager.register("settings", params -> new SettingsScene());
 *
 * SceneNavigator.setSceneManager(sceneManager);
 *
 * class HomeScene implements Scene {
 * 	private JPanel view;
 *
 * 	public HomeScene() {
 *
 * 		var button = new JButton("Go to Settings");
 *
 * 		button.addActionListener(e -> {
 * 			SceneNavigator.navigateTo("settings");
 * 		});
 * 	}
 * }
 *
 * </pre>
 *
 * @author Aaron Ragudos
 */
public class SceneManager {
	private static class RouteEntry {
		RouteFactory factory;
		Pattern regex;
		List<String> params;
		RouteGuard guard;
		int dynamicPathCount;

		public RouteEntry(String path, RouteFactory factory, RouteGuard guard) {
			this.factory = factory;
			this.guard = guard;

			var regexBuilder = new StringBuilder();
			var params = new ArrayList<String>();
			var dynamicPathCount = 0;

			for (String part : path.split("/")) {
				if (part.startsWith(":")) {
					regexBuilder.append("/([^/]+)");
					params.add(part.substring(1));
					dynamicPathCount++;
				} else {
					regexBuilder.append("/").append(part);
				}
			}

			this.regex = Pattern.compile("^" + regexBuilder.substring(1) + "$");
			this.params = params;
			this.dynamicPathCount = dynamicPathCount;
		}

		Map<String, String> match(String path) {
			var matcher = regex.matcher(path);

			if (!matcher.matches()) {
				return null;
			}

			var paramsMap = new HashMap<String, String>();

			for (var i = 0; i < params.size(); i++) {
				paramsMap.put(params.get(i), matcher.group(i + 1));
			}

			return paramsMap;
		}
	}

	@FunctionalInterface
	public interface RouteFactory {
		/**
		 * A factory that creates a scene. This is useful for creating scenes that
		 * require parameters to be passed to them.
		 *
		 * @param params
		 *            The parameters of the route.
		 * @return The scene to be created.
		 */
		Scene create(Map<String, String> params);
	}

	@FunctionalInterface
	public interface RouteGuard {
		/**
		 * A guard that checks if the user can access the route. This is useful for
		 * checking if the user is logged in or has the required permissions to access
		 * the route.
		 *
		 * @param params
		 *            The parameters of the route.
		 * @return true if the user can access the route, false otherwise.
		 */
		boolean canAccess(Map<String, String> params);
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(SceneManager.class);

	private final JPanel rootContainer;
	private final CardLayout cardLayout;

	private final List<RouteEntry> routeEntries = new ArrayList<>();
	private final HashMap<String, Scene> scenes = new HashMap<>();
	private String currentPath = null;

	public SceneManager() {
		cardLayout = new CardLayout();
		rootContainer = new JPanel(cardLayout);

		LOGGER.setLevel(Level.ALL);
	}

	/**
	 * This <b>DESTROYS</b> everything in this scene manager. Useful for sub scene
	 * managers that are not needed anymore.
	 */
	public void destroy() {
		SwingUtilities.invokeLater(() -> {
			for (var entry : scenes.entrySet()) {
				var scene = entry.getValue();
				cardLayout.removeLayoutComponent(scene.getView());
				scene.onDestroy();
			}

			scenes.clear();
			routeEntries.clear();
			currentPath = null;
			rootContainer.removeAll();
		});
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public JPanel getRootContainer() {
		return rootContainer;
	}

	public Scene getScene(String route) {
		return scenes.get(route);
	}

	/**
	 * Navigate to the scene with the given route. The route is the name of the
	 * scene in lowercase. The route must be unique among all scenes. If the scene
	 * is not registered, an IllegalArgumentException is thrown.
	 *
	 * <p>
	 * If route is the same as the current scene, this method does nothing.
	 *
	 * @param route
	 * @throws IllegalArgumentException
	 *             if the route is not registered
	 */
	public void navigateTo(String path) throws IllegalArgumentException {
		SwingUtilities.invokeLater(() -> {
			LOGGER.fine("Navigating to: " + path);

			Scene oldScene = null;

			if (currentPath == null) {
				currentPath = path;
			} else {
				if (currentPath.equals(path)) {
					return;
				}

				oldScene = scenes.get(currentPath);

				// We do this to prevent the user from navigating away from the current scene
				// if it is not allowed to navigate away. For example, if the user is in a
				// mode where they are editing something, we don't want them to be able to
				// navigate away from the scene without saving or cancelling first.
				if (!oldScene.canNavigateAway()) {
					return;
				}
			}

			for (var entry : routeEntries) {
				var params = entry.match(path);

				if (params != null) {
					if (!entry.guard.canAccess(params)) {
						LOGGER.warning("Cannot access path: " + path);
						return;
					}

					var scene = scenes.get(path);

					if (scene == null) {
						scene = entry.factory.create(params);

						scenes.put(path, scene);

						var view = scene.getView();

						rootContainer.add(view);
						cardLayout.addLayoutComponent(view, path);
						scene.onCreate();
					}

					cardLayout.show(rootContainer, path);

					if (oldScene != null) {
						oldScene.onHide();
					}

					scene.onShow();

					currentPath = path;

					return;
				}
			}

			LOGGER.warning("No route found for path: " + path);
		});
	}

	public void register(String path, RouteFactory factory) {
		routeEntries.add(new RouteEntry(path, factory, _ -> true));
	}

	public void register(String path, RouteFactory factory, RouteGuard guard) {
		routeEntries.add(new RouteEntry(path, factory, guard));
		// To avoid the edge case where the user registers a route with a dynamic path
		// after the static paths, we sort the routes by the number of dynamic paths
		// in descending order. This way, the static paths will be checked first.
		routeEntries.sort(Comparator.comparingInt(e -> e.dynamicPathCount));
	}

	public void unregisterScene(String route) {
		SwingUtilities.invokeLater(() -> {
			var removedScene = scenes.remove(route);

			if (removedScene == null) {
				return;
			}

			removedScene.onDestroy();
			rootContainer.remove(removedScene.getView());
		});
	}
}
