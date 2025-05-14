package com.github.hanzm_10.murico.swingapp.ui.components.reports;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.CustomerPayment;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.lib.utils.NumberUtils;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class ReportsSalesTable {
	class AmountComparator implements Comparator<Object> {
		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof String && o2 instanceof String) {
				String str1 = (String) o1;
				String str2 = (String) o2;

				// Extract the numeric part of the strings
				double num1 = Double.parseDouble(str1.replace("₱", "").replace(",", ""));
				double num2 = Double.parseDouble(str2.replace("₱", "").replace(",", ""));

				return Double.compare(num1, num2);
			}
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return this.equals(obj);
		}
	}

	class OrderIdComparator implements Comparator<Object> {
		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof String && o2 instanceof String) {
				String str1 = (String) o1;
				String str2 = (String) o2;

				// Extract the numeric part of the strings
				int num1 = Integer.parseInt(str1.replace("#", ""));
				int num2 = Integer.parseInt(str2.replace("#", ""));

				return Integer.compare(num1, num2);
			}
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return this.equals(obj);
		}
	}

	private JPanel container;

	private JLabel labelTitle;
	private JScrollPane scrollPane;
	private DefaultTableModel tableModel;

	private JTable table;

	public ReportsSalesTable() {
		createComponents();
		attachComponents();
	}

	private void attachComponents() {
		container.add(labelTitle);
		container.add(scrollPane, "grow");
	}

	private void createComponents() {
		container = new JPanel();
		container.setLayout(new MigLayout("wrap 1, insets 0", "[grow]", "[]10[grow]"));

		labelTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Sales Breakdown"), 24, Styles.PRIMARY_COLOR);

		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		table = new JTable(tableModel);
		scrollPane = new JScrollPane(table);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		tableModel.setColumnCount(0);

		tableModel.addColumn("Date");
		tableModel.addColumn("Order ID");
		tableModel.addColumn("Amount (PHP)");
		tableModel.addColumn("Payment Method");

		TableRowSorter<TableModel> trs = new TableRowSorter<TableModel>(tableModel);

		trs.setComparator(1, new OrderIdComparator());
		trs.setComparator(2, new AmountComparator());

		table.setRowSorter(trs);
		table.setAutoCreateRowSorter(false);

		// Apply to all columns
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = table.getTableHeader();
		header.setBackground(Styles.SECONDARY_COLOR); // bluish green
		header.setBorder(new FlatRoundBorder());
		header.setForeground(Styles.SECONDARY_FOREGROUND_COLOR);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
	}

	public void destroy() {
	}

	public JPanel getContainer() {
		return container;
	}

	public void setTableData(CustomerPayment[] data) {
		tableModel.setRowCount(0);

		for (var item : data) {
			tableModel
					.addRow(new Object[] {
							item._createdAt().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-",
									"/"),
							"#" + item._customerOrderId(), "₱" + NumberUtils.formatter.format(item.amountPhp()),
							item.paymentMethod() });
		}
	}
}
