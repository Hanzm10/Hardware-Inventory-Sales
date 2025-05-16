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
package com.github.hanzm_10.murico.swingapp.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.constants.Metadata;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.scenes.auth.AuthScene;
import com.github.hanzm_10.murico.swingapp.scenes.home.HomeScene;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame {
	private class MainFrameWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			// TODO: If a user is performing a task, ask if they want to save their progress
			// before closing the application
			AbandonedConnectionCleanupThread.checkedShutdown();
			SceneNavigator.getInstance().destroy();
			dispose();
		}
	}

	@SuppressWarnings("unused")
	private static final Logger LOGGER = MuricoLogger.getLogger(MainFrame.class);

	public MainFrame() {

		addWindowListener(new MainFrameWindowListener());

		setTitle(Metadata.APP_TITLE + " " + Metadata.APP_VERSION);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		initSceneManager();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initSceneManager() {
		var sceneManager = new StaticSceneManager();

		sceneManager.registerScene("auth", () -> new AuthScene(), AuthScene.GUARD);
		sceneManager.registerScene("home", () -> new HomeScene(), HomeScene.GUARD);

		var sceneNavigator = SceneNavigator.getInstance();

		sceneNavigator.initialize(sceneManager);

		if (SessionManager.getInstance().getSession() == null) {
			sceneNavigator.navigateTo("auth");
		} else {
			sceneNavigator.navigateTo("home");
		}

		var rootContainer = sceneManager.getRootContainer();
		var wrapper = new JPanel();

		wrapper.setLayout(new MigLayout("insets 16", "[grow]", "[grow]"));
		wrapper.setPreferredSize(Styles.DEFAULT_DIMENSIONS);
		wrapper.setSize(Styles.DEFAULT_DIMENSIONS);

		wrapper.add(rootContainer, "grow");

		add(wrapper);
	}
}
