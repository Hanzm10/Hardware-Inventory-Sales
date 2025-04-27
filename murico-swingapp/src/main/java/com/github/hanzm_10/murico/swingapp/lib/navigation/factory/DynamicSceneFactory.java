package com.github.hanzm_10.murico.swingapp.lib.navigation.factory;

import java.util.Map;

import com.github.hanzm_10.murico.swingapp.lib.navigation.Scene;

/**
 * <p>
 * The DynamicSceneFactory interface is used to create scenes dynamically. This
 * is useful for scenes that require parameters to be created, such as a scene
 * that displays a specific item.
 * </p>
 *
 * <p>
 * In the scene name "item/:itemId", the ":itemId" part is a parameter that will
 * be replaced with the actual item ID when the scene is created. For example,
 * 
 * "item/123" will create a scene with the item ID of 123.
 * </p>
 * 
 * <pre>
 * sceneManager.registerScene("item/:itemId", _ -> new ItemScene(params.get("itemId")));
 * int latestItemId = dbCall.getLatestItemId();
 * sceneManager.navigateTo("item/" + latestItemId);
 * </pre>
 */
@FunctionalInterface
public interface DynamicSceneFactory extends SceneFactory {
    Scene createScene(Map<String, ? extends Object> params);

    @Override
    default Scene createScene() {
        return createScene(Map.of());
    }
}
