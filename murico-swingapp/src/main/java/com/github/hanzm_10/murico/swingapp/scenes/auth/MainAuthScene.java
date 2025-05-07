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

import java.awt.Image;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.ImagePanel;

import net.miginfocom.swing.MigLayout;

public class MainAuthScene implements Scene {
	private static final Logger LOGGER = MuricoLogger.getLogger(MainAuthScene.class);

	protected ButtonSceneNavigatorListener btnListener = new ButtonSceneNavigatorListener(new AtomicBoolean(false));

	protected JPanel view;
	protected MigLayout layout;
	protected Image image;
	protected ImagePanel imagePanel;
	protected JButton loginButton;

	protected JButton registerButton;

	private void attachComponents() {
		if (imageLoaded()) {
			imagePanel = new ImagePanel(image);
			view.add(imagePanel, "cell 0 0 2");
		}

		view.add(loginButton, "cell 0 1");
		view.add(registerButton, "cell 1 1");
	}

	private void attachListeners() {
		loginButton.addActionListener(btnListener);
		registerButton.addActionListener(btnListener);

		loginButton.setActionCommand("auth/login");
		registerButton.setActionCommand("auth/register");
	}

	private void createComponents() {
		loginButton = StyledButtonFactory.createButton("Log In", ButtonStyles.TERTIARY, 280, 50);
		registerButton = StyledButtonFactory.createButton("Create an account", ButtonStyles.TERTIARY, 280, 50);
	}

	@Override
	public String getSceneName() {
		return "main";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private boolean imageLoaded() {
		try {
			image = AssetManager.getOrLoadImage("images/auth_main.png");
			return true;
		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Failed to load image", e);
		}

		return false;
	}

	@Override
	public void onCreate() {
		setViewLayout();
		createComponents();
		attachListeners();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		loginButton.removeActionListener(btnListener);
		registerButton.removeActionListener(btnListener);
		view.removeAll();
		loginButton = null;
		registerButton = null;
		image = null;
		imagePanel = null;
		view.setLayout(null);
		layout = null;
		view = null;

		return true;
	}

	private void setViewLayout() {
		layout = new MigLayout("", "[::280px,grow,right]64[::280px,grow,left]",
				"[240px::315px, bottom]24[50px::50px, top]");
		view.setLayout(layout);
	}
}
