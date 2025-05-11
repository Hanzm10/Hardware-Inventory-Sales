package com.github.hanzm_10.murico.swingapp.ui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.lib.exceptions.MuricoError;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.service.database.SessionService;

public class LogoutButton extends JButton implements ActionListener {
	private Thread logoutThread;

	public LogoutButton() {
		super();

		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (logoutThread != null && logoutThread.isAlive()) {
			return;
		}

		logoutThread = new Thread(() -> {
			try {
				SessionService.logout();
				SceneNavigator.getInstance().navigateTo("auth/login");
			} catch (MuricoError e) {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), e.getMessage(),
							"Failed to log out", JOptionPane.ERROR_MESSAGE);
				});
			}
		});

		logoutThread.start();
	}

	public void destroy() {
		if (logoutThread != null) {
			logoutThread.interrupt();
		}
	}
}
