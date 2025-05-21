package com.github.hanzm_10.murico.swingapp.scenes.home.reports;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.SalesReportGraph;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.SalesReportSummary;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.SalesReportTable;

import net.miginfocom.swing.MigLayout;

public final class SalesReportsScene implements Scene {
	private JPanel view;

	private SalesReportSummary salesReportSummary;
	private SalesReportTable salesReportTable;
	private SalesReportGraph salesReportGraph;

	private ExecutorService executor;

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

		executor = Executors.newCachedThreadPool();

		executor.submit(salesReportSummary::performBackgroundTask);
		executor.submit(salesReportTable::performBackgroundTask);
		executor.submit(salesReportGraph::performBackgroundTask);
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 0", "[grow, left, shrink 20]16[grow]", "[grow][shrink 20,grow]"));

		createComponents();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		terminateThreads();

		salesReportSummary.destroy();
		salesReportTable.destroy();
		salesReportGraph.destroy();

		return true;
	}

	@Override
	public void onHide() {
		terminateThreads();
	}

	private void terminateThreads() {
		if (executor != null) {
			executor.shutdownNow();
			executor = null;
		}
	}
}
