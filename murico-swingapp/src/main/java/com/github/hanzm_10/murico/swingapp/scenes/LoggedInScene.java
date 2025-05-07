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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.lib.exceptions.MuricoError;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.utils.SessionUtils;
import com.github.hanzm_10.murico.swingapp.service.database.SessionService;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;

import net.miginfocom.swing.MigLayout;

public class LoggedInScene implements Scene {
	public static class LoggedInSceneGuard implements SceneGuard {
		@Override
		public boolean canAccess() {
			return SessionManager.getInstance().getSession() != null
					&& !SessionUtils.isSessionExpired(SessionManager.getInstance().getSession());
		}
	}

	public static final SceneGuard GUARD = new LoggedInSceneGuard();

	protected JPanel view;

	// just a temp mechanism so that we can go back to auth page
	protected JPanel containerJPanel;
	protected JButton logoutBtn;
	protected JButton goToLoginBtn;

	@Override
	public String getSceneName() {
		return "home";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
		containerJPanel = new JPanel();
		goToLoginBtn = new JButton("Go to login! :)");
		logoutBtn = StyledButtonFactory.createButton("Get me outta here! :>", ButtonStyles.DANGER);

		goToLoginBtn.addActionListener(_ -> {
			SceneNavigator.navigateTo("auth/login");
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view), "HA! Got EM! (Click logout <3)",
						"HEHE", JOptionPane.ERROR_MESSAGE);
			});
		});

		logoutBtn.addActionListener(_ -> {
			try {
				SessionService.logout();
				SceneNavigator.navigateTo("auth/login");
			} catch (MuricoError e) {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view), e.getMessage(), "ERROR!",
							JOptionPane.ERROR_MESSAGE);
				});
			}
		});

		containerJPanel.setLayout(new MigLayout("", "[center]", "[center]"));
		containerJPanel.add(new JLabel("You're logged in! Congratulations"), "wrap");
		containerJPanel.add(goToLoginBtn, "wrap");
		containerJPanel.add(logoutBtn);

		view.setLayout(new MigLayout("", "[grow, center]", "[grow, center]"));
		view.add(containerJPanel);
	}

	@Override
	public boolean onDestroy() {
		return Scene.super.onDestroy();
	}
}
