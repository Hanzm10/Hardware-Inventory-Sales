package com.github.hanzm_10.murico.swingapp.scenes.home.reports;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsSalesTable;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsSummarySales;

public final class SalesReportsScene implements Scene {
	private JPanel view;
	private ReportsSummarySales summarySales;
	private ReportsSalesTable salesTable;

	private Thread getSummaryDataThread;
	private Thread getTableDataThread;

	@Override
	public String getSceneName() {
		return "sales reports";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void getSummaryData() {
	}

	private void getTableData() {
	}

	@Override
	public void onBeforeShow() {
		terminateThreads();

		getSummaryDataThread = new Thread(this::getSummaryData);
		getTableDataThread = new Thread(this::getTableData);

		getSummaryDataThread.start();
		getTableDataThread.start();
	}

	@Override
	public void onCreate() {
		view.setLayout(new GridLayout(2, 1, 16, 0));

		summarySales = new ReportsSummarySales();
		salesTable = new ReportsSalesTable();

		view.add(summarySales.getContainer());
		view.add(salesTable.getContainer());
	}

	@Override
	public boolean onDestroy() {
		summarySales.destroy();
		salesTable.destroy();

		return true;
	}

	@Override
	public void onHide() {
		terminateThreads();
	}

	private void terminateThreads() {
		if (getSummaryDataThread != null) {
			getSummaryDataThread.interrupt();
		}

		if (getTableDataThread != null) {
			getTableDataThread.interrupt();
		}
	}
}
