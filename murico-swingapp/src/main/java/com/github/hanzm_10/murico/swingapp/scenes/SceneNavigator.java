package com.github.hanzm_10.murico.swingapp.scenes;

import com.github.hanzm_10.murico.swingapp.scenes.Scene.SubSceneSupport;

/**
 * The SceneNavigator class is responsible for navigating between scenes in the application. It uses
 * the SceneManager to manage the scenes and their navigation.
 *
 * <p>
 * The SceneNavigator is a singleton class, which means that there is only one instance of it in the
 * application. This is done to ensure that there is only one point of control for navigating
 * between scenes.
 * </p>
 * 
 * <p>
 * P.S. I did this to avoid Dependency Injection and have a way to manage stuff like data fetching
 * properly.
 * </p>
 *
 * @author Aaron Ragudos
 */
public class SceneNavigator {
    private static SceneManager sceneManager;

    public static void navigateTo(String pathName) {
        if (sceneManager == null) {
            throw new IllegalStateException(
                    "SceneManager is not set. Please set it before navigating.");
        }

        var pathParts = pathName.split("/", 2);
        var sceneName = pathParts[0];

        sceneManager.navigateTo(sceneName);

        if (pathParts.length == 1) {
            return;
        }

        var subSceneName = pathParts[1];

        if (subSceneName == null) {
            return;
        }

        if (sceneManager.getScene(sceneName) instanceof SubSceneSupport subSceneSupport) {
            subSceneSupport.showSubScene(subSceneName);
        }
    }

    public static void setSceneManager(SceneManager sceneManager) {
        SceneNavigator.sceneManager = sceneManager;
    }
}
