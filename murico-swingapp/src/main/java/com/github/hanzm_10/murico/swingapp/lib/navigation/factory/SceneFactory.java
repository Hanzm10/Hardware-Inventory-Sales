package com.github.hanzm_10.murico.swingapp.lib.navigation.factory;

import com.github.hanzm_10.murico.swingapp.lib.navigation.Scene;

/**
 * A functional interface for creating scenes in a Java Swing application. This
 * interface is used to define a factory method for creating scenes.
 * 
 * Useful for lazy loading scenes or for creating scenes with specific
 * configurations.
 * 
 * @see Scene
 * @see SceneManager
 */
@FunctionalInterface
public interface SceneFactory {
    Scene createScene();
}
