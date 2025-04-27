package com.github.hanzm_10.murico.swingapp.lib.navigation.entry;

import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;

public interface SceneEntry {
    String getName();

    SceneFactory getSceneFactory();

    SceneGuard getSceneGuard();
}
