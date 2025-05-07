package com.github.hanzm_10.murico.swingapp.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;

public class ButtonSceneNavigatorListener implements ActionListener {
	private final AtomicBoolean disableNavigation;

	public ButtonSceneNavigatorListener(AtomicBoolean disableNavigation) {
		this.disableNavigation = disableNavigation;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (disableNavigation.get()) {
			return;
		}

		SceneNavigator.navigateTo(e.getActionCommand());
	}

}
