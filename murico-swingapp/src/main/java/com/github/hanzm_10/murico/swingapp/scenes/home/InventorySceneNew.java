package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.ProgressLevelRenderer.StockInfo;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryHeader;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryTable;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.AddItemDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs.EditItemStockDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs.InventoryFilterDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs.RestockItemDialog;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;

import net.miginfocom.swing.MigLayout;

public class InventorySceneNew implements Scene, DocumentListener {

	private static final Logger LOGGER = MuricoLogger.getLogger(InventorySceneNew.class);

	private JPanel view;

	private InventoryTable inventoryTable;
	private InventoryHeader inventoryHeader;

	private AddItemDialog addItemDialog;

	private Thread inventoryTableThread;

	private String activeCategoryFilter = "ALL";
	private String activeSupplierFilter = "ALL";

	public void applyTableFilters() {
		if (inventoryTable.getRowSorter() == null) {
			LOGGER.severe("Sorter not initialized. Cannot apply filters.");
			return;
		}

		var searchField = inventoryHeader.getSearchField();

		List<RowFilter<Object, Object>> combinedFilters = new ArrayList<>();
		if (!"ALL".equalsIgnoreCase(activeCategoryFilter)) {
			combinedFilters.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(activeCategoryFilter) + "$",
					InventoryTable.COL_CATEGORY_TYPE));
		}
		if (!"ALL".equalsIgnoreCase(activeSupplierFilter)) {
			combinedFilters.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(activeSupplierFilter) + "$",
					InventoryTable.COL_SUPPLIER_NAME));
		}

		String searchText = (searchField != null) ? searchField.getText().trim() : "";
		if (!searchText.isEmpty()) {
			try {
				String regex = "(?i)" + Pattern.quote(searchText);
				List<RowFilter<Object, Object>> textSearchORFilters = new ArrayList<>();
				textSearchORFilters.add(RowFilter.regexFilter(regex, InventoryTable.COL_ITEM_NAME));
				textSearchORFilters.add(RowFilter.regexFilter(regex, InventoryTable.COL_ITEM_ID));

				combinedFilters.add(RowFilter.orFilter(textSearchORFilters));
			} catch (PatternSyntaxException pse) {
				LOGGER.severe("Search text created invalid regex: " + pse.getMessage());
			}
		}

		if (combinedFilters.isEmpty()) {
			inventoryTable.getRowSorter().setRowFilter(null);
		} else {
			inventoryTable.getRowSorter().setRowFilter(RowFilter.andFilter(combinedFilters));
		}
	}

	public boolean archiveCoreItem(int itemId) {
		String updateItemSql = "UPDATE items SET is_deleted = TRUE WHERE _item_id = ?;";
		String updateItemStocksSql = "UPDATE item_stocks SET is_deleted = TRUE WHERE _item_id = ?;";
		Connection conn = null;
		boolean success = false;
		try {
			conn = MySqlFactoryDao.createConnection();
			conn.setAutoCommit(false);
			int itemRowsAffected = 0;
			try (PreparedStatement pstmtItem = conn.prepareStatement(updateItemSql)) {
				pstmtItem.setInt(1, itemId);
				itemRowsAffected = pstmtItem.executeUpdate();
			}
			if (itemRowsAffected > 0) {
				try (PreparedStatement pstmtStocks = conn.prepareStatement(updateItemStocksSql)) {
					pstmtStocks.setInt(1, itemId);
					int stockRowsAffected = pstmtStocks.executeUpdate();
					LOGGER.info("Archived " + stockRowsAffected + " stock variants for item ID: " + itemId);
				}
				conn.commit();
				success = true;
				LOGGER.info("Item ID: " + itemId + " archived successfully.");
			} else {
				conn.rollback();
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
						"Item not found or already archived.", "Archive Not Performed",
						JOptionPane.INFORMATION_MESSAGE);
				LOGGER.warning("Item ID: " + itemId + " not found or already archived.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error archiving item: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	private void attachComponents() {
		view.setLayout(new MigLayout("insets 0, flowy", "[grow]", "[]16[grow]"));

		view.add(inventoryHeader.getView(), "growx");
		view.add(inventoryTable.getView(), "grow");

		inventoryHeader.initializeComponents();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(this::performSearch);
	}

	private void createComponents() {
		inventoryHeader = new InventoryHeader(this::listenToHeaderButtonEvents, this);
		inventoryTable = new InventoryTable(this);
	}

	@Override
	public String getSceneName() {
		return "inventory";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void handleAddCommand() {
		if (addItemDialog == null) {
			addItemDialog = new AddItemDialog(SwingUtilities.getWindowAncestor(view), this);
		}

		addItemDialog.setVisible(true);
	}

	public void handleDeleteItem(int modelRow) {
		if (inventoryTable.getTable() == null || inventoryTable.getTable() == null || modelRow < 0
				|| modelRow >= inventoryTable.getTable().getRowCount()) {
			LOGGER.warning("Invalid model row index: " + modelRow);
			return;
		}
		try {
			StockInfo stockInfo = (StockInfo) inventoryTable.getTable().getValueAt(modelRow,
					InventoryTable.COL_STOCK_QUANTITY);
			int itemIdToArchive = stockInfo.getItemId();
			String productName = (String) inventoryTable.getTable().getValueAt(modelRow, InventoryTable.COL_ITEM_NAME);
			int confirm = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(view),
					"Are you sure you want to archive the item '" + productName + "' (Item ID: " + itemIdToArchive
							+ ")?\n" + "This will hide the item and all its stock variants from active views.\n"
							+ "It can usually be recovered by an administrator.",
					"Confirm Archive Item", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION) {
				LOGGER.info("Attempting to archive core Item ID: " + itemIdToArchive);
				boolean success = archiveCoreItem(itemIdToArchive);
				if (success) {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
							"Item '" + productName + "' and its stock have been archived.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					refreshTableData();
				} else {
					LOGGER.severe("Archiving failed for core item ID: " + itemIdToArchive + " (parent notification)");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error preparing for item archival: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void handleFilterCommand() {
		Window owner = SwingUtilities.getWindowAncestor(view);
		InventoryFilterDialog dialog = new InventoryFilterDialog(owner, this, activeCategoryFilter,
				activeSupplierFilter);
		dialog.setVisible(true);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(this::performSearch);
	}

	private void listenToHeaderButtonEvents(ActionEvent ev) {
		var command = ev.getActionCommand();

		switch (command) {
		case InventoryHeader.ADD_COMMAND:
			handleAddCommand();
			break;
		case InventoryHeader.FILTER_COMMAND:
			handleFilterCommand();
			break;
		}
	}

	@Override
	public void onBeforeShow() {
		terminateThreads();

		inventoryTableThread = new Thread(inventoryTable::performBackgroundTask);

		inventoryTableThread.start();
	}

	@Override
	public void onCreate() {
		createComponents();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		terminateThreads();

		if (inventoryTable != null) {
			inventoryTable.destroy();
		}

		inventoryHeader.destroy();
		inventoryTable.destroy();

		return true;
	}

	@Override
	public void onHide() {
		terminateThreads();
	}

	public void openEditItemDialog(int viewRow) {
		if (inventoryTable.getTable() == null || inventoryTable.getTable() == null) {
			return;
		}
		int modelRow = inventoryTable.getTable().convertRowIndexToModel(viewRow);
		if (modelRow < 0 || modelRow >= inventoryTable.getTable().getRowCount()) {
			LOGGER.severe("EditItem: Invalid modelRow (" + modelRow + ") from viewRow: " + viewRow);
			return;
		}
		try {
			String productName = (String) inventoryTable.getTable().getValueAt(modelRow, InventoryTable.COL_ITEM_NAME);
			StockInfo stockInfo = (StockInfo) inventoryTable.getTable().getValueAt(modelRow,
					InventoryTable.COL_STOCK_QUANTITY);
			BigDecimal unitPrice = (BigDecimal) inventoryTable.getTable().getValueAt(modelRow,
					InventoryTable.COL_UNIT_PRICE);
			int itemStockId = (Integer) inventoryTable.getTable().getValueAt(modelRow,
					InventoryTable.COL_ITEM_STOCK_ID);
			LOGGER.info("Opening Edit Dialog for Item Stock ID: " + itemStockId);
			Window owner = SwingUtilities.getWindowAncestor(view);
			EditItemStockDialog dialog = new EditItemStockDialog(owner, this, itemStockId, productName,
					stockInfo.minimumProgressLevel(), unitPrice, stockInfo.currentProgressLevel());
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error retrieving item data for editing: " + e.getMessage(), "Data Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void openEditItemDialog1(int viewRow) {
		if (inventoryTable.getTable() == null || inventoryTable.getTableModel() == null) {
			return;
		}

		int modelRow = inventoryTable.getTable().convertRowIndexToModel(viewRow);

		if (modelRow < 0 || modelRow >= inventoryTable.getTableModel().getRowCount()) {
			LOGGER.severe("EditItem: Invalid modelRow (" + modelRow + ") from viewRow: " + viewRow);
			return;
		}

		try {
			String productName = (String) inventoryTable.getTableModel().getValueAt(modelRow,
					InventoryTable.COL_ITEM_NAME);
			StockInfo stockInfo = (StockInfo) inventoryTable.getTableModel().getValueAt(modelRow,
					InventoryTable.COL_STOCK_QUANTITY);
			BigDecimal unitPrice = (BigDecimal) inventoryTable.getTableModel().getValueAt(modelRow,
					InventoryTable.COL_UNIT_PRICE);
			int itemStockId = (Integer) inventoryTable.getTableModel().getValueAt(modelRow,
					InventoryTable.COL_ITEM_STOCK_ID);
			LOGGER.info("Opening Edit Dialog for Item Stock ID: " + itemStockId);
			Window owner = SwingUtilities.getWindowAncestor(view);
			EditItemStockDialog dialog = new EditItemStockDialog(owner, this, itemStockId, productName,
					stockInfo.minimumProgressLevel(), unitPrice, stockInfo.currentProgressLevel());
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error retrieving item data for editing: " + e.getMessage(), "Data Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void openRestockDialog(int modelRow) {
		if (inventoryTable.getTable() == null || inventoryTable.getTable() == null || modelRow < 0
				|| modelRow >= inventoryTable.getTable().getRowCount()) {
			LOGGER.severe("RestockItem: Invalid modelRow: " + modelRow);
			return;
		}
		try {
			int itemStockId = (Integer) inventoryTable.getTable().getValueAt(modelRow,
					InventoryTable.COL_ITEM_STOCK_ID);
			String productName = (String) inventoryTable.getTable().getValueAt(modelRow, InventoryTable.COL_ITEM_NAME);
			StockInfo stockInfo = (StockInfo) inventoryTable.getTable().getValueAt(modelRow,
					InventoryTable.COL_STOCK_QUANTITY);
			int currentQuantity = stockInfo.currentProgressLevel();
			int coreItemId = stockInfo.getItemId();
			LOGGER.info("Opening Restock Dialog for Item Stock ID: " + itemStockId + ", Core Item ID: " + coreItemId);
			Window owner = SwingUtilities.getWindowAncestor(view);
			RestockItemDialog dialog = new RestockItemDialog(owner, this, itemStockId, coreItemId, productName,
					currentQuantity);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error preparing for restock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void performSearch() {
		applyTableFilters();
	}

	public boolean processRestock(int itemStockId, int quantityToAdd, int quantityBefore) {
		if (quantityToAdd <= 0) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view), "Quantity to add must be positive.",
					"Invalid Input", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		Connection conn = null;
		boolean success = false;
		int quantityAfter = quantityBefore + quantityToAdd;
		try {
			conn = MySqlFactoryDao.createConnection();
			conn.setAutoCommit(false);
			String updateStockSql = "UPDATE item_stocks SET quantity = ? WHERE _item_stock_id = ? AND is_deleted = FALSE";
			try (PreparedStatement pstmtStock = conn.prepareStatement(updateStockSql)) {
				pstmtStock.setInt(1, quantityAfter);
				pstmtStock.setInt(2, itemStockId);
				int rowsAffected = pstmtStock.executeUpdate();
				if (rowsAffected == 0) {
					throw new SQLException(
							"Restock failed: Item stock record not found, not updated, or already deleted. Stock ID: "
									+ itemStockId);
				}
			}
			String insertRestockSql = "INSERT INTO item_restocks (_item_stock_id, quantity_before, quantity_after, quantity_added) VALUES (?, ?, ?, ?)";
			try (PreparedStatement pstmtRestock = conn.prepareStatement(insertRestockSql)) {
				pstmtRestock.setInt(1, itemStockId);
				pstmtRestock.setInt(2, quantityBefore);
				pstmtRestock.setInt(3, quantityAfter);
				pstmtRestock.setInt(4, quantityToAdd);
				pstmtRestock.executeUpdate();
			}
			conn.commit();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error processing restock: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		return success;
	}

	public void refreshTableData() {
		inventoryTable.refresh();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(this::performSearch);
	}

	public void setCurrentCategoryFilter(String category) {
		this.activeCategoryFilter = category;
		applyTableFilters();
	}

	public void setCurrentSupplierFilter(String supplier) {
		this.activeSupplierFilter = supplier;
		applyTableFilters();
	}

	private void terminateThreads() {
		if (inventoryTableThread != null && inventoryTableThread.isAlive()) {
			inventoryTableThread.interrupt();
			ConnectionManager.cancel(inventoryTableThread);
		}

	}

	public boolean updateItemStockDetails(int itemStockId, BigDecimal newPrice, int newMinQuantity) {
		String sql = "UPDATE item_stocks SET price_php = ?, minimum_quantity = ? WHERE _item_stock_id = ?";
		try (Connection conn = MySqlFactoryDao.createConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setBigDecimal(1, newPrice);
			pstmt.setInt(2, newMinQuantity);
			pstmt.setInt(3, itemStockId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error updating stock details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}