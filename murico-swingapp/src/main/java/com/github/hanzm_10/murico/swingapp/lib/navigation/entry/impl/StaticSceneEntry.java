package com.github.hanzm_10.murico.swingapp.lib.navigation.entry.impl;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.entry.SceneEntry;
import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.StaticSceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.StaticSceneGuard;

public class StaticSceneEntry implements SceneEntry {
    private final String name;
    private final StaticSceneFactory factory;
    private final StaticSceneGuard guard;

    public StaticSceneEntry(@NotNull final String name, @NotNull final StaticSceneFactory factory,
            @NotNull final StaticSceneGuard guard) {
        this.name = name;
        this.factory = factory;
        this.guard = guard;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SceneFactory getSceneFactory() {
        return factory;
    }

    @Override
    public StaticSceneGuard getSceneGuard() {
        return guard;
    }
}
