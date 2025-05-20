
package com.github.hanzm_10.murico.swingapp.scenes.home.reports;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.InventoryReportGraph;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.InventoryReportSummary;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.InventoryReportTable;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;

import net.miginfocom.swing.MigLayout;

public final class InventoryReportsScene implements Scene {

	private JPanel view;

	private InventoryReportSummary inventoryReportSummary;
	private InventoryReportGraph inventoryReportGraph;
	private InventoryReportTable inventoryReportTable;

	private Thread inventoryReportSummaryThread;
	private Thread inventoryReportTableThread;
	private Thread inventoryReportGraphThread;

	private void attachComponents() {
		view.add(inventoryReportSummary.getView(), "cell 0 0, grow");
		view.add(inventoryReportGraph.getView(), "cell 1 0, grow");
		view.add(inventoryReportTable.getView(), "cell 0 1 2, grow");
	}

	private void createComponents() {
		inventoryReportSummary = new InventoryReportSummary();
		inventoryReportTable = new InventoryReportTable();
		inventoryReportGraph = new InventoryReportGraph();
	}

	@Override
	public String getSceneName() {
		return "inventory reports";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onBeforeShow() {
		terminateThreads();

		inventoryReportSummaryThread = new Thread(inventoryReportSummary::performBackgroundTask);
		inventoryReportTableThread = new Thread(inventoryReportTable::performBackgroundTask);
		inventoryReportGraphThread = new Thread(inventoryReportGraph::performBackgroundTask);

		inventoryReportSummaryThread.start();
		inventoryReportTableThread.start();
		inventoryReportGraphThread.start();
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 0", "[grow, left, shrink 10]16[grow]", "[grow][shrink 10,grow]"));

		createComponents();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		terminateThreads();

		inventoryReportSummary.destroy();
		inventoryReportGraph.destroy();
		inventoryReportTable.destroy();

		return true;
	}

	@Override
	public void onHide() {
		terminateThreads();
	}

	private void terminateThreads() {
		if (inventoryReportSummaryThread != null && inventoryReportSummaryThread.isAlive()) {
			inventoryReportSummaryThread.interrupt();
			ConnectionManager.cancel(inventoryReportSummaryThread);
		}

		if (inventoryReportTableThread != null && inventoryReportTableThread.isAlive()) {
			inventoryReportTableThread.interrupt();
			ConnectionManager.cancel(inventoryReportTableThread);
		}

		if (inventoryReportGraphThread != null && inventoryReportGraphThread.isAlive()) {
			inventoryReportGraphThread.interrupt();
			ConnectionManager.cancel(inventoryReportGraphThread);
		}
	}

}
