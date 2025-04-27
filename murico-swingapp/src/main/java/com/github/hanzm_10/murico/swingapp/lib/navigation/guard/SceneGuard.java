package com.github.hanzm_10.murico.swingapp.lib.navigation.guard;

/**
 * <p>
 * The SceneGuard interface is used to determine if a scene can be accessed.
 * This is useful for scenes that require a certain condition to be met before
 * they can be accessed.
 * </p>
 * 
 * <p>
 * This interface is used by the SceneManager to determine if a scene can be
 * accessed. If the scene cannot be accessed, the SceneManager will not allow
 * the scene to be shown.
 * </p>
 */
@FunctionalInterface
public interface SceneGuard {
    boolean canAccess();
}
