package com.github.hanzm_10.murico.swingapp.lib.navigation;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

public class TestDummySceneParent implements Scene {
    protected SceneManager sceneManager;
    JPanel view;

    @Override
    public String getSceneName() {
        return "TestDummyScene";
    }

    @Override
    public JPanel getSceneView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        sceneManager = new StaticSceneManager();

        sceneManager.registerScene("test1", () -> new TestDummyScene(1, e -> sceneManager.navigateTo("test2")));
        sceneManager.registerScene("test2", () -> new TestDummyScene(2, e -> sceneManager.navigateTo("test3")));
        sceneManager.registerScene("test3", () -> new TestDummyScene(3, e -> sceneManager.navigateTo("test1")));

        sceneManager.navigateTo("test1");

        view.add(sceneManager.getRootContainer());
    }

}
