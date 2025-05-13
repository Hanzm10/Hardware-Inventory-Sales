package com.github.hanzm_10.murico.swingapp.service.database; // Adjust package if needed

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.InsufficientStockException;

/**
 * Service class containing business logic related to the checkout process,
 * primarily handling order finalization and database interaction.
 */
//Inside CheckoutService.java

public class CheckoutService {

	private static final Logger LOGGER = Logger.getLogger(CheckoutService.class.getName());

	public int finalizeOrder(List<OrderLineItemData> items,
			// Remove parameters no longer inserted into customer_orders
			// BigDecimal paymentAmount, BigDecimal pureTotal,
			// BigDecimal totalWithVat, BigDecimal vatAmount,
			BigDecimal calculatedTotal, // Keep the calculated total for potential use in 'sales' table
			Integer customerId, int employeeId)
			throws SQLException, InsufficientStockException, IllegalArgumentException {

		if (items == null || items.isEmpty()) {
			throw new IllegalArgumentException("Cannot finalize an order with no items.");
		}

		LOGGER.log(Level.INFO, "Attempting to finalize order for employee {0} with {1} item types.",
				new Object[] { employeeId, items.size() });

		Connection conn = null;
		int generatedOrderId = -1;

		try {
			conn = MySqlFactoryDao.createConnection(); // Use correct factory
			if (conn == null) {
				throw new SQLException("Failed database connection.");
			}
			conn.setAutoCommit(false);
			LOGGER.fine("Transaction started.");

			// 1. Insert into customer_orders table
			// ONLY includes columns that exist in the table definition you provided
			// --- CORRECTED SQL AND PARAMETERS ---
			String sqlOrder = """
					INSERT INTO customer_orders (
					    _customer_id, _employee_id, _customer_order_number,
					    discount_type, discount_value, vat_percent, remarks
					) VALUES (?, ?, UUID(), ?, ?, ?, ?)
					"""; // _created_at has default; _customer_order_number has default UUID()

			try (PreparedStatement pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {

				// Parameter 1: _customer_id
				if (customerId != null) {
					pstmtOrder.setInt(1, customerId);
				} else {
					pstmtOrder.setNull(1, Types.INTEGER);
				}
				// Parameter 2: _employee_id
				pstmtOrder.setInt(2, employeeId);
				// Parameter 3: discount_type (defaulting to 'none' for now)
				pstmtOrder.setString(3, "none"); // TODO: Implement discount logic if needed
				// Parameter 4: discount_value (defaulting to 0 for now)
				pstmtOrder.setBigDecimal(4, BigDecimal.ZERO); // TODO: Implement discount logic if needed
				// Parameter 5: vat_percent (defaulting to 12.00 for now)
				pstmtOrder.setBigDecimal(5, new BigDecimal("12.00")); // TODO: Make configurable or get from settings
				// Parameter 6: remarks (setting to null for now)
				pstmtOrder.setNull(6, Types.VARCHAR); // TODO: Allow adding remarks if needed

				int rowsAffected = pstmtOrder.executeUpdate();
				if (rowsAffected == 0) {
					throw new SQLException("Creating customer order failed, no rows affected.");
				}

				try (ResultSet generatedKeys = pstmtOrder.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						generatedOrderId = generatedKeys.getInt(1);
						LOGGER.log(Level.INFO, "Generated Customer Order ID: {0}", generatedOrderId);
					} else {
						throw new SQLException("Creating customer order failed, no ID obtained.");
					}
				}
			} // Closes pstmtOrder

			// 2. Insert into customer_orders_item_stocks and Update item_stocks quantity
			// --- (This part remains largely the same as before) ---
			String sqlItemInsert = "INSERT INTO customer_orders_item_stocks (_customer_order_id, _item_stock_id, price_php, quantity) VALUES (?, ?, ?, ?)";
			String sqlStockUpdate = "UPDATE item_stocks SET quantity = quantity - ? WHERE _item_stock_id = ? AND quantity >= ?";

			try (PreparedStatement pstmtItemInsert = conn.prepareStatement(sqlItemInsert);
					PreparedStatement pstmtStockUpdate = conn.prepareStatement(sqlStockUpdate)) {

				for (OrderLineItemData item : items) {
					if (item.quantity() <= 0) {
						throw new SQLException(/* ... */);
					}
					// Add to item insert batch
					pstmtItemInsert.setInt(1, generatedOrderId);
					pstmtItemInsert.setInt(2, item.itemStockId());
					pstmtItemInsert.setBigDecimal(3, item.priceAtSale()); // Price at time of sale
					pstmtItemInsert.setInt(4, item.quantity());
					pstmtItemInsert.addBatch();

					// Add to stock update batch
					pstmtStockUpdate.setInt(1, item.quantity());
					pstmtStockUpdate.setInt(2, item.itemStockId());
					pstmtStockUpdate.setInt(3, item.quantity()); // Stock check
					pstmtStockUpdate.addBatch();
				}
				// Execute batches and validate results (same as before)
				int[] itemInsertCounts = pstmtItemInsert.executeBatch(); /* Validate */
				int[] stockUpdateCounts = pstmtStockUpdate
						.executeBatch(); /* Validate, throw InsufficientStockException if needed */
				for (int c : itemInsertCounts) {
					if (c == Statement.EXECUTE_FAILED) {
						throw new SQLException("Item insert batch failed.");
					}
				}
				if (stockUpdateCounts.length != items.size()) {
					throw new SQLException("Stock update batch size mismatch.");
				}
				for (int i = 0; i < stockUpdateCounts.length; i++) {
					if (stockUpdateCounts[i] == Statement.EXECUTE_FAILED) {
						throw new SQLException("Stock update batch execution failed.");
					}
					if (stockUpdateCounts[i] == 0) {
						OrderLineItemData failedItem = items.get(i);
						String errorMsg = "Insufficient stock for item '" + failedItem.itemName() + "' (StockID: "
								+ failedItem.itemStockId() + "). Order cannot be completed.";
						LOGGER.log(Level.WARNING, errorMsg);
						throw new InsufficientStockException(errorMsg);
					}
				}

			} // Closes item/stock statements

			// 3. Insert into 'sales' table (assuming this is where totals go now)
			// -----------------------------------------------------------------
			String sqlSales = "INSERT INTO sales (_customer_order_id, total_price_php, total_amount_paid_php, change_php) VALUES (?, ?, ?, ?)";
			try (PreparedStatement pstmtSales = conn.prepareStatement(sqlSales)) {
				// TODO: Get actual payment amount and calculate change
				BigDecimal paymentAmount = calculatedTotal; // Placeholder - assuming exact payment
				BigDecimal changeAmount = paymentAmount.subtract(calculatedTotal); // Placeholder change calc

				pstmtSales.setInt(1, generatedOrderId);
				pstmtSales.setBigDecimal(2, calculatedTotal); // The final total cost
				pstmtSales.setBigDecimal(3, paymentAmount);
				pstmtSales.setBigDecimal(4, changeAmount);

				int rowsAffected = pstmtSales.executeUpdate();
				if (rowsAffected == 0) {
					throw new SQLException("Failed to insert record into sales table.");
				}
				LOGGER.log(Level.INFO, "Sales record created for Order ID: {0}", generatedOrderId);
			}

			// 4. Commit Transaction
			// ---------------------
			conn.commit();
			LOGGER.log(Level.INFO, "Order {0} finalized successfully. Transaction committed.", generatedOrderId);

		} catch (SQLException | InsufficientStockException e) {
			// ... (Error handling / rollback - unchanged) ...
			throw e; // Re-throw
		} finally {
			// ... (Connection cleanup - unchanged) ...
		}

		return generatedOrderId;
	}
}