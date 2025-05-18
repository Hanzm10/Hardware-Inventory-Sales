package com.github.hanzm_10.murico.swingapp.scenes.home.order_menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat; // For formatting total
import java.util.Vector;

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

import com.github.hanzm_10.murico.swingapp.constants.Styles;
// Use your actual factory DAO class
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;

public class OrderHistoryPanel extends JPanel {

	private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₱ #,##0.00");
	// Column indices constants (optional, but can help maintainability)
	private static final int HISTORY_COL_DATE = 0;

	private static final int HISTORY_COL_ORDER_ID = 1;

//	private static final int HISTORY_COL_CUSTOMER = 2;
	// srivate static final int HISTORY_COL_EMPLOYEE = 3;
	private static final int HISTORY_COL_TOTAL = 4;
	// Removed Branch column index
	private JTable orderHistoryTable;
	private DefaultTableModel orderHistoryTableModel;
	// Removed currentBranchId

	public OrderHistoryPanel() {
		// Removed branchId parameter
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		initializeUI();
	}

	private void initializeUI() {
		JLabel title = new JLabel("Order History (Customer Sales)", SwingConstants.CENTER);
		title.setFont(new Font("Montserrat Bold", Font.BOLD, 16));
		add(title, BorderLayout.NORTH);

		// Updated Column Names (Removed "Branch")
		String[] cols = { "Date", "Order ID", "Customer", "Employee", "Total" };
		orderHistoryTableModel = new DefaultTableModel(cols, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case HISTORY_COL_DATE:
					return Timestamp.class;
				case HISTORY_COL_ORDER_ID:
					return Integer.class;
				case HISTORY_COL_TOTAL:
					return String.class; // Display formatted currency
				// case HISTORY_COL_TOTAL: return BigDecimal.class; // If sorting by raw value
				// needed
				default:
					return String.class;
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		orderHistoryTable = new JTable(orderHistoryTableModel);

		orderHistoryTable.getTableHeader().setBackground(Styles.SECONDARY_COLOR);
		orderHistoryTable.getTableHeader().setForeground(Styles.SECONDARY_FOREGROUND_COLOR);

		orderHistoryTable.setFillsViewportHeight(true);
		orderHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orderHistoryTable.setAutoCreateRowSorter(true); // Enable sorting
		// Optional: Adjust column widths
		orderHistoryTable.getColumnModel().getColumn(HISTORY_COL_DATE).setPreferredWidth(140);
		orderHistoryTable.getColumnModel().getColumn(HISTORY_COL_ORDER_ID).setPreferredWidth(80);
		orderHistoryTable.getColumnModel().getColumn(HISTORY_COL_TOTAL).setPreferredWidth(100);

		add(new JScrollPane(orderHistoryTable), BorderLayout.CENTER);

		// Optional Refresh Button
		JButton refreshButton = new JButton("Refresh History");
		refreshButton.addActionListener(this::loadData); // Call loadData on click
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(refreshButton);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	/** Called by OrderMenuScene when this panel is shown */
	public void loadData() {
		System.out.println("OrderHistoryPanel: Loading data...");
		loadOrderHistoryData();
	}

	private void loadData(java.awt.event.ActionEvent e) {
		loadOrderHistoryData();
	}

	/**
	 * Fetches and displays past customer orders from the database.
	 */
	private void loadOrderHistoryData() {
		if (orderHistoryTableModel == null) {
			return; // Check if UI is ready
		}
		System.out.println("Querying customer order history...");
		orderHistoryTableModel.setRowCount(0); // Clear existing data

		// --- UPDATED SQL QUERY ---
		// Joins customer_orders with users, customers (optional), and SALES table
		// Selects total_price_php from the sales table
		String sql = """
				SELECT
				    co._created_at,          -- Order creation date
				    co._customer_order_id,
				    CASE
				        WHEN c._customer_id IS NULL THEN 'Walk-in'
				        ELSE COALESCE(TRIM(c.first_name || ' ' || c.last_name), c.email, 'Customer #' || c._customer_id)
				    END AS customer_display,
				    u.display_name AS employee_name,
				    s.total_due        -- Get total cost from SALES table
				    -- Add status if available later
				FROM customer_orders co
				JOIN users u ON co._employee_id = u._user_id
				LEFT JOIN customers c ON co._customer_id = c._customer_id
				LEFT JOIN sales s ON co._customer_order_id = s._customer_order_id -- JOIN sales table
				-- No branch filter needed
				ORDER BY co._created_at DESC;
				""";

		try (Connection conn = MySqlFactoryDao.createConnection(); // Use correct factory
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// No parameters needed

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Vector<Object> row = new Vector<>();
					row.add(rs.getTimestamp("_created_at"));
					row.add(rs.getInt("_customer_order_id"));
					row.add(rs.getString("customer_display"));
					row.add(rs.getString("employee_name"));
					// Get total from the sales table's column
					BigDecimal total = rs.getBigDecimal("total_due");
					row.add(total != null ? CURRENCY_FORMAT.format(total) : "N/A"); // Format the total
					// Removed branch column
					orderHistoryTableModel.addRow(row);
				}
				System.out.println("Loaded " + orderHistoryTableModel.getRowCount() + " customer orders.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading order history: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}