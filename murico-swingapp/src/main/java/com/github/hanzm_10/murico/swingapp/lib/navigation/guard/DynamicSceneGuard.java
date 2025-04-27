package com.github.hanzm_10.murico.swingapp.lib.navigation.guard;

import java.util.Map;

@FunctionalInterface
public interface DynamicSceneGuard extends SceneGuard {
    boolean canAccess(Map<String, ? extends Object> params);

    @Override
    default boolean canAccess() {
        return canAccess(Map.of());
    }
}
