package com.github.hanzm_10.murico.swingapp.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;

public class ButtonSceneNavigatorListener implements ActionListener {
	private final Supplier<AtomicBoolean> disableNavigation;

	public ButtonSceneNavigatorListener(Supplier<AtomicBoolean> disableNavigation) {
		this.disableNavigation = disableNavigation;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (disableNavigation.get().get()) {
			return;
		}

		SceneNavigator.navigateTo(e.getActionCommand());
	}

}
