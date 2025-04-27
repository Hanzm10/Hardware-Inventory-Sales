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
package com.github.hanzm_10.murico.swingapp.scenes.auth;

import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.scenes.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.Scene.SubSceneSupport;
import com.github.hanzm_10.murico.swingapp.scenes.SceneManager;

public class AuthScene implements Scene, SubSceneSupport {
    private static class AuthSceneGuard implements SceneManager.RouteGuard {
        @Override
        public boolean canAccess(Map<String, String> params) {
            return true;
        }
    }

    private static final Logger LOGGER = MuricoLogger.getLogger(AuthScene.class);
    public static final AuthSceneGuard authSceneGuard = new AuthSceneGuard();

    private JPanel view;
    private SceneManager subSceneManager;

    public AuthScene() {
        subSceneManager = new SceneManager();

        subSceneManager.registerDynamic("main", _ -> new MainScene(), authSceneGuard);
        subSceneManager.registerDynamic("login", _ -> new LoginScene(), authSceneGuard);
        subSceneManager.registerDynamic("register", _ -> new RegisterScene(), authSceneGuard);
    }

    @Override
    public String getName() {
        return "auth";
    }

    @Override
    public SceneManager getSubSceneManager() {
        return subSceneManager;
    }

    @Override
    public JPanel getView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        if (view == null) {
            LOGGER.severe("AuthScene view is null");
            return;
        }

        view.add(subSceneManager.getRootContainer());
        subSceneManager.navigateTo("main");
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onHide() {
    }

    @Override
    public void onShow() {
    }

    @Override
    public void showSubScene(String path) throws IllegalArgumentException {
        // No need to check for more nested sub scenes
        // since auth's sub scenes doesn't have any.
        subSceneManager.navigateTo(path);
    }
}
