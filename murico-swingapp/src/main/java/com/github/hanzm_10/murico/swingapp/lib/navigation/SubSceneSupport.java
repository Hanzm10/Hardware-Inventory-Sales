package com.github.hanzm_10.murico.swingapp.lib.navigation;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.managers.SceneManager;

/**
 * <p>
 * The SubSceneSupport interface is used to show a sub scene. A sub scene is a
 * scene that is displayed on top of another scene. It is used to display
 * additional information or options without navigating away from the current
 * scene.
 * </p>
 *
 * <p>
 * Sub scenes are identified by their names, which must be unique among all sub
 * scenes. The name of the sub scene is used to navigate to it.
 * </p>
 *
 * <p>
 * An example of a sub scene name is "home/settings" or
 * "home/settings/advanced".
 * </p>
 * 
 */
public interface SubSceneSupport {
    SceneManager getSceneManager();

    /**
     * This method is used to show a sub scene. The sub scene is identified by its
     * name.
     *
     * @param subSceneName The name of the sub scene.
     * @throws IllegalArgumentException If a sub scene with the provided name does
     *                                  not exist.
     */
    void showSubScene(@NotNull final String subSceneName) throws IllegalArgumentException;
}
