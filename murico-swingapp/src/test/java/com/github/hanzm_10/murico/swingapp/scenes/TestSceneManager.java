package com.github.hanzm_10.murico.swingapp.scenes;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestSceneManager {

    public static void main(String[] args) {
        var frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Test Scene Manager");
        SceneManager sceneManager = new SceneManager();

        frame.setContentPane(sceneManager.getRootContainer());

        frame.setSize(1280, 768);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        sceneManager.registerDynamic("scene1", _ -> new TestScene1());
        sceneManager.registerDynamic("scene2", _ -> new TestScene2());
        sceneManager.registerDynamic("scene3", _ -> new TestScene3());

        SceneNavigator.setSceneManager(sceneManager);

        sceneManager.navigateTo("scene1");
    }
}

class TestScene1 implements Scene {
    JPanel view;

    @Override
    public String getName() {
        return "scene1";
    }

    @Override
    public JPanel getView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        view.add(new JLabel("Scene 1"));
        var btn = new JButton("Switch to Scene 2");

        btn.addActionListener(e -> {
            SceneNavigator.navigateTo("scene2");
        });

        view.add(btn);
    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub

    }

}

class TestScene2 implements Scene {
    JPanel view;

    @Override
    public String getName() {
        return "scene2";
    }

    @Override
    public JPanel getView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        view.add(new JLabel("Scene 2"));
        var btn = new JButton("Switch to Scene 3");

        btn.addActionListener(e -> {
            SceneNavigator.navigateTo("scene3");
        });

        view.add(btn);
    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub

    }
}

class TestScene3 implements Scene {
    JPanel view;

    @Override
    public String getName() {
        return "scene3";
    }

    @Override
    public JPanel getView() {
        return view == null ? (view = new JPanel()) : view;
    }

    @Override
    public void onCreate() {
        view.add(new JLabel("Scene 3"));
        var btn = new JButton("Switch to Scene 1");
        btn.addActionListener(e -> {
            SceneNavigator.navigateTo("scene1");
        });

        view.add(btn);

    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub

    }
}
