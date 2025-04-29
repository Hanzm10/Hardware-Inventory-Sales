package com.github.hanzm_10.murico.swingapp.lib.navigation;

import org.jetbrains.annotations.NotNull;

public record ParsedSceneName(@NotNull String parentSceneName, String subSceneName) {
    public static final String SEPARATOR = "/";

    public static ParsedSceneName parse(@NotNull String sceneName) {
        if (sceneName.isEmpty()) {
            return new ParsedSceneName("", null);
        }

        int index = sceneName.indexOf(SEPARATOR);

        if (index == -1) {
            return new ParsedSceneName(sceneName, null);
        }

        String parentSceneName = sceneName.substring(0, index);
        String subSceneName = sceneName.substring(index + 1);

        return new ParsedSceneName(parentSceneName, subSceneName);
    }

}
