package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.MonthlyGross;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.WeeklyGross;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.YearlyGross;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

import net.miginfocom.swing.MigLayout;

public class DashboardScene implements Scene {

	private static final Logger LOGGER = MuricoLogger.getLogger(DashboardScene.class);

	private JPanel view;

	private JTabbedPane tabbedPane;

	private ChartPanel weeklyChartPanel;
	private DefaultCategoryDataset weeklyDataset;
	private AtomicReference<WeeklyGross[]> weeklyGross = new AtomicReference<>(new WeeklyGross[0]);
	private ChartPanel monthlyChartPanel;
	private DefaultCategoryDataset monthlyDataset;
	private AtomicReference<MonthlyGross[]> monthlyGross = new AtomicReference<>(new MonthlyGross[0]);

	private ChartPanel yearlyChartPanel;
	private DefaultCategoryDataset yearlyDataset;
	private AtomicReference<YearlyGross[]> yearlyGross = new AtomicReference<>(new YearlyGross[0]);

	private ExecutorService executor;

	private void createMonthlyChart() {
		monthlyDataset = new DefaultCategoryDataset();

		var chart = ChartFactory.createLineChart("Sales per Month", "Month", "Amount", monthlyDataset,
				PlotOrientation.VERTICAL, true, true, false);

		chart.setAntiAlias(true);
		chart.setBackgroundPaint(new Color(0x00, true));
		chart.getTitle().setPaint(Styles.SECONDARY_COLOR);

		monthlyChartPanel = new ChartPanel(chart);
		monthlyChartPanel.setFont(new Font("Montserrat", Font.PLAIN, 12));
	}

	private void createWeeklyChart() {

		weeklyDataset = new DefaultCategoryDataset();

		var chart = ChartFactory.createLineChart("Sales per Month", "Month", "Amount", weeklyDataset,
				PlotOrientation.VERTICAL, true, true, false);

		chart.setAntiAlias(true);
		chart.setBackgroundPaint(new Color(0x00, true));
		chart.getTitle().setPaint(Styles.SECONDARY_COLOR);

		weeklyChartPanel = new ChartPanel(chart);
		weeklyChartPanel.setFont(new Font("Montserrat", Font.PLAIN, 12));
	}

	private void createYearlyChart() {
		yearlyDataset = new DefaultCategoryDataset();

		var chart = ChartFactory.createLineChart("Sales per Month", "Month", "Amount", yearlyDataset,
				PlotOrientation.VERTICAL, true, true, false);

		chart.setAntiAlias(true);
		chart.setBackgroundPaint(new Color(0x00, true));
		chart.getTitle().setPaint(Styles.SECONDARY_COLOR);

		yearlyChartPanel = new ChartPanel(chart);
		yearlyChartPanel.setFont(new Font("Montserrat", Font.PLAIN, 12));
	}

	@Override
	public String getSceneName() {
		return "dashboard";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void handleMonthlyGross() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			monthlyGross.set(factory.getSalesDao().getMonthlyGross());
			SwingUtilities.invokeLater(this::updateMonthlyGross);
		} catch (SQLException | IOException e) {
			LOGGER.log(Level.SEVERE, "Error fetching monthly gross", e);
		}
	}

	private void handleWeeklyGross() {

		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			weeklyGross.set(factory.getSalesDao().getWeeklyGross());
			SwingUtilities.invokeLater(this::updateWeeklyGross);
		} catch (SQLException | IOException e) {
			LOGGER.log(Level.SEVERE, "Error fetching weekly gross", e);
		}
	}

	private void handleYearlyGross() {

		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			yearlyGross.set(factory.getSalesDao().getYearlyGross());
			SwingUtilities.invokeLater(this::updateYearlyGross);
		} catch (SQLException | IOException e) {
			LOGGER.log(Level.SEVERE, "Error fetching yearly gross", e);
		}
	}

	@Override
	public void onBeforeShow() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}

		executor = Executors.newFixedThreadPool(3);

		executor.submit(this::handleWeeklyGross);
		executor.submit(this::handleMonthlyGross);
		executor.submit(this::handleYearlyGross);
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 0", "[grow, center]", "[grow]"));

		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		createWeeklyChart();
		createMonthlyChart();
		createYearlyChart();

		tabbedPane.addTab("Weekly Sales", weeklyChartPanel);
		tabbedPane.addTab("Monthly Sales", monthlyChartPanel);
		tabbedPane.addTab("Yearly Sales", yearlyChartPanel);

		tabbedPane.setSelectedIndex(0);

		view.add(tabbedPane, "grow");
	}

	@Override
	public boolean onDestroy() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}

		if (weeklyChartPanel != null) {
			weeklyChartPanel.removeAll();
			weeklyChartPanel = null;
		}

		if (monthlyChartPanel != null) {
			monthlyChartPanel.removeAll();
			monthlyChartPanel = null;
		}

		if (yearlyChartPanel != null) {
			yearlyChartPanel.removeAll();
			yearlyChartPanel = null;
		}

		return true;
	}

	@Override
	public void onHide() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}
	}

	private void updateMonthlyGross() {
		var data = monthlyGross.get();

		monthlyDataset.clear();

		for (var gross : data) {
			monthlyDataset.addValue(gross.gross(), "Gross", gross.month());
		}
	}

	private void updateWeeklyGross() {

		var data = weeklyGross.get();

		weeklyDataset.clear();

		for (var gross : data) {
			weeklyDataset.addValue(gross.gross(), "Gross", gross.weekDay());
		}
	}

	private void updateYearlyGross() {

		var data = yearlyGross.get();

		yearlyDataset.clear();

		for (var gross : data) {
			yearlyDataset.addValue(gross.gross(), "Gross", gross.year());
		}
	}

}
