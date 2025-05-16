package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.comparators.NumberWithSymbolsComparator;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.CustomerPayment;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.CurrencyRenderer;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.IdRenderer;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

class NonEditableTableModel extends DefaultTableModel {
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}

public class SalesReportTable implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(SalesReportTable.class);

	public static final int COL_CUSTOMER_PAYMENT_ID = 0;
	public static final int COL_CUSTOMER_ORDER_ID = 1;
	public static final int COL_DATE = 2;
	public static final int COL_PAYMENT_METHOD = 3;
	public static final int COL_AMOUNT_PHP = 4;

	private JPanel view;

	private JLabel title;
	private DefaultTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;

	private AtomicReference<CustomerPayment[]> customerPayments = new AtomicReference<>(new CustomerPayment[0]);
	private AtomicBoolean initialized = new AtomicBoolean(false);

	private void attachComponents() {
		view.add(title);
		view.add(scrollPane, "grow");
	}

	private void createComponents() {
		view.setLayout(new MigLayout("insets 0, flowy", "[grow]", "[]8px[grow]"));

		title = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Sales Breakdown"), 24, Styles.PRIMARY_COLOR);

		tableModel = new NonEditableTableModel();
		table = new JTable(tableModel);
		scrollPane = new JScrollPane(table);

		var cellRenderer = new DefaultTableCellRenderer();
		var sorter = new TableRowSorter<TableModel>(tableModel);
		var comparator = new NumberWithSymbolsComparator();

		cellRenderer.setHorizontalAlignment(JLabel.CENTER);

		var columnNames = CustomerPayment.getColumnNames();

		for (var columnName : columnNames) {
			tableModel.addColumn(columnName);
		}

		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
		}

		sorter.setComparator(0, comparator);
		sorter.setComparator(1, comparator);
		sorter.setComparator(4, comparator);

		table.setRowSorter(sorter);

		var header = table.getTableHeader();

		header.setBackground(Styles.SECONDARY_COLOR);
		header.setForeground(Styles.SECONDARY_FOREGROUND_COLOR);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		var columnModel = table.getColumnModel();

		columnModel.getColumn(COL_CUSTOMER_PAYMENT_ID).setCellRenderer(new IdRenderer());
		columnModel.getColumn(COL_CUSTOMER_ORDER_ID).setCellRenderer(new IdRenderer());
		columnModel.getColumn(COL_AMOUNT_PHP).setCellRenderer(new CurrencyRenderer());
	}

	@Override
	public void destroy() {
		if (view != null) {
			view.removeAll();
			view = null;
		}

		title = null;
		tableModel = null;
		table = null;
		scrollPane = null;

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
			var customerPaymentsOpened = factory.getSalesDao().getCustomerPayments();
			customerPayments.set(customerPaymentsOpened);

			SwingUtilities.invokeLater(this::updateTableData);
		} catch (IOException | SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to get table data", e);
		}
	}

	private void updateTableData() {
		if (!initialized.get()) {
			initializeComponents();
		}

		var data = customerPayments.get();
		tableModel.setRowCount(0);

		for (var item : data) {
			tableModel.addRow(new Object[] { item._customerPaymentId(), item._customerOrderId(),
					item._createdAt().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "/"),
					item.paymentMethod(), item.amountPhp(), });
		}
	}

}
