package com.github.hanzm_10.murico.swingapp.lib.navigation.entry.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.github.hanzm_10.murico.swingapp.lib.navigation.entry.SceneEntry;
import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.DynamicSceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.DynamicSceneGuard;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;

public class DynamicSceneEntry implements SceneEntry {
    private final String name;
    private final DynamicSceneFactory factory;
    private final DynamicSceneGuard guard;

    private Pattern regexPattern;
    private List<String> params;

    private int dynamicParamCount;

    public static final String DYNAMIC_PARAM_PREFIX = ":";

    public DynamicSceneEntry(final String name, final DynamicSceneFactory factory, final DynamicSceneGuard guard) {
        this.name = name;
        this.factory = factory;
        this.guard = guard;

        var regexPatternBuilder = new StringBuilder();
        var params = new ArrayList<String>();
        var dynamicParamCount = 0;

        for (var subSceneName : name.split("/")) {
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

    @Override
    public String getName() {
        return name;
    }

    Map<String, ? extends Object> match(String sceneName) {
        var matcher = regexPattern.matcher(sceneName);

        if (!matcher.matches()) {
            return null;
        }

        var paramsMap = new java.util.HashMap<String, Object>();

        for (var i = 0; i < dynamicParamCount; i++) {
            paramsMap.put(params.get(i), matcher.group(i + 1));
        }

        return paramsMap;
    }

    @Override
    public SceneFactory getSceneFactory() {
        // TODO Auto-generated method stub
        return factory;
    }

    @Override
    public SceneGuard getSceneGuard() {
        // TODO Auto-generated method stub
        return guard;
    }

}
