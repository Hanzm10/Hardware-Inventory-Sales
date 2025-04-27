package com.github.hanzm_10.murico.swingapp.lib.navigation.entry;

import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;

public class SceneEntry {
    protected final String name;
    protected final SceneFactory factory;
    protected final SceneGuard guard;

    public SceneEntry(final String name, final SceneFactory factory, final SceneGuard guard) {
        this.name = name;
        this.factory = factory;
        this.guard = guard;
    }

    String getName() {
        return name;
    }

    SceneFactory getSceneFactory() {
        return factory;
    }

    SceneGuard getSceneGuard() {
        return guard;
    }
}
