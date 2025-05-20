package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.ItemQuantityPerPackaging;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;

public class InventoryReportGraph implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryReportGraph.class);

	private ChartPanel chartPanel;

	private AtomicReference<ItemQuantityPerPackaging[]> itemQuantityPerPackaging = new AtomicReference<>(
			new ItemQuantityPerPackaging[0]);

	@Override
	public void destroy() {
	}

	@Override
	public JPanel getView() {
		return chartPanel == null ? (chartPanel = new ChartPanel(null)) : chartPanel;
	}

	@Override
	public void initializeComponents() {
	}

	@Override
	public boolean isInitialized() {
		return false;
	}

	@Override
	public void performBackgroundTask() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			itemQuantityPerPackaging.set(factory.getItemDao().getItemsQuantityPerPackaging());

			for (var item : itemQuantityPerPackaging.get()) {
				System.out.println("Item: " + item.itemName());

				for (var packaging : item.packagingQuantities()) {
					System.out
							.println("Packaging: " + packaging.packagingName() + ", Quantity: " + packaging.quantity());
				}

				System.out.println();

			}
		} catch (Exception e) {
			LOGGER.warning("Failed to get item quantity per packaging: " + e.getMessage());
		}
	}

}
