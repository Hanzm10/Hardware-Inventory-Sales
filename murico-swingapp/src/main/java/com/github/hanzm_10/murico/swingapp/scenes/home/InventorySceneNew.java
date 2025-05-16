package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryHeader;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryTable;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;

import net.miginfocom.swing.MigLayout;

public class InventorySceneNew implements Scene {

	private JPanel view;

	private InventoryTable inventoryTable;
	private InventoryHeader inventoryHeader;

	private Thread inventoryTableThread;

	private void attachComponents() {
		view.setLayout(new MigLayout("insets 0, flowy", "[grow]", "[]16[grow]"));

		view.add(inventoryHeader.getView(), "growx");
		view.add(inventoryTable.getView(), "grow");

		inventoryHeader.initializeComponents();
	}

	private void createComponents() {
		inventoryHeader = new InventoryHeader(this::listenToHeaderButtonEvents);
		inventoryTable = new InventoryTable();
	}

	@Override
	public String getSceneName() {
		return "inventory";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void handleAddCommand() {
	}

	private void handleFilterCommand() {
	}

	private void listenToHeaderButtonEvents(ActionEvent ev) {
		var command = ev.getActionCommand();

		switch (command) {
		case InventoryHeader.ADD_COMMAND:
			handleAddCommand();
			break;
		case InventoryHeader.FILTER_COMMAND:
			handleFilterCommand();
			break;
		}
	}

	@Override
	public void onBeforeShow() {
		terminateThreads();

		inventoryTableThread = new Thread(inventoryTable::performBackgroundTask);

		inventoryTableThread.start();
	}

	@Override
	public void onCreate() {
		createComponents();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		terminateThreads();

		if (inventoryTable != null) {
			inventoryTable.destroy();
		}

		return true;
	}

	@Override
	public void onHide() {
		terminateThreads();
	}

	private void terminateThreads() {
		if (inventoryTableThread != null && inventoryTableThread.isAlive()) {
			inventoryTableThread.interrupt();
			ConnectionManager.cancel(inventoryTableThread);
		}

	}

}
