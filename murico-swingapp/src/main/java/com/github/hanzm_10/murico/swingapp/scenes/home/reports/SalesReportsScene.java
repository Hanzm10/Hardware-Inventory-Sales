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

	@Override
	public String getSceneName() {
		return "sales reports";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
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
}
