package com.github.hanzm_10.murico.swingapp.lib.navigation.managers;

import java.awt.CardLayout;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;

/**
 * Interface for managing scene navigation in a Java Swing application.
 * Implementations should provide the logic for handling different scenes.
 */
public interface SceneManager {
    /**
     * Checks if the scene manager can handle the specified scene.
     * 
     * For example, if the scene is dynamic, a static scene manager should return
     * false.
     *
     * @param sceneName The name of the scene to check.
     * @return true if the scene can be handled, false otherwise.
     */
    boolean canHandle(String sceneName);

    /**
     * <strong>DESTROYS</strong> the scene manager and releases any resources it
     * holds.
     * 
     * This method should be called when the scene manager is no longer needed. It
     * should clean up any resources, listeners, or references to prevent memory
     * leaks.
     * 
     * @return true if the destruction was successful, false otherwise.
     */
    boolean destroy();

    /**
     * @return The root container for the scene manager.
     */
    JPanel getRootContainer();

    /**
     * @return The name of the current scene.
     */
    String getCurrentSceneName();

    /**
     * @return The CardLayout associated with this scene manager.
     */
    CardLayout getCardLayout();

    /**
     * Navigates to the specified scene.
     * 
     * This method should contain the logic for transitioning to the new scene.
     *
     * @param sceneName The name of the scene to navigate to.
     * @return true if the navigation was successful, false otherwise.
     */
    boolean navigateTo(String sceneName);

    /**
     * Registers a scene with the scene manager.
     * 
     * This method should be called to add a new scene to the manager's
     * capabilities.
     *
     * @param sceneName    The name of the scene to register.
     * @param sceneFactory The factory responsible for creating the scene.
     * @return true if the registration was successful, false otherwise.
     */
    boolean registerScene(String sceneName, SceneFactory sceneFactory);

    /**
     * Registers a scene with a guard.
     * 
     * This method should be called to add a new scene to the manager's capabilities
     * with an associated guard.
     *
     * @param sceneName    The name of the scene to register.
     * @param sceneFactory The factory responsible for creating the scene.
     * @param sceneGuard   The guard responsible for checking conditions before
     *                     navigating to the scene.
     * @return true if the registration was successful, false otherwise.
     */
    boolean registerScene(String sceneName, SceneFactory sceneFactory, SceneGuard sceneGuard);

    /**
     * Unregisters a scene from the scene manager.
     * 
     * This method should be called to remove a scene from the manager's
     * capabilities.
     *
     * @param sceneName The name of the scene to unregister.
     * @return true if the unregistration was successful, false otherwise.
     */
    boolean unregisterScene(String sceneName);
}
