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
package com.github.hanzm_10.murico.swingapp.lib.navigation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;

class FirstChild implements Scene {
	JPanel view;

	@Override
	public String getSceneName() {
		return "first-child";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
		view.add(new JLabel("I am the first child!"));
	}

	@Override
	public void onHide() {
		System.out.println("\n ==== First child is hiding! ==== \n");
	}
}

class SecondChild implements Scene, SubSceneSupport {
	SceneManager sceneManager;
	JPanel view;

	private void createSceneManager() {
		sceneManager = new StaticSceneManager();

		sceneManager.registerScene("first-child", () -> new FirstChild());
	}

	@Override
	public SceneManager getSceneManager() {
		if (sceneManager == null) {
			createSceneManager();
		}

		return sceneManager;
	}

	@Override
	public String getSceneName() {
		return "second-child";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public boolean navigateTo(@NotNull String subSceneName) {
		return sceneManager.navigateTo(subSceneName);
	}

	@Override
	public void navigateToDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate() {

		view.add(new JLabel("I am harboring the first child! "));
		view.add(sceneManager.getRootContainer());
		view.add(new JLabel("I am the second child!"));

		SceneNavigator.getInstance().navigateTo("TestDummyScene/second-child/first-child");
	}

	@Override
	public void onHide() {
		System.out.println("\n ==== Second child is hiding! ==== \n");
	}
}

class TestDummySceneParent implements Scene, SubSceneSupport {
	SceneManager sceneManager;
	JPanel view;

	private void createSceneManager() {
		sceneManager = new StaticSceneManager();

		sceneManager.registerScene("first-child", () -> new FirstChild());
		sceneManager.registerScene("second-child", () -> new SecondChild());
	}

	@Override
	public SceneManager getSceneManager() {
		if (sceneManager == null) {
			createSceneManager();
		}

		return sceneManager;
	}

	@Override
	public String getSceneName() {
		return "TestDummyScene";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public boolean navigateTo(@NotNull String subSceneName) {
		return sceneManager.navigateTo(subSceneName);
	}

	@Override
	public void navigateToDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate() {
		var container = new JPanel(new FlowLayout());

		var btn1 = new JButton("Go to sub scene 1");
		var btn2 = new JButton("Go to sub scene 2");

		btn1.addActionListener(_ -> {
			SceneNavigator.getInstance().navigateTo(getSceneName() + "/first-child");
		});

		btn2.addActionListener(_ -> {
			SceneNavigator.getInstance().navigateTo(getSceneName() + "/second-child");
		});

		view.setLayout(new GridLayout());

		container.add(btn1);
		container.add(btn2);

		view.add(new JLabel("I am the parent!"));
		view.add(container);
		view.add(sceneManager.getRootContainer());
		SceneNavigator.getInstance().navigateTo("TestDummyScene/first-child");
	}
}

public class TestSceneManager {

	public static void main(String[] args) {
		var frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Test Scene Manager");
		SceneManager sceneManager = new StaticSceneManager();

		sceneManager.registerScene("TestDummyScene", () -> new TestDummySceneParent());

		SceneNavigator.getInstance().initialize(sceneManager);

		var rootContainer = sceneManager.getRootContainer();

		rootContainer.setPreferredSize(new Dimension(1280, 720));

		frame.add(rootContainer);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		SceneNavigator.getInstance().navigateTo("TestDummyScene");
	}
}
