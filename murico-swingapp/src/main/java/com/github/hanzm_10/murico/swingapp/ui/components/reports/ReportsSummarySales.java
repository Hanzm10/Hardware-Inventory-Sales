package com.github.hanzm_10.murico.swingapp.ui.components.reports;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.data.category.DefaultCategoryDataset;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalItemCategorySoldInYear;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.lib.utils.NumberUtils;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ReportsSummarySales {
	private JPanel container;

	private JPanel summaryBoxesContainer;
	private JPanel graphSummaryContainer;

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

	private DefaultCategoryDataset graphDataset;
	private ChartPanel chartPanel;

	public ReportsSummarySales() {
		createComponents();
		attachComponents();
	}

	private void attachComponents() {
		summaryBoxesContainer.add(totalRevenueContainer, "grow");
		totalRevenueContainer.add(totalRevenueTitle, "grow");
		totalRevenueContainer.add(totalRevenueInfo, "grow");

		summaryBoxesContainer.add(totalGrossContainer, "grow,wrap");
		totalGrossContainer.add(totalGrossTitle, "grow");
		totalGrossContainer.add(totalGrossInfo, "grow");

		summaryBoxesContainer.add(totalNetSalesContainer, "grow");
		totalNetSalesContainer.add(totalNetSalesTitle, "grow");
		totalNetSalesContainer.add(totalNetSalesInfo, "grow");

		summaryBoxesContainer.add(totalItemsSoldContainer, "grow");
		totalItemsSoldContainer.add(totalItemsSoldTitle, "grow");
		totalItemsSoldContainer.add(totalItemsSoldInfo, "grow");

		graphSummaryContainer.add(chartPanel, "grow");

		container.add(summaryBoxesContainer, "grow");
		container.add(graphSummaryContainer, "grow");
	}

	private void createComponents() {
		container = new JPanel();
		summaryBoxesContainer = new JPanel();
		graphSummaryContainer = new JPanel();

		container.setLayout(new MigLayout("insets 0, wrap 2", "[grow]", "[grow]"));
		summaryBoxesContainer.setLayout(new MigLayout("insets 0, wrap 2", "[::250px,grow]", "[grow]"));
		graphSummaryContainer.setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));
		totalRevenueContainer = new RoundedPanel(14);
		totalRevenueTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Revenue of All Time"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalRevenueInfo = LabelFactory.createBoldLabel("", 24, Styles.SECONDARY_FOREGROUND_COLOR);

		totalGrossContainer = new RoundedPanel(14);
		totalGrossTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Gross of All Time"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalGrossInfo = LabelFactory.createBoldLabel("", 24, Styles.SECONDARY_FOREGROUND_COLOR);

		totalNetSalesContainer = new RoundedPanel(14);
		totalNetSalesTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Net Sales of All Time"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalNetSalesInfo = LabelFactory.createBoldLabel("", 24, Styles.SECONDARY_FOREGROUND_COLOR);

		totalItemsSoldContainer = new RoundedPanel(14);
		totalItemsSoldTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Total Items Sold of All Time"), 16,
				Styles.SECONDARY_FOREGROUND_COLOR);
		totalItemsSoldInfo = LabelFactory.createBoldLabel("", 24, Styles.SECONDARY_FOREGROUND_COLOR);

		totalRevenueContainer.setBackground(Styles.SECONDARY_COLOR);
		totalGrossContainer.setBackground(Styles.SECONDARY_COLOR);
		totalNetSalesContainer.setBackground(Styles.SECONDARY_COLOR);
		totalItemsSoldContainer.setBackground(Styles.SECONDARY_COLOR);

		totalRevenueContainer.setLayout(new MigLayout("insets 16, wrap", "[150px::,grow]", "[grow]"));
		totalGrossContainer.setLayout(new MigLayout("insets 16, wrap", "[150px::,grow]", "[grow]"));
		totalNetSalesContainer.setLayout(new MigLayout("insets 16, wrap", "[150px::,grow]", "[grow]"));
		totalItemsSoldContainer.setLayout(new MigLayout("insets 16, wrap", "[150px::,grow]", "[grow]"));

		totalRevenueInfo.setHorizontalAlignment(SwingConstants.CENTER);
		totalGrossInfo.setHorizontalAlignment(SwingConstants.CENTER);
		totalNetSalesInfo.setHorizontalAlignment(SwingConstants.CENTER);
		totalItemsSoldInfo.setHorizontalAlignment(SwingConstants.CENTER);

		var border = new FlatRoundBorder();

		border.applyStyleProperty("borderColor", Styles.PRIMARY_COLOR);
		border.applyStyleProperty("borderWidth", 2);
		border.applyStyleProperty("arc", 16);

		totalRevenueContainer.setBorder(border);
		totalGrossContainer.setBorder(border);
		totalNetSalesContainer.setBorder(border);
		totalItemsSoldContainer.setBorder(border);

		initGraph();
	}

	public void destroy() {
	}

	public JPanel getContainer() {
		return container;
	}

	private void initGraph() {
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

		// Optionally: adjust font, position, etc.
		// renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 12));

		plot.setRenderer(renderer);

		chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(new FlatRoundBorder());
	}

	public void setTotalGrossInfo(@NotNull final double totalGross) {
		totalGrossInfo.setText(HtmlUtils.wrapInHtml("₱ " + NumberUtils.formatWithSuffix(totalGross)));
	}

	public void setTotalItemsSoldInfo(@NotNull final int totalItemsSold) {
		totalItemsSoldInfo.setText(HtmlUtils.wrapInHtml(NumberUtils.intFormatter.format(totalItemsSold)));
	}

	public void setTotalNetSalesInfo(@NotNull final double totalNetSales) {
		totalNetSalesInfo.setText(HtmlUtils.wrapInHtml("₱ " + NumberUtils.formatWithSuffix(totalNetSales)));
	}

	public void setTotalRevenueInfo(@NotNull final double totalRevenue) {
		totalRevenueInfo.setText(HtmlUtils.wrapInHtml("₱ " + NumberUtils.formatWithSuffix(totalRevenue)));
	}

	public void updateGraph(@NotNull final TotalItemCategorySoldInYear[] totalItemCategorySoldInYears) {
		for (var itemCategorySoldInYear : totalItemCategorySoldInYears) {

			graphDataset.addValue(itemCategorySoldInYear.totalQuanitySold(), itemCategorySoldInYear.itemCategory(),
					itemCategorySoldInYear.year());
		}
	}
}
