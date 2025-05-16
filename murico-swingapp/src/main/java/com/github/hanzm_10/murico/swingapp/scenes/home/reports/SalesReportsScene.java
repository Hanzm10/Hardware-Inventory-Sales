package com.github.hanzm_10.murico.swingapp.scenes.home.reports;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.SalesReportGraph;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.SalesReportSummary;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.SalesReportTable;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;

import net.miginfocom.swing.MigLayout;

public final class SalesReportsScene implements Scene {
	private JPanel view;

	private SalesReportSummary salesReportSummary;
	private SalesReportTable salesReportTable;
	private SalesReportGraph salesReportGraph;

	private Thread salesReportSummaryThread;
	private Thread salesReportTableThread;
	private Thread salesReportGraphThread;

	private void attachComponents() {
		view.add(salesReportSummary.getView(), "cell 0 0, grow");
		view.add(salesReportGraph.getView(), "cell 1 0, grow");
		view.add(salesReportTable.getView(), "cell 0 1 2, grow");
	}

	private void createComponents() {
		salesReportSummary = new SalesReportSummary();
		salesReportTable = new SalesReportTable();
		salesReportGraph = new SalesReportGraph();
	}

	@Override
	public String getSceneName() {
		return "sales reports";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onBeforeShow() {
		terminateThreads();

		salesReportSummaryThread = new Thread(salesReportSummary::performBackgroundTask);
		salesReportTableThread = new Thread(salesReportTable::performBackgroundTask);
		salesReportGraphThread = new Thread(salesReportGraph::performBackgroundTask);

		salesReportSummaryThread.start();
		salesReportTableThread.start();
		salesReportGraphThread.start();
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 0", "[grow, left, shrink 10]16[grow]", "[grow][shrink 10,grow]"));

		createComponents();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		if (salesReportSummary != null) {
			salesReportSummary.destroy();
		}

		if (salesReportTable != null) {
			salesReportTable.destroy();
		}

		if (salesReportGraph != null) {
			salesReportGraph.destroy();
		}

		return true;
	}

	@Override
	public void onHide() {
		terminateThreads();
	}

	private void terminateThreads() {
		if (salesReportSummaryThread != null && salesReportSummaryThread.isAlive()) {
			salesReportSummaryThread.interrupt();
			ConnectionManager.cancel(salesReportSummaryThread);
		}

		if (salesReportTableThread != null && salesReportTableThread.isAlive()) {
			salesReportTableThread.interrupt();
			ConnectionManager.cancel(salesReportTableThread);
		}

		if (salesReportGraphThread != null && salesReportGraphThread.isAlive()) {
			salesReportGraphThread.interrupt();
			ConnectionManager.cancel(salesReportGraphThread);
		}
	}
}
