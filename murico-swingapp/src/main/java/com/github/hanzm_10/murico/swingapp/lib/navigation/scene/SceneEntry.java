package com.github.hanzm_10.murico.swingapp.lib.navigation.scene;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;

public record SceneEntry(@NotNull SceneFactory sceneFactory, @NotNull SceneGuard sceneGuard) {
}
