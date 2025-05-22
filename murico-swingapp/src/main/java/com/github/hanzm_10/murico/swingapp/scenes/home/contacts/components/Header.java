package com.github.hanzm_10.murico.swingapp.scenes.home.contacts.components;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;

public class Header implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(Header.class);

	public static final String EDIT_COMMAND = "edit";

	private JPanel view;

	private JButton edit;

	private AtomicBoolean initialized = new AtomicBoolean(false);

	private ActionListener btnListener;

	@Override
	public void destroy() {
		if (view != null) {
			view.removeAll();
			view = null;
		}
		if (edit != null) {
			edit.removeActionListener(btnListener);
			edit = null;
		}
	}

	@Override
	public JPanel getView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void initializeComponents() {
		if (initialized.get()) {
			return;
		}

		view.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 0));

		edit = new JButton("Edit");

		try {
			edit.setIcon(AssetManager.getOrLoadIcon("icons/edit2.svg"));
		} catch (Exception e) {
			LOGGER.severe("Error initializing header components: " + e.getMessage());
		}

		edit.setToolTipText("Edit selected contact");

		edit.setActionCommand(EDIT_COMMAND);

		view.add(edit);

		if (btnListener != null) {
			edit.addActionListener(btnListener);
		}

		initialized.set(true);
	}

	@Override
	public boolean isInitialized() {
		return initialized.get();
	}

	public void setBtnListener(ActionListener btnListener) {
		if (this.btnListener != null) {
			edit.removeActionListener(this.btnListener);
		}

		this.btnListener = btnListener;

		edit.addActionListener(btnListener);
	}

}
