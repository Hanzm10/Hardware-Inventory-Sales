
package com.github.hanzm_10.murico.swingapp.scenes.home.reports;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.CustomerPayment;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalItemCategorySoldInYear;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsSalesTable;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsSummarySales;

import net.miginfocom.swing.MigLayout;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.CustomerPayment;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalItemCategorySoldInYear;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsSalesTable;
import com.github.hanzm_10.murico.swingapp.ui.components.reports.ReportsSummarySales;

public final class InventoryReportsScene implements Scene {
	private static final Logger LOGGER = MuricoLogger.getLogger(SalesReportsScene.class);
	private JPanel view;
	private ReportsSummarySales summarySales;
	private ReportsSalesTable salesTable;

	private Thread getSummaryDataThread;
	private Thread getGraphDataThread;
	private Thread getTableDataThread;
	private AtomicReference<TotalOfSales> totalOfSales;
	private AtomicReference<TotalItemCategorySoldInYear[]> totalItemCategorySoldInYear;
	private AtomicReference<CustomerPayment[]> customerPayments;

	private void attachComponents() {
		view.add(summarySales.getContainer(), "cell 0 0, grow");
		view.add(salesTable.getContainer(), "cell 0 1, grow");
	}

	private void createComponents() {
		summarySales = new ReportsSummarySales();
		salesTable = new ReportsSalesTable();
	}

	private void getGraphData() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
		try {
			var totalItemCategorySoldInYearOpened = factory.getSalesDao().getTotalItemCategorySoldInYear();
			totalItemCategorySoldInYear.set(totalItemCategorySoldInYearOpened);

			SwingUtilities.invokeLater(this::updateGraphView);
		} catch (IOException | SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to get graph data", e);
		}
	}


	@Override
	public String getSceneName() {
		return "inventory reports";
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
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
		try {
			var customerPaymentsOpened = factory.getSalesDao().getCustomerPayments();
			customerPayments.set(customerPaymentsOpened);

			SwingUtilities.invokeLater(this::updateTableView);
		} catch (IOException | SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to get table data", e);
		}
	}
	
	
	public void onBeforeShow() {
		attachComponents();

		terminateThreads();

		getSummaryDataThread = new Thread(this::getSummaryData);
		getGraphDataThread = new Thread(this::getGraphData);
		getTableDataThread = new Thread(this::getTableData);

		getSummaryDataThread.start();
		getGraphDataThread.start();
		getTableDataThread.start();
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

		view.removeAll();
	}

	private void terminateThreads() {
		if (getSummaryDataThread != null) {
			ConnectionManager.cancel(getSummaryDataThread);
			getSummaryDataThread.interrupt();
		}

		if (getGraphDataThread != null) {
			ConnectionManager.cancel(getGraphDataThread);
			getGraphDataThread.interrupt();
		}

		if (getTableDataThread != null) {
			ConnectionManager.cancel(getTableDataThread);
			getTableDataThread.interrupt();
		}
	}

	private void updateGraphView() {
		var totalItemCategorySoldInYearOpened = totalItemCategorySoldInYear.get();

		if (totalItemCategorySoldInYearOpened == null) {
			LOGGER.warning("Total item category sold in year is null");
			return;
		}

		summarySales.updateGraph(totalItemCategorySoldInYearOpened);
	}

	private void updateTableView() {
		var customerPaymentsOpened = customerPayments.get();

		if (customerPaymentsOpened == null) {
			LOGGER.warning("Customer payments is null");
			return;
		}

		salesTable.setTableData(customerPaymentsOpened);
	}

	private void updateTotalOfSalesView() {
		var totalOfSalesOpened = totalOfSales.getAcquire();

		summarySales.setTotalRevenueInfo(totalOfSalesOpened.totalRevenue());
		summarySales.setTotalGrossInfo(totalOfSalesOpened.totalGross());
		summarySales.setTotalNetSalesInfo(totalOfSalesOpened.totalNetSales());
		summarySales.setTotalItemsSoldInfo(totalOfSalesOpened.totalItemsSold());
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}
}
