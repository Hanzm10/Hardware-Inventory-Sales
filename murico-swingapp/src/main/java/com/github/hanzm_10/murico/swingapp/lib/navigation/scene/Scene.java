package com.github.hanzm_10.murico.swingapp.lib.navigation.scene;

import javax.swing.JPanel;

public interface Scene {
    String getSceneName();

    JPanel getSceneView();

    default boolean supportsSubScenes() {
        return this instanceof SubSceneSupport;
    }

    default boolean canShow() {
        return true;
    }

    default void onBeforeShow() {
    }

    default void onShow() {
    }

    default boolean canHide() {
        return true;
    }

    default void onCannotHide() {
    }

    default void onBeforeHide() {
    }

    default void onHide() {
    }

    void onCreate();

    default boolean onDestroy() {
        return true;
    }

    default Scene getSelf() {
        return this;
    }

}
