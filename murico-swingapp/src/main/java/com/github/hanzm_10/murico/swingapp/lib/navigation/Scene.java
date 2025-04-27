package com.github.hanzm_10.murico.swingapp.lib.navigation;

import com.github.hanzm_10.murico.swingapp.scenes.SceneManager;

/**
 * <p>
 * The base interface for all scenes in the application. A scene is a view that
 * can be navigated to and from. It is responsible for creating its own view and
 * handling its own events.
 * </p>
 * 
 * <p>
 * Scenes are registered with the {@link SceneManager}, which is responsible for
 * managing the navigation between scenes.
 * </p>
 * 
 * <p>
 * Scenes are identified by their names, which must be unique among all scenes.
 * The name of the scene is used to navigate to it.
 * 
 * An example of a scene name is "home" or "settings". The name must be in lower
 * case. For sub scenes, the name must be in lower case and separated by
 * slashes. An example of a sub scene name is "home/settings" or
 * "home/settings/advanced".
 * </p>
 * 
 * There are four life-cycle methods that are called in the following order:
 * 
 * <ul>
 * <li>onCreate: Called when the scene is created. This is where the view is
 * created and initialized.</li>
 * <li>onShow: Called when the scene is shown. This is where the scene should be
 * updated. This is where the scene should be updated with data.</li>
 * <li>onBeforeHide: Called before the scene is hidden. This is where the scene
 * should save its state and such.</li>
 * <li>onHide: Called when the scene is hidden. This is where the scene should
 * stop all its operations if there are any.</li>
 * <li>onDestroy: Called when the scene is destroyed. This is where the scene
 * should be cleaned up and reset all its state.</li>
 * </ul>
 */
public interface Scene {
    /**
     * This is useful for when a scene is <code>dirty</code> or has a sub scene that
     * is <code>dirty</code>. Dirty means that the scene has unsaved changes that
     * need to be saved before navigating away from it.
     * 
     * @return true if the scene is dirty, false otherwise.
     */
    default boolean canNavigateAway() {
        return true;
    }

    /**
     * Useful for when a scene is <code>dirty</code> or has a sub scene that is
     * <code>dirty</code>.
     * 
     * Possibly save the state of the scene or sub scene before navigating away from
     * it, or prompt the user to save the state of the scene or sub scene before
     * navigating away from it.
     */
    void onBeforeHide();

    /**
     * The name of the scene. This is used to identify the scene in the
     * {@link SceneManager}. This name must be unique among all scenes.
     * 
     * @return The name of the scene in lower case.
     */
    String getName();

    /**
     * This hook is useful to lazily initialize the contents of the scene.
     * 
     * @return true if the scene was created successfully, false otherwise.
     */
    boolean onCreate();

    /**
     * This hook is useful so the scene can have knowledge of when it's shown.
     */
    void onShow();

    /**
     * This hook is useful for cleanup tasks when we want to remove the scene.
     * 
     * @return true if the scene was destroyed successfully, false otherwise.
     */
    boolean onDestroy();

    /**
     * This hook is useful so the scene can have knowledge of when it's hidden.
     */
    void onHide();
}
