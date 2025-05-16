package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components;

import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;

import net.miginfocom.swing.MigLayout;

public class InventoryHeader implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryHeader.class);

	public static final String ADD_COMMAND = "add";
	public static final String FILTER_COMMAND = "filter";

	private RoundedPanel view;

	private JButton addButton;
	private JButton filterButton;

	private AtomicBoolean initialized = new AtomicBoolean(false);

	private ActionListener btnListener;

	public InventoryHeader(ActionListener btnListener) {
		this.btnListener = btnListener;
	}

	private void attachComponents() {
		view.setLayout(new MigLayout("insets 12", "[grow]", "[grow]"));
		view.setBackground(Styles.SECONDARY_COLOR);
		view.setForeground(Styles.SECONDARY_FOREGROUND_COLOR);

		view.add(addButton, "width 32!, height 32!");
		view.add(Box.createHorizontalGlue(), "growx, pushx, span");
		view.add(filterButton, "width 32!, height 32!");
	}

	private void createComponents() {
		addButton = new JButton("");
		filterButton = new JButton("");

		filterButton.setBackground(Styles.TERTIARY_COLOR);

		try {
			addButton.setIcon(AssetManager.getOrLoadIcon("icons/plus.svg"));
			filterButton.setIcon(AssetManager.getOrLoadIcon("icons/funnel.svg"));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to load icon", e);
		}

		addButton.setToolTipText("Add");
		filterButton.setToolTipText("Filter");

		addButton.setActionCommand(ADD_COMMAND);
		filterButton.setActionCommand(FILTER_COMMAND);

		addButton.addActionListener(btnListener);
		filterButton.addActionListener(btnListener);
	}

	@Override
	public void destroy() {
		if (view != null) {
			view.removeAll();
			view = null;
		}

		addButton.removeActionListener(btnListener);
		filterButton.removeActionListener(btnListener);

		addButton = null;
		filterButton = null;

		initialized.set(false);
		LOGGER.info("InventoryHeader destroyed");
	}

	@Override
	public JPanel getView() {
		return view == null ? (view = new RoundedPanel(24)) : view;
	}

	@Override
	public void initializeComponents() {
		if (initialized.get()) {
			return;
		}

		createComponents();
		attachComponents();

		initialized.set(true);
	}

	@Override
	public boolean isInitialized() {
		return initialized.get();
	}

}
