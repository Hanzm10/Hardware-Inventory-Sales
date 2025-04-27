package com.github.hanzm_10.murico.swingapp.lib.navigation.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.DynamicSceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.DynamicSceneGuard;

public class DynamicSceneEntry extends SceneEntry {
    private Pattern regexPattern;
    private List<String> params;

    private int dynamicParamCount;

    public static final String DYNAMIC_PARAM_PREFIX = ":";

    public DynamicSceneEntry(final String name, final DynamicSceneFactory factory, final DynamicSceneGuard guard)
            throws IllegalArgumentException {
        super(name, factory, guard);

        var regexPatternBuilder = new StringBuilder();
        var params = new ArrayList<String>();
        var dynamicParamCount = 0;

        for (var subSceneName : name.split("/")) {
            // happens when name is "home//settings"
            if (subSceneName.isBlank()) {
                throw new IllegalArgumentException("Scene name cannot be empty");
            }

            if (subSceneName.startsWith(DYNAMIC_PARAM_PREFIX)) {
                regexPatternBuilder.append("/([^/]+)");
                params.add(subSceneName.substring(DYNAMIC_PARAM_PREFIX.length()));
                dynamicParamCount++;
            } else {
                regexPatternBuilder.append("/").append(subSceneName);
            }
        }

        this.regexPattern = Pattern.compile("^" + regexPatternBuilder.toString() + "$");
        this.params = params;
        this.dynamicParamCount = dynamicParamCount;
    }

    Map<String, String> match(String sceneName) {
        var matcher = regexPattern.matcher(sceneName);

        if (!matcher.matches()) {
            return null;
        }

        var paramsMap = new java.util.HashMap<String, String>();

        for (var i = 0; i < dynamicParamCount; i++) {
            paramsMap.put(params.get(i), matcher.group(i + 1));
        }

        return paramsMap;
    }

}
