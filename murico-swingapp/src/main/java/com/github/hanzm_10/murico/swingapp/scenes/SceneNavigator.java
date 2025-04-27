/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.scenes;

import com.github.hanzm_10.murico.swingapp.scenes.Scene.SubSceneSupport;

/**
 * The SceneNavigator class is responsible for navigating between scenes in the
 * application. It uses the SceneManager to manage the scenes and their
 * navigation.
 *
 * <p>
 * The SceneNavigator is a singleton class, which means that there is only one
 * instance of it in the application. This is done to ensure that there is only
 * one point of control for navigating between scenes.
 * </p>
 * 
 * <p>
 * P.S. I did this to avoid Dependency Injection and have a way to manage stuff
 * like data fetching properly.
 * </p>
 *
 * @author Aaron Ragudos
 */
public class SceneNavigator {
    private static SceneManager sceneManager;

    public static void navigateTo(String pathName) {
        if (sceneManager == null) {
            throw new IllegalStateException("SceneManager is not set. Please set it before navigating.");
        }

        var pathParts = pathName.split("/", 2);
        var sceneName = pathParts[0];

        sceneManager.navigateTo(sceneName);

        if (pathParts.length == 1) {
            return;
        }

        var subSceneName = pathParts[1];

        if (subSceneName == null) {
            return;
        }

        if (sceneManager.getDynamicScene(sceneName) instanceof SubSceneSupport subSceneSupport) {
            subSceneSupport.showSubScene(subSceneName);
        }
    }

    public static void setSceneManager(SceneManager sceneManager) {
        SceneNavigator.sceneManager = sceneManager;
    }
}
