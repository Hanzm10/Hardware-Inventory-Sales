package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.data.category.DefaultCategoryDataset;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalItemCategorySoldInYear;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;

public class SalesReportGraph implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(SalesReportGraph.class);

	private ChartPanel chartPanel;
	private DefaultCategoryDataset graphDataset;

	private AtomicBoolean initialized = new AtomicBoolean(false);
	private AtomicReference<TotalItemCategorySoldInYear[]> totalItemCategorySoldInYear = new AtomicReference<>(
			new TotalItemCategorySoldInYear[0]);

	@Override
	public void destroy() {
		if (chartPanel != null) {
			chartPanel.removeAll();
			chartPanel = null;
		}

		initialized.set(false);
	}

	@Override
	public JPanel getView() {
		return chartPanel == null ? (chartPanel = new ChartPanel(null)) : chartPanel;
	}

	@Override
	public void initializeComponents() {
		if (initialized.get()) {
			return;
		}

		graphDataset = new DefaultCategoryDataset();

		JFreeChart chart = ChartFactory.createStackedBarChart("Best Selling Products of All Time", "Year", "Quantity",
				graphDataset, PlotOrientation.VERTICAL, true, // legend
				true, // tooltips
				false // URLs
		);

		chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
		chart.setAntiAlias(true);
		chart.setBackgroundPaint(new Color(0x00, true));
		chart.getTitle().setPaint(Styles.SECONDARY_COLOR);

		CategoryPlot plot = chart.getCategoryPlot();
		StackedBarRenderer renderer = new StackedBarRenderer();

		// Show value labels
		renderer.setDefaultItemLabelsVisible(true);
		renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());

		plot.setRenderer(renderer);

		chartPanel.setChart(chart);

		initialized.set(true);
	}

	@Override
	public boolean isInitialized() {
		return false;
	}

	@Override
	public void performBackgroundTask() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
		try {
			var totalItemCategorySoldInYearOpened = factory.getSalesDao().getTotalItemCategorySoldInYear();
			totalItemCategorySoldInYear.set(totalItemCategorySoldInYearOpened);

			SwingUtilities.invokeLater(this::updateGraph);
		} catch (IOException | SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to get graph data", e);
		}
	}

	private void updateGraph() {
		if (!initialized.get()) {
			initializeComponents();
		}

		var totalItemCategorySoldInYearOpened = totalItemCategorySoldInYear.get();

		for (var itemCategorySoldInYear : totalItemCategorySoldInYearOpened) {
			graphDataset.addValue(itemCategorySoldInYear.totalQuanitySold(), itemCategorySoldInYear.itemCategory(),
					itemCategorySoldInYear.year());
		}
	}

}
