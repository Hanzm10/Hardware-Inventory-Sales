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

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.ImagePanel;

import net.miginfocom.swing.MigLayout;

public class MainAuthScene implements Scene {
	private static final Logger LOGGER = MuricoLogger.getLogger(MainAuthScene.class);

	protected JPanel view;
	protected MigLayout layout;
	protected ImagePanel imagePanel;
	protected JButton loginButton;
	protected JButton registerButton;

	// for testing purposes
	public static void main(String[] args) {
		MainAuthScene scene = new MainAuthScene();

		SwingUtilities.invokeLater(() -> {
			var frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("Main Auth Scene");

			frame.getContentPane().setLayout(new MigLayout("", "[grow,center]", "[grow,center]"));
			frame.getContentPane().setPreferredSize(Styles.DEFAULT_DIMENSIONS);
			frame.getContentPane().setBackground(Color.CYAN);

			var sView = scene.getSceneView();
			scene.onCreate();

			sView.setVisible(true);

			frame.add(sView, "cell 0 0");
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

	@Override
	public String getSceneName() {
		return "main";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
		Image image;

		try {
			image = AssetManager.getOrLoadImage("images/auth_main.png");
		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Failed to load image", e);
			return;
		}

		layout = new MigLayout("", "[grow,right][64px,center][grow,left]",
				"[:320.00:520px,grow 50,bottom][50px,grow,top]");

		imagePanel = new ImagePanel(image);

		loginButton = StyledButtonFactory.createButton("Log In", ButtonStyles.TERTIARY, 280, 50);
		registerButton = StyledButtonFactory.createButton("Create an account", ButtonStyles.TERTIARY, 280, 50);

		view.setLayout(layout);

		view.add(imagePanel, "cell 0 0 3 1, grow");
		view.add(loginButton, "cell 0 1");
		view.add(registerButton, "cell 2 1");
	}
}
