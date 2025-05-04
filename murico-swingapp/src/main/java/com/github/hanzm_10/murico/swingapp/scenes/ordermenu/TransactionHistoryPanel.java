package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

// Use your actual factory DAO class
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat; // For formatting amount
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class TransactionHistoryPanel extends JPanel {

    private JTable transactionHistoryTable;
    private DefaultTableModel transactionHistoryTableModel;
    // Removed currentBranchId

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₱ #,##0.00");

    // Column indices constants (optional) - adjust if needed
    private static final int TRANS_COL_DATE = 0;
    private static final int TRANS_COL_TYPE = 1;
    private static final int TRANS_COL_REF_ID = 2;
    private static final int TRANS_COL_DETAILS = 3;
    private static final int TRANS_COL_AMOUNT = 4;
    private static final int TRANS_COL_PARTY = 5;
    private static final int TRANS_COL_EMPLOYEE = 6;
    // Removed Branch column index
    private static final int TRANS_COL_STATUS = 7;


    public TransactionHistoryPanel() {
        // Removed branchId parameter
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        initializeUI();
    }

    private void initializeUI() {
        JLabel title = new JLabel("Transaction History (Sales, Purchases, Payments)", SwingConstants.CENTER);
        title.setFont(new Font("Montserrat Bold", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Updated Column Names (Removed "Branch")
        String[] cols = {"Date", "Type", "Ref ID", "Details", "Amount", "Party", "Employee", "Status"};
        transactionHistoryTableModel = new DefaultTableModel(cols, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
              @Override public Class<?> getColumnClass(int columnIndex) {
                 switch (columnIndex) {
                     case TRANS_COL_DATE: return Timestamp.class;
                     case TRANS_COL_AMOUNT: return String.class; // Formatted Currency
                     // case TRANS_COL_AMOUNT: return BigDecimal.class; // If sorting by raw value needed
                     default: return String.class;
                 }
             }
        };
        transactionHistoryTable = new JTable(transactionHistoryTableModel);
        transactionHistoryTable.setFillsViewportHeight(true);
        transactionHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionHistoryTable.setAutoCreateRowSorter(true);
        // Optional: Adjust column widths
         transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_DATE).setPreferredWidth(140);
         transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_DETAILS).setPreferredWidth(200);
         transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_AMOUNT).setPreferredWidth(100);
         transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_PARTY).setPreferredWidth(120);
         transactionHistoryTable.getColumnModel().getColumn(TRANS_COL_STATUS).setPreferredWidth(100);


        add(new JScrollPane(transactionHistoryTable), BorderLayout.CENTER);

        // Optional Refresh Button
        JButton refreshButton = new JButton("Refresh History");
        refreshButton.addActionListener(e -> loadData());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

     /** Called by OrderMenuScene when this panel is shown */
    public void loadData() {
         System.out.println("TransactionHistoryPanel: Loading data...");
         loadTransactionHistoryData();
    }

    /**
     * Fetches and displays combined transaction history.
     * NOTE: This query is complex and assumes certain interpretations.
     * Excludes item_restocks and refunds for now based on schema complexity.
     */
    private void loadTransactionHistoryData() {
        if (transactionHistoryTableModel == null) return;
        System.out.println("Querying transaction history...");
        transactionHistoryTableModel.setRowCount(0);

        // --- UPDATED SQL QUERY (Using UNION ALL, No Branch Filter, Uses sales table) ---
        String sql = """
            SELECT trans_date, trans_type, ref_id, details, amount, party, employee, status FROM (
                -- Customer Sales (using sales table total)
                SELECT
                    co._created_at AS trans_date, 'Sale' AS trans_type,
                    co._customer_order_id AS ref_id,
                    co._customer_order_number AS details, -- Show order number
                    s.total_price_php AS amount,           -- Get total from SALES table
                    CASE WHEN c._customer_id IS NULL THEN 'Walk-in' ELSE COALESCE(TRIM(c.first_name||' '||c.last_name), c.email, 'Cust#'||c._customer_id) END AS party,
                    u.display_name AS employee,
                    'Completed' AS status -- Assume sales are completed? Or add status to sales/customer_orders?
                FROM customer_orders co
                JOIN users u ON co._employee_id = u._user_id
                LEFT JOIN customers c ON co._customer_id = c._customer_id
                LEFT JOIN sales s ON co._customer_order_id = s._customer_order_id -- Join Sales table

                UNION ALL

                -- Supplier Purchases (Calculate total from items_orders) - Show when 'delivered'?
                SELECT
                    o._created_at AS trans_date, -- Or status_updated_at when delivered?
                    'Purchase' AS trans_type,
                    o._order_id AS ref_id,
                    o._order_number AS details, -- Show PO number
                    (SELECT SUM(io.quantity * io.wsp_php) FROM items_orders io WHERE io._order_id = o._order_id) AS amount, -- Calculate total
                    s.name AS party,
                    e.display_name AS employee, -- Employee who created PO
                    o.status AS status          -- Status from supplier orders
                FROM orders o
                JOIN suppliers s ON o._supplier_id = s._supplier_id
                JOIN users e ON o._employee_id = e._user_id
                WHERE o.status = 'delivered' -- Filter example: Only show delivered POs?

                UNION ALL

                -- Customer Payments
                SELECT
                    cp._created_at AS trans_date, 'Payment (Cust)' AS trans_type,
                    cp._customer_payment_id AS ref_id,
                    'Order #' || cp._customer_order_id || ' (' || cp.payment_method || ')' AS details,
                    cp.amount_php AS amount,
                    CASE WHEN c._customer_id IS NULL THEN 'Walk-in' ELSE COALESCE(TRIM(c.first_name||' '||c.last_name), c.email, 'Cust#'||c._customer_id) END AS party,
                    NULL AS employee, -- Payment might not directly link to employee?
                    'Completed' AS status
                FROM customer_payments cp
                JOIN customer_orders co ON cp._customer_order_id = co._customer_order_id
                LEFT JOIN customers c ON co._customer_id = c._customer_id

                UNION ALL

                 -- Supplier Payments (Order Payments)
                SELECT
                    op._created_at AS trans_date, 'Payment (Supp)' AS trans_type,
                    op._order_payment_id AS ref_id,
                    'PO #' || op._order_id || ' (' || op.payment_method || ')' AS details,
                    -op.amount_php AS amount, -- Showing as negative (expense)
                    s.name AS party,
                    NULL AS employee,
                    'Completed' AS status
                FROM order_payments op
                JOIN orders o ON op._order_id = o._order_id
                JOIN suppliers s ON o._supplier_id = s._supplier_id

                -- UNION ALL for Refunds could be added here if schema supports easy joining
                -- SELECT cr._created_at, 'Refund', cr._refund_id, ..., -cr.refund_amount, ... FROM customer_refunds cr ...

            ) AS combined_transactions
            ORDER BY trans_date DESC;
            """;

         try (Connection conn = MySqlFactoryDao.createConnection(); // Use correct factory
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
             // No parameters needed as branch filter is removed

             try(ResultSet rs = pstmt.executeQuery()) {
                 while(rs.next()) {
                     Vector<Object> row = new Vector<>();
                     row.add(rs.getTimestamp("trans_date"));
                     row.add(rs.getString("trans_type"));
                     row.add(rs.getObject("ref_id")); // Use getObject for potentially different types (INT vs UUID maybe later)
                     row.add(rs.getString("details"));
                     BigDecimal amount = rs.getBigDecimal("amount");
                     row.add(amount != null ? CURRENCY_FORMAT.format(amount) : "N/A"); // Format amount
                     row.add(rs.getString("party"));
                     row.add(rs.getString("employee")); // Might be null
                     // Removed Branch column
                     row.add(rs.getString("status"));
                     transactionHistoryTableModel.addRow(row);
                 }
                  System.out.println("Loaded " + transactionHistoryTableModel.getRowCount() + " transactions.");
             }
         } catch (SQLException e) {
              e.printStackTrace();
              JOptionPane.showMessageDialog(this, "Error loading transaction history: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
         }
    }
} // End TransactionHistoryPanel