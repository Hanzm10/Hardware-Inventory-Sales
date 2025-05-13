package com.github.hanzm_10.murico.swingapp.scenes.home.reports;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsSalesTable;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsSummarySales;

import net.miginfocom.swing.MigLayout;

public final class SalesReportsScene implements Scene {
	private static final Logger LOGGER = MuricoLogger.getLogger(SalesReportsScene.class);
	private JPanel view;
	private ReportsSummarySales summarySales;
	private ReportsSalesTable salesTable;

	private Thread getSummaryDataThread;
	private Thread getTableDataThread;
	private AtomicReference<TotalOfSales> totalOfSales;

	private void attachComponents() {
		view.add(summarySales.getContainer(), "cell 0 0, grow");
		view.add(salesTable.getContainer(), "cell 0 1, grow");
	}

	private void createComponents() {
		summarySales = new ReportsSummarySales();
		salesTable = new ReportsSalesTable();
	}

	@Override
	public String getSceneName() {
		return "sales reports";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void getSummaryData() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
		try {
			var tOfSales = factory.getSalesDao().getTotalOfSales();
			totalOfSales.set(tOfSales);

			SwingUtilities.invokeLater(this::updateTotalOfSalesView);
		} catch (IOException | SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to get summary of sales data", e);
		}
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
		view.setLayout(new MigLayout("insets 0", "[grow]", "[::375px, grow][grow]"));

		totalOfSales = new AtomicReference<TotalOfSales>();

		createComponents();
		attachComponents();
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
			ConnectionManager.cancel(getSummaryDataThread);
			getSummaryDataThread.interrupt();
		}

		if (getTableDataThread != null) {
			ConnectionManager.cancel(getTableDataThread);
			getTableDataThread.interrupt();
		}
	}

	private void updateTotalOfSalesView() {
		var totalOfSalesOpened = totalOfSales.getAcquire();

		summarySales.setTotalRevenueInfo(totalOfSalesOpened.totalRevenue());
		summarySales.setTotalGrossInfo(totalOfSalesOpened.totalGross());
		summarySales.setTotalNetSalesInfo(totalOfSalesOpened.totalNetSales());
		summarySales.setTotalItemsSoldInfo(totalOfSalesOpened.totalItemsSold());
	}
}
