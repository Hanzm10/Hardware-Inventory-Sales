package com.github.hanzm_10.murico.swingapp.scenes.home.order_menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.comparators.NumberWithSymbolsComparator;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.CurrencyRenderer;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.DateRenderer;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.IdRenderer;

public class TransactionHistoryPanel extends JPanel {

	private static final Logger LOGGER = MuricoLogger.getLogger(TransactionHistoryPanel.class);

	// Column indices constants (optional) - adjust if needed
	private static final int TRANS_COL_DATE = 0;

	// private static final int TRANS_COL_TYPE = 1;

	private static final int TRANS_COL_REF_ID = 2;
	private static final int TRANS_COL_DETAILS = 3;
	private static final int TRANS_COL_AMOUNT = 4;
	private static final int TRANS_COL_PARTY = 5;
	// private static final int TRANS_COL_EMPLOYEE = 6;
	// Removed Branch column index
	private static final int TRANS_COL_STATUS = 7;
	private JTable transactionHistoryTable;
	private DefaultTableModel transactionHistoryTableModel;
	// Removed currentBranchId

	private JButton refreshButton;

	public TransactionHistoryPanel() {
		// Removed branchId parameter
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		initializeUI();
	}

	public void destroy() {
		refreshButton.removeActionListener(this::loadData); // Remove listener
	}

	private void initializeUI() {
		JLabel title = new JLabel("Transaction History (Sales, Purchases, Payments)", SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
		add(title, BorderLayout.NORTH);

		// Updated Column Names (Removed "Branch")
		String[] cols = { "Date", "Type", "ID", "Order Number", "Amount", "Party", "Employee", "Status" };
		transactionHistoryTableModel = new DefaultTableModel(cols, 0);
		transactionHistoryTable = new JTable(transactionHistoryTableModel);

		transactionHistoryTable.getTableHeader().setBackground(Styles.SECONDARY_COLOR);
		transactionHistoryTable.getTableHeader().setForeground(Styles.SECONDARY_FOREGROUND_COLOR);

		transactionHistoryTable.setFillsViewportHeight(true);
		transactionHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_DATE).setPreferredWidth(140);
		transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_DETAILS).setPreferredWidth(200);
		transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_AMOUNT).setPreferredWidth(100);
		transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_PARTY).setPreferredWidth(120);
		transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_STATUS).setPreferredWidth(100);

		var sorter = new TableRowSorter<TableModel>(transactionHistoryTableModel);
		var comparator = new NumberWithSymbolsComparator();

		sorter.setComparator(TRANS_COL_REF_ID, comparator);
		sorter.setComparator(TRANS_COL_AMOUNT, comparator);

		transactionHistoryTable.setRowSorter(sorter);

		var currencyRenderer = new CurrencyRenderer();
		var idRenderer = new IdRenderer();
		var dateRenderer = new DateRenderer();

		idRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		currencyRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		dateRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_DATE).setCellRenderer(dateRenderer);
		transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_REF_ID).setCellRenderer(idRenderer);
		transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_AMOUNT).setCellRenderer(currencyRenderer);

		add(new JScrollPane(transactionHistoryTable), BorderLayout.CENTER);

		refreshButton = new JButton("Refresh History");
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(refreshButton);
		add(bottomPanel, BorderLayout.SOUTH);
		refreshButton.addActionListener(this::loadData); // Call loadData on click
	}

	public void loadData() {
		loadTransactionHistoryData();
	}

	private void loadData(java.awt.event.ActionEvent e) {
		loadTransactionHistoryData();
	}

	private void loadTransactionHistoryData() {
		if (transactionHistoryTableModel == null) {
			return;
		}

		transactionHistoryTableModel.setRowCount(0);

		try {
			var transactions = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getSalesDao()
					.getTransactionHistory();

			for (var transaction : transactions) {
				var orderNumber = transaction.orderNumber() == null ? "N/A" : transaction.orderNumber();
				var employeeHandler = transaction.employeeHandler() == null ? "N/A" : transaction.employeeHandler();

				Object[] rowData = { transaction.transactionDate(), transaction.transactionType(), transaction.refId(),
						orderNumber, transaction.amount(), transaction.party(), employeeHandler, transaction.status() };

				transactionHistoryTableModel.addRow(rowData);
			}

		} catch (SQLException | IOException e) {
			LOGGER.log(Level.SEVERE, "Error loading transaction history", e);

			JOptionPane.showMessageDialog(this, "Error loading transaction history: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}
} // End TransactionHistoryPanel