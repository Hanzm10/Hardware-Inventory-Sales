/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs.AddItemDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs.EditItemStockDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs.InventoryFilterDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs.RestockItemDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.editors.ButtonEditor;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.renderers.ButtonRenderer;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.renderers.CurrencyRenderer;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.renderers.ItemIdRenderer;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.renderers.ProductNameRenderer;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.renderers.StockInfo;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.renderers.StockLevelRenderer;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;

import net.miginfocom.swing.MigLayout;

public class InventoryScene implements Scene, DocumentListener {
	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryScene.class);

	// --- Constants for Columns ---
	public static final int COL_PRODUCT_NAME = 0;
	public static final int COL_ITEM_ID = 1;
	public static final int COL_CATEGORY = 2;
	public static final int COL_PACK_TYPE = 3;
	public static final int COL_SUPPLIER = 4;
	public static final int COL_STOCK_LEVEL = 5;
	public static final int COL_UNIT_PRICE = 6;
	public static final int COL_ACTION = 7;
	public static final int HIDDEN_COL_ITEM_STOCK_ID = 8;

	private JPanel view;

	// --- UI Components ---
	private JTable table;
	private DefaultTableModel tableModel;
	private TableRowSorter<DefaultTableModel> sorter;
	private JTextField searchField;
	private JButton filterButton;
	private JButton addButton;

	// Fields to store current filter state
	private String activeCategoryFilter = "ALL";
	private String activeSupplierFilter = "ALL";

	private JScrollPane scrollPane;

	private RoundedPanel tealTopBar;
	private JPanel tealTopBarRightActionPanel;

	public void applyTableFilters() {
		if (sorter == null) {
			LOGGER.severe("Sorter not initialized. Cannot apply filters.");
			return;
		}

		List<RowFilter<Object, Object>> combinedFilters = new ArrayList<>();
		if (!"ALL".equalsIgnoreCase(activeCategoryFilter)) {
			combinedFilters
					.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(activeCategoryFilter) + "$", COL_CATEGORY));
		}
		if (!"ALL".equalsIgnoreCase(activeSupplierFilter)) {
			combinedFilters
					.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(activeSupplierFilter) + "$", COL_SUPPLIER));
		}

		String searchText = (searchField != null) ? searchField.getText().trim() : "";
		if (!searchText.isEmpty()) {
			try {
				String regex = "(?i)" + Pattern.quote(searchText);
				List<RowFilter<Object, Object>> textSearchORFilters = new ArrayList<>();
				textSearchORFilters.add(RowFilter.regexFilter(regex, COL_PRODUCT_NAME));
				textSearchORFilters.add(RowFilter.regexFilter(regex, COL_ITEM_ID));

				combinedFilters.add(RowFilter.orFilter(textSearchORFilters));
			} catch (PatternSyntaxException pse) {
				LOGGER.severe("Search text created invalid regex: " + pse.getMessage());
			}
		}

		if (combinedFilters.isEmpty()) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(RowFilter.andFilter(combinedFilters));
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
		tealTopBarRightActionPanel.add(filterButton);
		tealTopBarRightActionPanel.add(searchField);

		tealTopBar.add(addButton, "cell 0 0");
		tealTopBar.add(tealTopBarRightActionPanel, "cell 1 0");

		view.add(tealTopBar, BorderLayout.NORTH);
		view.add(scrollPane);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(this::performSearch);
	}

	private void createComponents() {
		createTable();
		createTealTopBar();

		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	private JButton createStyledIconButton(String svgPath, String toolTip, int width, int height) {
		JButton button = new JButton();
		try {
			ImageIcon icon = AssetManager.getOrLoadIcon(svgPath);
			if (icon != null) {
				button.setIcon(icon);
			} else {
				button.setText(toolTip.length() > 1 ? toolTip.substring(0, 1) : toolTip);
			}
		} catch (Exception e) {
			LOGGER.severe("Error loading icon via AssetManager: " + svgPath + " - " + e.getMessage());
			e.printStackTrace();
			button.setText(toolTip.length() > 1 ? toolTip.substring(0, 1) : toolTip);
		}
		button.setToolTipText(toolTip);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return button;
	}

	private void createTable() {
		table = new JTable();
		table.setRowHeight(40);
		table.setGridColor(new Color(220, 220, 220));
		table.setShowGrid(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);

		var header = table.getTableHeader();

		header.setBackground(Styles.SECONDARY_COLOR);
		header.setForeground(Styles.SECONDARY_FOREGROUND_COLOR);

		header.setReorderingAllowed(false);
		table.setFillsViewportHeight(true);

		String[] columnNames = {"Product Name", "Item ID", "Category", "Pack Type", "Supplier", "Stock Level",
				"Unit Price", "", "_ItemStockID"};
		tableModel = new DefaultTableModel(null, columnNames) {
			@Override
			public Class<?> getColumnClass(int i) {
				switch (i) {
					case COL_ITEM_ID :
						return String.class;
					case COL_PACK_TYPE :
						return String.class;
					case COL_STOCK_LEVEL :
						return StockInfo.class;
					case COL_UNIT_PRICE :
						return BigDecimal.class;
					case COL_ACTION :
						return JButton.class;
					case HIDDEN_COL_ITEM_STOCK_ID :
						return Integer.class;
					default :
						return String.class;
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == COL_ACTION;
			}
		};
		table.setModel(tableModel);
		sorter = new TableRowSorter<DefaultTableModel>(tableModel);
		table.setRowSorter(sorter);
	}

	private void createTealTopBar() {
		tealTopBar = new RoundedPanel(20);
		tealTopBar.setLayout(new MigLayout("insets 8, filly", "[]push[]", "[grow]"));

		tealTopBar.setBackground(new Color(0x337E8F));

		addButton = createStyledIconButton("icons/add_button.svg", "Add New Item", 24, 24);

		addButton.setBackground(new Color(0x00, true));
		addButton.setBorder(null);
		addButton.addActionListener(this::openAddItemDialog);

		tealTopBarRightActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		tealTopBarRightActionPanel.setOpaque(false);

		filterButton = createStyledIconButton("icons/filter_icon.svg", "Filter Options", 22, 22);
		filterButton.setBackground(new Color(0x00, true));
		filterButton.setBorder(null);
		filterButton.addActionListener(this::openFilterDialog); // Attach listener

		searchField = new JTextField(20);
		searchField.setPreferredSize(new Dimension(searchField.getPreferredSize().width, 28));
		searchField.getDocument().addDocumentListener(this);
	}

	@Override
	public String getSceneName() {
		return "inventory";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	public void handleDeleteItem(int modelRow) {
		if (table == null || table == null || modelRow < 0 || modelRow >= table.getRowCount()) {
			LOGGER.warning("Invalid model row index: " + modelRow);
			return;
		}
		try {
			StockInfo stockInfo = (StockInfo) table.getValueAt(modelRow, COL_STOCK_LEVEL);
			int itemIdToArchive = stockInfo.getItemId();
			String productName = (String) table.getValueAt(modelRow, COL_PRODUCT_NAME);
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

	private void hideColumn(int columnIndex) {
		if (table != null && table.getColumnCount() > columnIndex && columnIndex >= 0) {
			TableColumnModel tcm = table.getColumnModel();
			tcm.getColumn(columnIndex).setMinWidth(0);
			tcm.getColumn(columnIndex).setMaxWidth(0);
			tcm.getColumn(columnIndex).setWidth(0);
			tcm.getColumn(columnIndex).setPreferredWidth(0);
		} else {
			LOGGER.severe("Warning: Could not hide column at index " + columnIndex);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(this::performSearch);
	}

	@Override
	public void onCreate() {
		view.setLayout(new BorderLayout(0, 10));

		createComponents();
		setupTableRenderersAndEditors();
		attachComponents();
	}

	@Override
	public void onHide() {
		view.removeAll();
	}

	@Override
	public void onShow() {
		attachComponents();

		if (searchField != null) {
			searchField.setText("");
		}

		activeCategoryFilter = "ALL";
		activeSupplierFilter = "ALL";

		refreshTableData();
	}

	private void openAddItemDialog(ActionEvent ev) {
		Window owner = SwingUtilities.getWindowAncestor(view);
		AddItemDialog dialog = new AddItemDialog(owner, this);
		dialog.setVisible(true);
	}

	public void openEditItemDialog(int viewRow) {
		if (table == null || table == null) {
			return;
		}
		int modelRow = table.convertRowIndexToModel(viewRow);
		if (modelRow < 0 || modelRow >= table.getRowCount()) {
			LOGGER.severe("EditItem: Invalid modelRow (" + modelRow + ") from viewRow: " + viewRow);
			return;
		}
		try {
			String productName = (String) table.getValueAt(modelRow, COL_PRODUCT_NAME);
			StockInfo stockInfo = (StockInfo) table.getValueAt(modelRow, COL_STOCK_LEVEL);
			BigDecimal unitPrice = (BigDecimal) table.getValueAt(modelRow, COL_UNIT_PRICE);
			int itemStockId = (Integer) table.getValueAt(modelRow, HIDDEN_COL_ITEM_STOCK_ID);
			LOGGER.info("Opening Edit Dialog for Item Stock ID: " + itemStockId);
			Window owner = SwingUtilities.getWindowAncestor(view);
			EditItemStockDialog dialog = new EditItemStockDialog(owner, this, itemStockId, productName,
					stockInfo.getMinimumQuantity(), unitPrice, stockInfo.getQuantity());
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error retrieving item data for editing: " + e.getMessage(), "Data Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void openEditItemDialog1(int viewRow) {
		if (table == null || tableModel == null) {
			return;
		}

		int modelRow = table.convertRowIndexToModel(viewRow);

		if (modelRow < 0 || modelRow >= tableModel.getRowCount()) {
			LOGGER.severe("EditItem: Invalid modelRow (" + modelRow + ") from viewRow: " + viewRow);
			return;
		}

		try {
			String productName = (String) tableModel.getValueAt(modelRow, COL_PRODUCT_NAME);
			StockInfo stockInfo = (StockInfo) tableModel.getValueAt(modelRow, COL_STOCK_LEVEL);
			BigDecimal unitPrice = (BigDecimal) tableModel.getValueAt(modelRow, COL_UNIT_PRICE);
			int itemStockId = (Integer) tableModel.getValueAt(modelRow, HIDDEN_COL_ITEM_STOCK_ID);
			LOGGER.info("Opening Edit Dialog for Item Stock ID: " + itemStockId);
			Window owner = SwingUtilities.getWindowAncestor(view);
			EditItemStockDialog dialog = new EditItemStockDialog(owner, this, itemStockId, productName,
					stockInfo.getMinimumQuantity(), unitPrice, stockInfo.getQuantity());
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"Error retrieving item data for editing: " + e.getMessage(), "Data Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openFilterDialog(ActionEvent ev) {
		Window owner = SwingUtilities.getWindowAncestor(view);
		InventoryFilterDialog dialog = new InventoryFilterDialog(owner, this, activeCategoryFilter,
				activeSupplierFilter);
		dialog.setVisible(true);
	}

	public void openRestockDialog(int modelRow) {
		if (table == null || table == null || modelRow < 0 || modelRow >= table.getRowCount()) {
			LOGGER.severe("RestockItem: Invalid modelRow: " + modelRow);
			return;
		}
		try {
			int itemStockId = (Integer) table.getValueAt(modelRow, HIDDEN_COL_ITEM_STOCK_ID);
			String productName = (String) table.getValueAt(modelRow, COL_PRODUCT_NAME);
			StockInfo stockInfo = (StockInfo) table.getValueAt(modelRow, COL_STOCK_LEVEL);
			int currentQuantity = stockInfo.getQuantity();
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

	private void populatetable() {
		if (table == null || tableModel == null) {
			return;
		}
		tableModel.setRowCount(0);
		String sql = """
				SELECT
				    i.name AS product_name,
				    i._item_id,
				    ic.name AS category_name,
				    p.name AS pack_type_name,
				    s.name AS supplier_name,
				    ist.quantity,
				    ist.minimum_quantity,
				    ist.price_php AS unit_price,
				    ist._item_stock_id
				FROM item_stocks ist
				JOIN items i ON ist._item_id = i._item_id
				LEFT JOIN packagings p ON ist._packaging_id = p._packaging_id
				LEFT JOIN item_categories_items ici ON i._item_id = ici._item_id
				LEFT JOIN item_categories ic ON ici._item_category_id = ic._item_category_id
				LEFT JOIN suppliers_items si ON i._item_id = si._item_id
				LEFT JOIN suppliers s ON si._supplier_id = s._supplier_id
				WHERE i.is_deleted = FALSE AND ist.is_deleted = FALSE
				ORDER BY i.name
				""";
		try (Connection conn = MySqlFactoryDao.createConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				Vector<Object> row = new Vector<>();
				row.add(rs.getString("product_name"));
				row.add("#" + rs.getInt("_item_id"));
				row.add(rs.getString("category_name") != null ? rs.getString("category_name") : "N/A");
				row.add(rs.getString("pack_type_name") != null ? rs.getString("pack_type_name") : "N/A");
				row.add(rs.getString("supplier_name") != null ? rs.getString("supplier_name") : "N/A");
				row.add(new StockInfo(rs.getInt("_item_id"), rs.getInt("quantity"), rs.getInt("minimum_quantity")));
				row.add(rs.getBigDecimal("unit_price"));
				row.add("...");
				row.add(rs.getInt("_item_stock_id"));
				tableModel.addRow(row);
			}
			LOGGER.info(getSceneName() + ": Table populated with " + tableModel.getRowCount()
					+ " rows (excluding archived).");
		} catch (SQLException e) {
			LOGGER.severe("SQL Error fetching inventory data: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
					"An error occurred while fetching inventory data:\n" + e.getMessage(), "Database Query Error",
					JOptionPane.ERROR_MESSAGE);
			tableModel.setRowCount(0);
			Vector<Object> errorRow = new Vector<>();
			for (int i = 0; i < tableModel.getColumnCount(); i++) {
				errorRow.add((i == 0) ? "Error Loading Data" : (i == HIDDEN_COL_ITEM_STOCK_ID ? -1 : ""));
			}
			tableModel.addRow(errorRow);
		}
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
							"Restock failed: Item stock record not found, not updated, or already deleted. Stock"
									+ " ID: " + itemStockId);
				}
			}
			String insertRestockSql = "INSERT INTO item_restocks (_item_stock_id, quantity_before, quantity_after,"
					+ " quantity_added) VALUES (?, ?, ?, ?)";
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
		LOGGER.info(getSceneName() + ": Refreshing table data...");
		if (table != null && tableModel != null) { // Added null check for model
			populatetable();
			applyTableFilters();
		} else {
			LOGGER.severe("Inventory table or model is null, cannot refresh.");
		}
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

	private void setupTableRenderersAndEditors() {
		if (table == null) {
			return;
		}

		TableColumnModel columnModel = table.getColumnModel();

		columnModel.getColumn(COL_PRODUCT_NAME).setPreferredWidth(200);
		columnModel.getColumn(COL_ITEM_ID).setPreferredWidth(80);
		columnModel.getColumn(COL_CATEGORY).setPreferredWidth(120);
		columnModel.getColumn(COL_PACK_TYPE).setPreferredWidth(100);
		columnModel.getColumn(COL_SUPPLIER).setPreferredWidth(120);
		columnModel.getColumn(COL_STOCK_LEVEL).setPreferredWidth(180);
		columnModel.getColumn(COL_UNIT_PRICE).setPreferredWidth(100);
		columnModel.getColumn(COL_ACTION).setPreferredWidth(50);
		columnModel.getColumn(COL_ACTION).setMinWidth(50);
		columnModel.getColumn(COL_ACTION).setMaxWidth(50);

		columnModel.getColumn(COL_PRODUCT_NAME).setCellRenderer(new ProductNameRenderer());
		columnModel.getColumn(COL_ITEM_ID).setCellRenderer(new ItemIdRenderer());
		columnModel.getColumn(COL_STOCK_LEVEL).setCellRenderer(new StockLevelRenderer());
		columnModel.getColumn(COL_UNIT_PRICE).setCellRenderer(new CurrencyRenderer());
		TableCellRenderer buttonRenderer = new ButtonRenderer();
		columnModel.getColumn(COL_ACTION).setCellRenderer(buttonRenderer);
		columnModel.getColumn(COL_ACTION).setCellEditor(new ButtonEditor(this, buttonRenderer));

		hideColumn(HIDDEN_COL_ITEM_STOCK_ID);
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
