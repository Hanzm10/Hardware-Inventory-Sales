package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import java.io.IOException;
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
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.lib.utils.NumberUtils;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class SalesReportSummary implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(SalesReportSummary.class);

	private JPanel view;

	private RoundedPanel totalRevenueContainer;
	private JLabel totalRevenueTitle;
	private JLabel totalRevenueInfo;

	private RoundedPanel totalGrossContainer;
	private JLabel totalGrossTitle;
	private JLabel totalGrossInfo;

	private RoundedPanel totalNetSalesContainer;
	private JLabel totalNetSalesTitle;
	private JLabel totalNetSalesInfo;

	private RoundedPanel totalItemsSoldContainer;
	private JLabel totalItemsSoldTitle;
	private JLabel totalItemsSoldInfo;

	private AtomicReference<TotalOfSales> totalOfSales = new AtomicReference<>(new TotalOfSales(0, 0, 0, 0));
	private AtomicBoolean initialized = new AtomicBoolean(false);

	private void attachComponents() {
		view.add(totalRevenueContainer, "grow");
		totalRevenueContainer.add(totalRevenueTitle, "growx");
		totalRevenueContainer.add(totalRevenueInfo, "growx");

		view.add(totalGrossContainer, "grow");
		totalGrossContainer.add(totalGrossTitle, "growx");
		totalGrossContainer.add(totalGrossInfo, "growx");

		view.add(totalNetSalesContainer, "grow");
		totalNetSalesContainer.add(totalNetSalesTitle, "growx");
		totalNetSalesContainer.add(totalNetSalesInfo, "growx");

		view.add(totalItemsSoldContainer, "grow");
		totalItemsSoldContainer.add(totalItemsSoldTitle, "growx");
		totalItemsSoldContainer.add(totalItemsSoldInfo, "growx");
	}

	private void createComponents() {
		view.setLayout(new MigLayout("insets 0, wrap 2", "[::200px]", "[top]"));
		totalRevenueContainer = new RoundedPanel(16);
		totalRevenueTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Revenue of All Time"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalRevenueInfo = LabelFactory.createBoldLabel("", 20, Styles.SECONDARY_FOREGROUND_COLOR);

		totalGrossContainer = new RoundedPanel(16);
		totalGrossTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Gross of All Time"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalGrossInfo = LabelFactory.createBoldLabel("", 20, Styles.SECONDARY_FOREGROUND_COLOR);

		totalNetSalesContainer = new RoundedPanel(16);
		totalNetSalesTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Net Sales of All Time"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalNetSalesInfo = LabelFactory.createBoldLabel("", 20, Styles.SECONDARY_FOREGROUND_COLOR);

		totalItemsSoldContainer = new RoundedPanel(16);
		totalItemsSoldTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Items Sold of All Time"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalItemsSoldInfo = LabelFactory.createBoldLabel("", 20, Styles.SECONDARY_FOREGROUND_COLOR);

		totalRevenueContainer.setBackground(Styles.SECONDARY_COLOR);
		totalGrossContainer.setBackground(Styles.SECONDARY_COLOR);
		totalNetSalesContainer.setBackground(Styles.SECONDARY_COLOR);
		totalItemsSoldContainer.setBackground(Styles.SECONDARY_COLOR);

		totalRevenueContainer.setLayout(new MigLayout("insets 16, flowy", "[grow, center]"));
		totalGrossContainer.setLayout(new MigLayout("insets 16, flowy", "[grow, center]"));
		totalNetSalesContainer.setLayout(new MigLayout("insets 16, flowy", "[grow, center]"));
		totalItemsSoldContainer.setLayout(new MigLayout("insets 16, flowy", "[grow, center]"));

		var border = new FlatRoundBorder();

		border.applyStyleProperty("borderColor", Styles.PRIMARY_COLOR);
		border.applyStyleProperty("borderWidth", 2);
		border.applyStyleProperty("arc", 18);

		totalRevenueContainer.setBorder(border);
		totalGrossContainer.setBorder(border);
		totalNetSalesContainer.setBorder(border);
		totalItemsSoldContainer.setBorder(border);
	}

	@Override
	public void destroy() {
		if (view != null) {
			view.removeAll();
			view = null;
		}

		totalRevenueContainer = null;
		totalGrossContainer = null;
		totalNetSalesContainer = null;
		totalItemsSoldContainer = null;

		totalRevenueTitle = null;
		totalGrossTitle = null;
		totalNetSalesTitle = null;
		totalItemsSoldTitle = null;

		totalRevenueInfo = null;
		totalGrossInfo = null;
		totalNetSalesInfo = null;
		totalItemsSoldInfo = null;

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
			var tOfSales = factory.getSalesDao().getTotalOfSales();
			totalOfSales.set(tOfSales);

			SwingUtilities.invokeLater(this::updateTotalOfSalesView);
		} catch (IOException | SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to get summary of sales data", e);
		}
	}

	private void updateTotalOfSalesView() {
		if (!initialized.get()) {
			SwingUtilities.invokeLater(this::initializeComponents);
		}

		SwingUtilities.invokeLater(() -> {
			totalRevenueInfo.setText(
					HtmlUtils.wrapInHtml("₱ " + NumberUtils.formatWithSuffix(totalOfSales.get().totalRevenue())));
			totalGrossInfo.setText(
					HtmlUtils.wrapInHtml("₱ " + NumberUtils.formatWithSuffix(totalOfSales.get().totalGross())));
			totalNetSalesInfo.setText(
					HtmlUtils.wrapInHtml("₱ " + NumberUtils.formatWithSuffix(totalOfSales.get().totalNetSales())));
			totalItemsSoldInfo.setText(
					HtmlUtils.wrapInHtml(NumberUtils.intFormatter.format(totalOfSales.get().totalItemsSold())));
		});
	}

}
