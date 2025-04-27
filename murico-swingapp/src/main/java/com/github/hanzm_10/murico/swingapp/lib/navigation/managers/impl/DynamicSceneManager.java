package com.github.hanzm_10.murico.swingapp.lib.navigation.managers.impl;

import java.awt.CardLayout;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;
import com.github.hanzm_10.murico.swingapp.lib.navigation.managers.SceneManager;

public class DynamicSceneManager implements SceneManager {

    @Override
    public boolean canHandle(String sceneName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean destroy() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public JPanel getRootContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCurrentSceneName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CardLayout getCardLayout() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean navigateTo(String sceneName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean registerScene(String sceneName, SceneFactory sceneFactory) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean registerScene(String sceneName, SceneFactory sceneFactory, SceneGuard sceneGuard) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unregisterScene(String sceneName) {
        // TODO Auto-generated method stub
        return false;
    }

}
