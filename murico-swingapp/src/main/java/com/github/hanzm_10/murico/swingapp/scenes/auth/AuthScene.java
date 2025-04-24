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

        subSceneManager.register("main", _ -> new MainScene(), authSceneGuard);
        subSceneManager.register("login", _ -> new LoginScene(), authSceneGuard);
        subSceneManager.register("register", _ -> new RegisterScene(), authSceneGuard);
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
    public void onHide() {
        // TODO Auto-generated method stub
        view.setVisible(false);
    }

    @Override
    public void onShow() {
        view.setVisible(true);
    }

    @Override
    public void showSubScene(String path) throws IllegalArgumentException {
        System.out.println("AuthScene.showSubScene: " + path);
        // No need to check for more nested sub scenes
        // since auth's sub scenes doesn't have any.
        subSceneManager.navigateTo(path);
    }


}
