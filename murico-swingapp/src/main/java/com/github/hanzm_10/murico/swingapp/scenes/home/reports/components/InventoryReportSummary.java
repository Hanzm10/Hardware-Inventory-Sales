package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.InventorySummary;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.lib.utils.NumberUtils;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class InventoryReportSummary implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryReportSummary.class);

	private JPanel view;

	private RoundedPanel totalInventoryValueContainer;
	private JLabel totalInventoryValueTitle;
	private JLabel totalInventoryValueInfo;

	private RoundedPanel totalItemsInStockContainer;
	private JLabel totalItemsInStockTitle;
	private JLabel totalItemsInStockInfo;

	private RoundedPanel totalItemsBelowCriticalLevelContainer;
	private JLabel totalItemsBelowCriticalLevelTitle;
	private JLabel totalItemsBelowCriticalLevelInfo;

	private RoundedPanel avgQuantityPerItemContainer;
	private JLabel avgQuantityPerItemTitle;
	private JLabel avgQuantityPerItemInfo;

	private AtomicReference<InventorySummary> inventorySummary = new AtomicReference<>(
			new InventorySummary(0, 0, 0, new BigDecimal(0)));
	private AtomicBoolean initialized = new AtomicBoolean(false);

	private void attachComponents() {
		view.add(totalInventoryValueContainer);
		totalInventoryValueContainer.add(totalInventoryValueTitle);
		totalInventoryValueContainer.add(totalInventoryValueInfo);

		view.add(totalItemsInStockContainer);
		totalItemsInStockContainer.add(totalItemsInStockTitle);
		totalItemsInStockContainer.add(totalItemsInStockInfo);

		view.add(totalItemsBelowCriticalLevelContainer);
		totalItemsBelowCriticalLevelContainer.add(totalItemsBelowCriticalLevelTitle);
		totalItemsBelowCriticalLevelContainer.add(totalItemsBelowCriticalLevelInfo);

		view.add(avgQuantityPerItemContainer);
		avgQuantityPerItemContainer.add(avgQuantityPerItemTitle);
		avgQuantityPerItemContainer.add(avgQuantityPerItemInfo);
	}

	private void createComponents() {
		view.setLayout(new MigLayout("insets 0, wrap 2", "[::200px]", "[top]"));

		totalInventoryValueContainer = new RoundedPanel(16);
		totalInventoryValueTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Inventory Value:"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalInventoryValueInfo = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 20,
				Styles.SECONDARY_FOREGROUND_COLOR);

		totalItemsInStockContainer = new RoundedPanel(16);
		totalItemsInStockTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Items in Stock:"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalItemsInStockInfo = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 20,
				Styles.SECONDARY_FOREGROUND_COLOR);

		totalItemsBelowCriticalLevelContainer = new RoundedPanel(16);
		totalItemsBelowCriticalLevelTitle = LabelFactory.createBoldLabel(
				HtmlUtils.wrapInHtml("Items Below Critical Level:"), 16, Styles.SECONDARY_FOREGROUND_COLOR);
		totalItemsBelowCriticalLevelInfo = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 20,
				Styles.WARNING_COLOR);

		avgQuantityPerItemContainer = new RoundedPanel(16);
		avgQuantityPerItemTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Average Quantity per Item:"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		avgQuantityPerItemInfo = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 20,
				Styles.SECONDARY_FOREGROUND_COLOR);

		totalInventoryValueContainer.setBackground(Styles.SECONDARY_COLOR);
		totalItemsInStockContainer.setBackground(Styles.SECONDARY_COLOR);
		totalItemsBelowCriticalLevelContainer.setBackground(Styles.SECONDARY_COLOR);
		avgQuantityPerItemContainer.setBackground(Styles.SECONDARY_COLOR);

		totalInventoryValueContainer.setLayout(new MigLayout("insets 16, flowy", "[grow, center]", "[grow]"));
		totalItemsInStockContainer.setLayout(new MigLayout("insets 16, flowy", "[grow, center]", "[grow]"));
		totalItemsBelowCriticalLevelContainer.setLayout(new MigLayout("insets 16, flowy", "[grow, center]", "[grow]"));
		avgQuantityPerItemContainer.setLayout(new MigLayout("insets 16, flowy", "[grow, center]", "[grow]"));

		var border = new FlatRoundBorder();

		border.applyStyleProperty("borderColor", Styles.PRIMARY_COLOR);
		border.applyStyleProperty("borderWidth", 2);
		border.applyStyleProperty("arc", 18);

		totalInventoryValueContainer.setBorder(border);
		totalItemsInStockContainer.setBorder(border);
		totalItemsBelowCriticalLevelContainer.setBorder(border);
		avgQuantityPerItemContainer.setBorder(border);
	}

	@Override
	public void destroy() {
		view.removeAll();

		inventorySummary.set(new InventorySummary(0, 0, 0, new BigDecimal(0)));

		totalInventoryValueContainer = null;
		totalInventoryValueTitle = null;
		totalInventoryValueInfo = null;
		totalItemsInStockContainer = null;
		totalItemsInStockTitle = null;
		totalItemsInStockInfo = null;
		totalItemsBelowCriticalLevelContainer = null;
		totalItemsBelowCriticalLevelTitle = null;
		totalItemsBelowCriticalLevelInfo = null;
		avgQuantityPerItemContainer = null;
		avgQuantityPerItemTitle = null;
		avgQuantityPerItemInfo = null;

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

		createComponents();
		attachComponents();

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
			var inventorySummary = factory.getItemDao().getInventorySummary();
			this.inventorySummary.set(inventorySummary);

			updateInventorySummary();
		} catch (IOException | SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to get summary of inventory data", e);
		}
	}

	private void updateInventorySummary() {
		if (!initialized.get()) {
			SwingUtilities.invokeLater(this::initializeComponents);
		}

		SwingUtilities.invokeLater(() -> {
			var inventorySummary = this.inventorySummary.get();

			if (inventorySummary == null) {
				totalInventoryValueInfo.setText(HtmlUtils.wrapInHtml("N/A"));
				totalItemsInStockInfo.setText(HtmlUtils.wrapInHtml("N/A"));
				totalItemsBelowCriticalLevelInfo.setText(HtmlUtils.wrapInHtml("N/A"));
				avgQuantityPerItemInfo.setText(HtmlUtils.wrapInHtml("N/A"));
				return;
			}

			totalInventoryValueInfo.setText(
					HtmlUtils.wrapInHtml("₱" + NumberUtils.formatWithSuffix(inventorySummary.totalInventoryValue())));
			totalItemsInStockInfo
					.setText(HtmlUtils.wrapInHtml(NumberUtils.formatWithSuffix(inventorySummary.totalItemsInStock())));
			totalItemsBelowCriticalLevelInfo.setText(HtmlUtils
					.wrapInHtml(NumberUtils.formatWithSuffix(inventorySummary.totalItemsBelowCriticalLevel())));
			avgQuantityPerItemInfo.setText(HtmlUtils
					.wrapInHtml(NumberUtils.formatWithSuffix(inventorySummary.averageStockPerItem().doubleValue())));
		});
	}

}
