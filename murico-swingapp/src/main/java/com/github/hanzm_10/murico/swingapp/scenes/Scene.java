package com.github.hanzm_10.murico.swingapp.scenes;

import javax.swing.JPanel;

/**
 * <p>
 * The Scene interface represents a scene in the application. A scene is a view that can be
 * navigated to and from. It is responsible for creating its own view and handling its own events.
 * </p>
 * 
 * <p>
 * Scenes are registered with the SceneManager, which is responsible for managing the navigation
 * between scenes.
 * </p>
 * 
 * <p>
 * Scenes are identified by their names, which must be unique among all scenes. The name of the
 * scene is used to navigate to it.
 * </p>
 * 
 * The lifecycle of a scene is as follows:
 * <ul>
 * <li>onCreate: Called when the scene is created. This is where the view is created and
 * initialized.</li>
 * <li>onShow: Called when the scene is shown. This is where the scene should be updated with
 * data.</li>
 * <li>onHide: Called when the scene is hidden. This is where the scene should stop all its
 * operations if there are any.</li>
 * <li>onDestroy: Called when the scene is destroyed. This is where the scene should be cleaned up
 * and reset all its state.</li>
 * 
 * @author Aaron Ragudos
 */
public interface Scene {
    public interface SubSceneSupport {
        public SceneManager getSubSceneManager();

        /**
         * This method is used to show a sub scene. The sub scene is identified by its path.
         * 
         * @param path The path of the sub scene.
         * @throws IllegalArgumentException If a sub scene in the provided path does not exist.
         */
        public void showSubScene(String path) throws IllegalArgumentException;
    }

    default public boolean canNavigateAway() {
        return true;
    }

    /**
     * The name of the scene. This is used to identify the scene in the scene manager. This name
     * must be unique among all scenes.
     *
     * @return The name of the scene in lowercase.
     */
    public String getName();

    /**
     * Only creates the view, does not add its contents.
     *
     * @return The view of the scene.
     */
    public JPanel getView();

    /*
     * For scenes that have sub scenes, this is where it should declare its default sub scene and
     * not in its constructor.
     */
    public void onCreate();

    default public void onDestroy() {
        // Default implementation does nothing
    }

    public void onHide();

    public void onShow();
}
