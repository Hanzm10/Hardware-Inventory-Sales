package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.data.category.DefaultCategoryDataset;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.ItemQuantityPerPackaging;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;

import net.miginfocom.swing.MigLayout;

public class InventoryReportGraph implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryReportGraph.class);

	private JPanel view;
	private JScrollPane scrollPane;
	private ChartPanel chartPanel;
	private DefaultCategoryDataset graphDataset;

	private AtomicBoolean initialized = new AtomicBoolean(false);
	private AtomicReference<ItemQuantityPerPackaging[]> itemQuantityPerPackaging = new AtomicReference<>(
			new ItemQuantityPerPackaging[0]);

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
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void initializeComponents() {
		if (initialized.get()) {
			return;
		}

		chartPanel = new ChartPanel(null);
		graphDataset = new DefaultCategoryDataset();

		var chart = ChartFactory.createBarChart("Inventory Stock Level", "Item", "Quantity", graphDataset,
				PlotOrientation.HORIZONTAL, true, true, false);

		chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
		chart.setAntiAlias(true);
		chart.setBackgroundPaint(new Color(0x00, true));
		chart.getTitle().setPaint(Styles.SECONDARY_COLOR);

		var plot = chart.getCategoryPlot();

		var renderer = new BarRenderer();

		renderer.setDefaultItemLabelsVisible(true);
		renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());

		plot.setRenderer(renderer);

		chartPanel.setChart(chart);
		chartPanel.setFont(new Font("Montserrat", Font.PLAIN, 12));

		scrollPane = new JScrollPane(chartPanel);
		scrollPane.setBorder(new LineBorder(Styles.SECONDARY_COLOR, 1));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

		view.setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));

		view.add(scrollPane, "cell 0 0, grow");

		initialized.set(true);
	}

	@Override
	public boolean isInitialized() {
		return initialized.get();
	}

	@Override
	public void performBackgroundTask() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			itemQuantityPerPackaging.set(factory.getItemDao().getItemsQuantityPerPackaging());

			SwingUtilities.invokeLater(this::updateGraph);
		} catch (Exception e) {
			LOGGER.warning("Failed to get item quantity per packaging: " + e.getMessage());
		}
	}

	private void updateGraph() {
		if (!isInitialized()) {
			initializeComponents();
		}

		var dataset = itemQuantityPerPackaging.get();

		for (var item : dataset) {
			var name = item.itemName();

			for (var packaging : item.packagingQuantities()) {
				var quantity = packaging.quantity();
				var packagingName = packaging.packagingName();

				graphDataset.addValue(quantity, packagingName, name);
			}
		}
	}

}
