package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components;

import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextFieldFactory;

import net.miginfocom.swing.MigLayout;

public class InventoryHeader implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryHeader.class);

	public static final String ADD_COMMAND = "add";
	public static final String FILTER_COMMAND = "filter";
	public static final String DELETE_COMMAND = "delete";
	public static final String EDIT_COMMAND = "edit";
	public static final String RESTOCK_COMMAND = "restock";

	private RoundedPanel view;

	private JButton addButton;
	private JButton restockButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton filterButton;

	private AtomicBoolean initialized = new AtomicBoolean(false);

	private ActionListener btnListener;
	private DocumentListener searchListener;

	private JTextField searchField;

	private void attachComponents() {
		view.setLayout(new MigLayout("insets 12", "[]", "[grow]"));
		view.setBackground(Styles.SECONDARY_COLOR);
		view.setForeground(Styles.SECONDARY_FOREGROUND_COLOR);

		view.add(addButton, "width 32!, height 32!");
		view.add(restockButton, "width 32!, height 32!");
		view.add(editButton, "width 32!,height 32!");
		view.add(deleteButton, "width 32!,height 32!");

		view.add(Box.createHorizontalGlue(), "growx, pushx");

		view.add(filterButton, "width 32!, height 32!, alignx right");
		view.add(searchField, "width 250!, height 32!, alignx right");
	}

	private void createComponents() {
		addButton = new JButton("");
		restockButton = new JButton("");
		editButton = new JButton("");
		deleteButton = new JButton("");
		filterButton = new JButton("");

		deleteButton.setBackground(Styles.DANGER_COLOR);

		filterButton.setBackground(Styles.TERTIARY_COLOR);
		searchField = TextFieldFactory.createTextField("Search", 20);

		try {
			addButton.setIcon(AssetManager.getOrLoadIcon("icons/plus.svg"));
			filterButton.setIcon(AssetManager.getOrLoadIcon("icons/funnel.svg"));
			deleteButton.setIcon(AssetManager.getOrLoadIcon("icons/trash.svg"));
			editButton.setIcon(AssetManager.getOrLoadIcon("icons/pencil.svg"));
			restockButton.setIcon(AssetManager.getOrLoadIcon("icons/repeat-2.svg"));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to load icon", e);
		}

		addButton.setToolTipText("Add");
		filterButton.setToolTipText("Filter");
		deleteButton.setToolTipText("Delete");
		editButton.setToolTipText("Edit");
		restockButton.setToolTipText("Restock");

		addButton.setActionCommand(ADD_COMMAND);
		filterButton.setActionCommand(FILTER_COMMAND);
		deleteButton.setActionCommand(DELETE_COMMAND);
		editButton.setActionCommand(EDIT_COMMAND);
		restockButton.setActionCommand(RESTOCK_COMMAND);
	}

	@Override
	public void destroy() {
		if (view != null) {
			view.removeAll();
			view = null;
		}

		if (btnListener != null) {
			addButton.removeActionListener(btnListener);
			filterButton.removeActionListener(btnListener);
			editButton.removeActionListener(btnListener);
			deleteButton.removeActionListener(btnListener);
			restockButton.removeActionListener(btnListener);
		}

		addButton = null;
		filterButton = null;
		deleteButton = null;
		editButton = null;
		restockButton = null;

		if (searchListener != null) {
			searchField.getDocument().removeDocumentListener(searchListener);
		}

		searchField = null;

		initialized.set(false);
		LOGGER.info("InventoryHeader destroyed");
	}

	public JTextField getSearchField() {
		return searchField;
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

	public void setBtnListener(ActionListener btnListener) {
		if (this.btnListener != null) {
			addButton.removeActionListener(this.btnListener);
			filterButton.removeActionListener(this.btnListener);
			editButton.removeActionListener(this.btnListener);
			deleteButton.removeActionListener(this.btnListener);
			restockButton.removeActionListener(this.btnListener);
		}

		this.btnListener = btnListener;

		addButton.addActionListener(btnListener);
		filterButton.addActionListener(btnListener);
		editButton.addActionListener(btnListener);
		deleteButton.addActionListener(btnListener);
		restockButton.addActionListener(btnListener);
	}

	public void setSearchListener(DocumentListener searchListener) {
		if (this.searchListener != null) {
			searchField.getDocument().removeDocumentListener(this.searchListener);
		}

		this.searchListener = searchListener;

		searchField.getDocument().addDocumentListener(searchListener);
	}

}
