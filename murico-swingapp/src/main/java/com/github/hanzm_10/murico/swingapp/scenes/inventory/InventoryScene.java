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
package com.github.hanzm_10.murico.swingapp.scenes.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.navigation.Scene;

// Import the Scene interface (adjust package if needed)
// Assuming Scene, SceneManager etc. are in the same package or imported correctly
// import com.github.hanzm_10.murico.swingapp.scenes.Scene;

// Import DatabaseManager (adjust package if needed)
// Assuming DatabaseManager is accessible, e.g., in the same project/module
// import com.yourpackage.DatabaseManager;

public class InventoryScene extends JPanel implements Scene {

	private JTable inventoryTable;
	private DefaultTableModel inventoryTableModel;
	private TableRowSorter<DefaultTableModel> sorter;
	private JComboBox<String> categoryFilterComboBox;
	private JButton editItemButton;
	private JTextField searchField;

	// Flag to prevent multiple UI initializations
	private boolean uiInitialized = false;

	// Store current Branch ID (TODO: Make this dynamic, perhaps passed via params
	// or fetched)
	private int currentBranchId = 1;

	/** Default constructor for Scene implementation. */
	public InventoryScene() {
		setLayout(new BorderLayout(0, 0));
		System.out.println("InventoryScene instance created.");
	}

	// --- Scene Interface Implementation ---

	@Override
	public String getName() {
		return "inventory"; // Unique scene name/path
	}

	@Override
	public JPanel getView() {
		return this; // This panel is the view
	}

	@Override
	public boolean onCreate() {
		System.out.println(getName() + ": onCreate");
		// Initialize UI components only once
		if (!uiInitialized) {
			initializeInventoryUI();
			uiInitialized = true;
		}

		return true;
	}

	@Override
	public void onShow() {
		System.out.println(getName() + ": onShow");
		// Ensure UI is built before loading data
		if (!uiInitialized) {
			onCreate(); // Call onCreate if not already initialized
		}
		// Load/refresh data when shown
		populateCategoryFilter(); // Refresh filter options
		populateInventoryTable(inventoryTable, currentBranchId, "All Categories"); // Load initial data

		// Reset state (optional)
		if (inventoryTable != null)
			inventoryTable.clearSelection();
		if (editItemButton != null)
			editItemButton.setEnabled(false);
		if (searchField != null)
			searchField.setText("");
		if (categoryFilterComboBox != null)
			categoryFilterComboBox.setSelectedItem("All Categories");
		if (sorter != null)
			sorter.setRowFilter(null); // Clear any existing row filter
	}

	@Override
	public void onHide() {
		System.out.println(getName() + ": onHide");
		// Pause activities if needed
	}

	@Override
	public boolean onDestroy() {
		// Cleanup components and listeners
		inventoryTable = null;
		inventoryTableModel = null;
		sorter = null;
		categoryFilterComboBox = null;
		editItemButton = null;
		searchField = null;
		removeAll(); // Remove all child components from this panel
		uiInitialized = false; // Reset flag

		return true; // Indicate successful destruction
	}

	// --- End Scene Interface Implementation ---

	private void initializeInventoryUI() {
		System.out.println(getName() + ": Initializing UI components...");

		// --- Top Bar Panel (Title + Actions) ---
		JPanel topBarPanel = new JPanel(new BorderLayout(0, 0));
		this.add(topBarPanel, BorderLayout.NORTH);

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lblInventoryTitle = new JLabel("INVENTORY");
		lblInventoryTitle.setFont(new Font("Montserrat ExtraBold", Font.BOLD, 30));
		titlePanel.add(lblInventoryTitle);
		titlePanel.setBorder(new EmptyBorder(5, 10, 5, 10));
		topBarPanel.add(titlePanel, BorderLayout.NORTH);

		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		actionPanel.setBackground(new Color(240, 240, 240));
		topBarPanel.add(actionPanel, BorderLayout.CENTER);

		// --- Action Buttons ---
		JButton btnAddItem = new JButton("Add Item (+)");
		btnAddItem.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
		btnAddItem.addActionListener(e -> openAddItemDialog());
		actionPanel.add(btnAddItem);

		editItemButton = new JButton("Edit Item");
		editItemButton.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
		editItemButton.setEnabled(false); // Initially disabled
		editItemButton.addActionListener(e -> openEditItemDialog());
		actionPanel.add(editItemButton);

		// --- Category Filter ---
		JLabel filterLabel = new JLabel("Filter by Category:");
		filterLabel.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
		actionPanel.add(filterLabel);

		categoryFilterComboBox = new JComboBox<>();
		categoryFilterComboBox.setFont(new Font("Montserrat SemiBold", Font.BOLD, 12));
		categoryFilterComboBox.setPreferredSize(new Dimension(180, 25));
		categoryFilterComboBox.addItem("Loading Categories...");
		categoryFilterComboBox.addActionListener(e -> {
			String selectedCategory = (String) categoryFilterComboBox.getSelectedItem();
			if (uiInitialized && selectedCategory != null && !selectedCategory.contains("Loading")
					&& !selectedCategory.contains("Error")) {
				populateInventoryTable(inventoryTable, currentBranchId, selectedCategory);
			}
		});
		actionPanel.add(categoryFilterComboBox);

		// --- Search ---
		actionPanel.add(Box.createHorizontalGlue()); // Spacer
		JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
		searchPanel.setOpaque(false);
		searchField = new JTextField(20);
		searchField.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				performSearch();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				performSearch();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				performSearch();
			}
		});
		// Using a JButton for the icon now
		JButton searchButton = new JButton(); // Icon button logic moved here from previous example
		try {
			URL searchIconUrl = getClass().getResource("/icons/search_icon.png"); // Ensure path is correct
			if (searchIconUrl != null)
				searchButton.setIcon(new ImageIcon(searchIconUrl));
			else
				searchButton.setText("\uD83D\uDD0D");
		} catch (Exception e) {
			searchButton.setText("\uD83D\uDD0D");
		}
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.setFocusPainted(false);
		searchButton.setOpaque(false);
		searchButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		searchButton.setToolTipText("Search Inventory");
		searchButton.addActionListener(e -> performSearch());
		searchPanel.add(searchField, BorderLayout.CENTER);
		searchPanel.add(searchButton, BorderLayout.EAST);
		actionPanel.add(searchPanel);

		// --- Inventory Table ---
		JScrollPane scrollPane = new JScrollPane();
		this.add(scrollPane, BorderLayout.CENTER);

		inventoryTable = new JTable();
		inventoryTable.setCellSelectionEnabled(false);
		inventoryTable.setRowSelectionAllowed(true);
		inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inventoryTable.setFont(new Font("Montserrat SemiBold", Font.BOLD, 12));
		inventoryTable.setRowHeight(25);
		inventoryTable.getTableHeader().setFont(new Font("Montserrat Bold", Font.BOLD, 12));
		inventoryTable.getTableHeader().setReorderingAllowed(false);
		inventoryTable.setFillsViewportHeight(true);

		// Listener for enabling/disabling Edit button
		inventoryTable.getSelectionModel().addListSelectionListener(e -> {
			if (editItemButton != null && !e.getValueIsAdjusting()) {
				editItemButton.setEnabled(inventoryTable.getSelectedRow() != -1);
			}
		});

		// Set initial model (data loaded in onShow)
		String[] initialColumns = {"Status"};
		Object[][] initialData = {{"Loading data..."}};
		inventoryTableModel = new DefaultTableModel(initialData, initialColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		inventoryTable.setModel(inventoryTableModel);
		scrollPane.setViewportView(inventoryTable);
	}

	// --- Core Logic Methods ---

	public void refreshTable() {
		System.out.println(getName() + ": Refreshing table...");
		String selectedCategory = "All Categories";
		if (categoryFilterComboBox != null && categoryFilterComboBox.getSelectedItem() != null) {
			selectedCategory = (String) categoryFilterComboBox.getSelectedItem();
		}
		if (inventoryTable != null) {
			populateInventoryTable(inventoryTable, currentBranchId, selectedCategory);
		} else {
			System.err.println("Inventory table is null, cannot refresh.");
		}
	}

	private void populateCategoryFilter() {
		if (categoryFilterComboBox == null)
			return;
		List<String> categories = new ArrayList<>();
		categories.add("All Categories");
		String sql = "SELECT DISTINCT item_category FROM items ORDER BY item_category";
		try (Connection conn = MySqlFactoryDao.createConnection(); // Assuming DatabaseManager is accessible
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				String category = rs.getString("item_category");
				if (category != null && !category.trim().isEmpty()) {
					categories.add(category);
				}
			}
			SwingUtilities.invokeLater(() -> {
				categoryFilterComboBox.setModel(new DefaultComboBoxModel<>(categories.toArray(new String[0])));
			});
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE); // Use 'this'
			SwingUtilities.invokeLater(() -> {
				categoryFilterComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Error Loading"}));
			});
		}
	}

	private void performSearch() {
		if (searchField == null || sorter == null)
			return;
		String searchText = searchField.getText();
		if (searchText.trim().isEmpty()) {
			sorter.setRowFilter(null);
		} else {
			try {
				sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
			} catch (PatternSyntaxException e) {
				sorter.setRowFilter(null);
			}
		}
	}

	private void openAddItemDialog() {
		Window owner = SwingUtilities.getWindowAncestor(this);
		AddItemDialog dialog = new AddItemDialog(owner, this);
		dialog.setVisible(true);
	}

	private void openEditItemDialog() {
		if (inventoryTable == null)
			return;
		int selectedViewRow = inventoryTable.getSelectedRow();
		if (selectedViewRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select an item to edit.", "No Selection",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int modelRow = inventoryTable.convertRowIndexToModel(selectedViewRow);
		TableModel model = inventoryTable.getModel();
		try {
			int itemId = (Integer) model.getValueAt(modelRow, 0);
			String name = (String) model.getValueAt(modelRow, 2);
			String category = (String) model.getValueAt(modelRow, 3);
			String packType = (String) model.getValueAt(modelRow, 4);
			int currentStock = (Integer) model.getValueAt(modelRow, 5);
			BigDecimal srp = (BigDecimal) model.getValueAt(modelRow, 6);
			BigDecimal wsp = (BigDecimal) model.getValueAt(modelRow, 7);
			int minQty = fetchMinimumQuantityForItem(itemId); // Fetch min quantity separately

			Window owner = SwingUtilities.getWindowAncestor(this);
			EditItemDialog dialog = new EditItemDialog(owner, this, itemId, name, packType, category, minQty, srp, wsp,
					currentStock);
			dialog.setVisible(true);
		} catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error retrieving item data from table.\n" + e.getMessage(),
					"Data Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private int fetchMinimumQuantityForItem(int itemId) {
		String sql = "SELECT item_minimum_quantity FROM items WHERE _item_id = ?";
		int minQty = 0;
		try (Connection conn = MySqlFactoryDao.createConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) { // Corrected: pass sql variable
			pstmt.setInt(1, itemId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					minQty = rs.getInt("item_minimum_quantity");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return minQty;
	}

	public void populateInventoryTable(JTable table, int branchId, String categoryFilter) {
		if (table == null) {
			System.err.println("Cannot populate null table.");
			return;
		}

		final String[] columnNames = {"ID", "Created At", "Name", "Category", "Pack Type", "Stock Qty", "SRP", "WSP"};
		// Create new model instance EACH time data is loaded
		DefaultTableModel currentModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
					case 0 :
						return Integer.class;
					case 1 :
						return Timestamp.class;
					case 5 :
						return Integer.class;
					case 6 :
						return BigDecimal.class;
					case 7 :
						return BigDecimal.class;
					default :
						return String.class;
				}
			}
		};
		this.inventoryTableModel = currentModel; // Update the class field reference

		StringBuilder sqlBuilder = new StringBuilder(
				/* SQL base */
				"SELECT i._item_id, i._item_created_at, i.item_name, i.item_category, i.item_pack_type,"
						+ " COALESCE(ib.item_quantity, 0) AS item_stock_quantity, i.item_srp, i.item_wsp"
						+ " FROM items i LEFT JOIN items_in_branch ib ON i._item_id = ib._item_id AND"
						+ " ib._company_branch_id = ? ");
		boolean hasCategoryFilter = categoryFilter != null && !categoryFilter.equals("All Categories");
		if (hasCategoryFilter) {
			sqlBuilder.append("WHERE i.item_category = ? ");
		}
		sqlBuilder.append("ORDER BY i.item_name");
		String sql = sqlBuilder.toString();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = MySqlFactoryDao.createConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql); // Corrected: pass sql variable
				pstmt.setInt(1, branchId);
				if (hasCategoryFilter) {
					pstmt.setString(2, categoryFilter);
				}
				rs = pstmt.executeQuery();
				while (rs.next()) {
					Vector<Object> row = new Vector<>();
					row.add(rs.getInt("_item_id"));
					row.add(rs.getTimestamp("_item_created_at"));
					row.add(rs.getString("item_name"));
					row.add(rs.getString("item_category"));
					row.add(rs.getString("item_pack_type"));
					row.add(rs.getInt("item_stock_quantity"));
					row.add(rs.getBigDecimal("item_srp"));
					row.add(rs.getBigDecimal("item_wsp"));
					currentModel.addRow(row); // Add to the new model instance
				}

				table.setModel(currentModel); // Set the NEW model on the table
				sorter = new TableRowSorter<>(currentModel); // Create sorter with the NEW model
				table.setRowSorter(sorter);

				// Adjust Column Alignment
				if (table.getColumnCount() > 0) {
					DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
					leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
					table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
					DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
					rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
					if (table.getColumnCount() > 7) {
						table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
						table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
						table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
					}
				}
				performSearch(); // Apply search filter
				System.out.println(getName() + ": Table populated.");
			} else {
				handleDbConnectionError(table, columnNames);
			}
		} catch (SQLException e) {
			handleDbQueryError(table, e, columnNames);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} ;
		}
	}

	private void handleDbConnectionError(JTable table, String[] columnNames) {
		System.err.println("Failed to get database connection.");
		JOptionPane.showMessageDialog(this, "Could not connect to the database.\nPlease check settings.",
				"Database Connection Error", JOptionPane.ERROR_MESSAGE);
		DefaultTableModel errorModel = new DefaultTableModel(new Object[][]{{"Error: Connection Failed"}}, columnNames);
		if (table != null)
			table.setModel(errorModel);
		if (table != null)
			table.setRowSorter(null);
		sorter = null;
	}

	private void handleDbQueryError(JTable table, SQLException e, String[] columnNames) {
		System.err.println("SQL Error fetching inventory data: " + e.getMessage());
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, "An error occurred while fetching inventory data:\n" + e.getMessage(),
				"Database Query Error", JOptionPane.ERROR_MESSAGE);
		DefaultTableModel errorModel = new DefaultTableModel(new Object[][]{{"Error: Query Failed"}}, columnNames);
		if (table != null)
			table.setModel(errorModel);
		if (table != null)
			table.setRowSorter(null);
		sorter = null;
	}

	// --- Database Modification Methods ---
	// Corrected signatures and prepareStatement calls
	public boolean addNewItemToDatabase(String name, String packType, String category, int minQty, BigDecimal srp,
			BigDecimal wsp) {
		String sql = "INSERT INTO items (item_name, item_pack_type, item_category, item_minimum_quantity,"
				+ " item_srp, item_wsp) VALUES (?, ?, ?, ?, ?, ?)";
		boolean success = false;
		try (Connection conn = MySqlFactoryDao.createConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) { // Corrected
			pstmt.setString(1, name);
			pstmt.setString(2, packType);
			pstmt.setString(3, category);
			pstmt.setInt(4, minQty);
			pstmt.setBigDecimal(5, srp);
			pstmt.setBigDecimal(6, wsp);
			success = (pstmt.executeUpdate() == 1);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return success;
	}

	public boolean updateItemInDatabase(int itemId, String name, String packType, String category, int minQty,
			BigDecimal srp, BigDecimal wsp) {
		String sql = "UPDATE items SET item_name = ?, item_pack_type = ?, item_category = ?, "
				+ "item_minimum_quantity = ?, item_srp = ?, item_wsp = ? WHERE _item_id = ?";
		boolean success = false;
		try (Connection conn = MySqlFactoryDao.createConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) { // Corrected
			pstmt.setString(1, name);
			pstmt.setString(2, packType);
			pstmt.setString(3, category);
			pstmt.setInt(4, minQty);
			pstmt.setBigDecimal(5, srp);
			pstmt.setBigDecimal(6, wsp);
			pstmt.setInt(7, itemId);
			success = (pstmt.executeUpdate() == 1);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error updating item: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return success;
	}

	public boolean deleteItemFromDatabase(int itemId) {
		String deleteItemSql = "DELETE FROM items WHERE _item_id = ?";
		boolean success = false;
		try (Connection conn = MySqlFactoryDao.createConnection();
				PreparedStatement pstmtItem = conn.prepareStatement(deleteItemSql)) { // Corrected
			pstmtItem.setInt(1, itemId);
			success = (pstmtItem.executeUpdate() == 1);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"Error deleting item: " + e.getMessage() + "\n(Related records might prevent deletion)",
					"Database Error", JOptionPane.ERROR_MESSAGE);
			success = false;
		}
		return success;
	}

	// --- Inner Classes for Dialogs ---

	class AddItemDialog extends JDialog {
		private JTextField nameField;
		private JTextField packTypeField;
		private JComboBox<String> categoryComboBox;
		private JSpinner minQtySpinner;
		private JSpinner srpSpinner;
		private JSpinner wspSpinner;
		private InventoryScene parentPanel;

		public AddItemDialog(Window owner, InventoryScene parent) {
			super(owner, "Add New Inventory Item", Dialog.ModalityType.APPLICATION_MODAL);
			this.parentPanel = parent;
			// --- UI Setup ---
			setSize(450, 350);
			setLocationRelativeTo(owner);
			setLayout(new BorderLayout(10, 10));
			((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
			JPanel inputPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			// Fields...
			gbc.gridx = 0;
			gbc.gridy = 0;
			inputPanel.add(new JLabel("Item Name:"), gbc);
			gbc.gridx = 1;
			gbc.weightx = 1.0;
			nameField = new JTextField(20);
			inputPanel.add(nameField, gbc);
			gbc.weightx = 0.0;
			gbc.gridx = 0;
			gbc.gridy = 1;
			inputPanel.add(new JLabel("Pack Type:"), gbc);
			gbc.gridx = 1;
			packTypeField = new JTextField(20);
			inputPanel.add(packTypeField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 2;
			inputPanel.add(new JLabel("Category:"), gbc);
			gbc.gridx = 1;
			categoryComboBox = new JComboBox<>();
			/* Populate */ ComboBoxModel<String> fm = parentPanel.categoryFilterComboBox.getModel();
			List<String> ic = new ArrayList<>();
			for (int i = 0; i < fm.getSize(); i++) {
				String c = fm.getElementAt(i);
				if (c != null && !c.equals("All Categories") && !c.contains("Load") && !c.contains("Err"))
					ic.add(c);
			}
			if (!ic.contains("to_be_assigned"))
				ic.add("to_be_assigned");
			categoryComboBox.setModel(new DefaultComboBoxModel<>(ic.toArray(new String[0])));
			if (!ic.isEmpty())
				categoryComboBox.setSelectedIndex(0);
			inputPanel.add(categoryComboBox, gbc);
			gbc.gridx = 0;
			gbc.gridy = 3;
			inputPanel.add(new JLabel("Min Qty:"), gbc);
			gbc.gridx = 1;
			minQtySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
			inputPanel.add(minQtySpinner, gbc);
			gbc.gridx = 0;
			gbc.gridy = 4;
			inputPanel.add(new JLabel("SRP ($):"), gbc);
			gbc.gridx = 1;
			srpSpinner = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 10000.00, 0.01));
			srpSpinner.setEditor(new JSpinner.NumberEditor(srpSpinner, "0.00"));
			inputPanel.add(srpSpinner, gbc);
			gbc.gridx = 0;
			gbc.gridy = 5;
			inputPanel.add(new JLabel("WSP ($):"), gbc);
			gbc.gridx = 1;
			wspSpinner = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 10000.00, 0.01));
			wspSpinner.setEditor(new JSpinner.NumberEditor(wspSpinner, "0.00"));
			inputPanel.add(wspSpinner, gbc);
			// Buttons...
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JButton saveButton = new JButton("Save Item");
			JButton cancelButton = new JButton("Cancel");
			saveButton.addActionListener(e -> saveItem());
			cancelButton.addActionListener(e -> dispose());
			buttonPanel.add(saveButton);
			buttonPanel.add(cancelButton);
			add(inputPanel, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);
		}

		private void saveItem() {
			// Validation...
			String name = nameField.getText().trim();
			String packType = packTypeField.getText().trim();
			Object selectedCategoryObj = categoryComboBox.getSelectedItem();
			if (name.isEmpty() || packType.isEmpty() || selectedCategoryObj == null) {
				JOptionPane.showMessageDialog(this, "Name, Pack Type, and Category are required.", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			String category = selectedCategoryObj.toString();
			int minQty = (Integer) minQtySpinner.getValue();
			BigDecimal srp, wsp;
			try {
				srp = BigDecimal.valueOf(((Number) srpSpinner.getValue()).doubleValue());
				wsp = BigDecimal.valueOf(((Number) wspSpinner.getValue()).doubleValue());
			} catch (ClassCastException cce) {
				JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Call parent's DB method
			boolean success = parentPanel.addNewItemToDatabase(name, packType, category, minQty, srp, wsp);
			if (success) {
				JOptionPane.showMessageDialog(this, "Item added.", "Success", JOptionPane.INFORMATION_MESSAGE);
				parentPanel.refreshTable();
				dispose();
			}
		}
	}

	class EditItemDialog extends JDialog {
		private JTextField nameField;
		private JTextField packTypeField;
		private JComboBox<String> categoryComboBox;
		private JSpinner minQtySpinner;
		private JSpinner srpSpinner;
		private JSpinner wspSpinner;
		private JLabel currentStockLabel;
		private InventoryScene parentPanel;
		private int editingItemId;

		public EditItemDialog(Window owner, InventoryScene parent, int itemId, String name, String packType,
				String category, int minQty, BigDecimal srp, BigDecimal wsp, int currentStock) {
			super(owner, "Edit Item (ID: " + itemId + ")", Dialog.ModalityType.APPLICATION_MODAL);
			this.parentPanel = parent;
			this.editingItemId = itemId;
			// --- UI Setup ---
			setSize(450, 400);
			setLocationRelativeTo(owner);
			setLayout(new BorderLayout(10, 10));
			((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
			JPanel inputPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			// Fields (pre-filled)...
			gbc.gridx = 0;
			gbc.gridy = 0;
			inputPanel.add(new JLabel("Item Name:"), gbc);
			gbc.gridx = 1;
			gbc.weightx = 1.0;
			nameField = new JTextField(20);
			nameField.setText(name);
			inputPanel.add(nameField, gbc);
			gbc.weightx = 0.0;
			gbc.gridx = 0;
			gbc.gridy = 1;
			inputPanel.add(new JLabel("Pack Type:"), gbc);
			gbc.gridx = 1;
			packTypeField = new JTextField(20);
			packTypeField.setText(packType);
			inputPanel.add(packTypeField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 2;
			inputPanel.add(new JLabel("Category:"), gbc);
			gbc.gridx = 1;
			categoryComboBox = new JComboBox<>();
			/* Populate */ ComboBoxModel<String> fm = parentPanel.categoryFilterComboBox.getModel();
			List<String> ic = new ArrayList<>();
			for (int i = 0; i < fm.getSize(); i++) {
				String c = fm.getElementAt(i);
				if (c != null && !c.equals("All Categories") && !c.contains("Load") && !c.contains("Err"))
					ic.add(c);
			}
			if (!ic.contains("to_be_assigned"))
				ic.add("to_be_assigned");
			categoryComboBox.setModel(new DefaultComboBoxModel<>(ic.toArray(new String[0])));
			categoryComboBox.setSelectedItem(category);
			inputPanel.add(categoryComboBox, gbc);
			gbc.gridx = 0;
			gbc.gridy = 3;
			inputPanel.add(new JLabel("Min Qty:"), gbc);
			gbc.gridx = 1;
			minQtySpinner = new JSpinner(new SpinnerNumberModel(minQty, 0, 10000, 1));
			inputPanel.add(minQtySpinner, gbc);
			gbc.gridx = 0;
			gbc.gridy = 4;
			inputPanel.add(new JLabel("SRP ($):"), gbc);
			gbc.gridx = 1;
			srpSpinner = new JSpinner(new SpinnerNumberModel(srp.doubleValue(), 0.00, 10000.00, 0.01));
			srpSpinner.setEditor(new JSpinner.NumberEditor(srpSpinner, "0.00"));
			inputPanel.add(srpSpinner, gbc);
			gbc.gridx = 0;
			gbc.gridy = 5;
			inputPanel.add(new JLabel("WSP ($):"), gbc);
			gbc.gridx = 1;
			wspSpinner = new JSpinner(new SpinnerNumberModel(wsp.doubleValue(), 0.00, 10000.00, 0.01));
			wspSpinner.setEditor(new JSpinner.NumberEditor(wspSpinner, "0.00"));
			inputPanel.add(wspSpinner, gbc);
			gbc.gridx = 0;
			gbc.gridy = 6;
			inputPanel.add(new JLabel("Current Stock:"), gbc);
			gbc.gridx = 1;
			currentStockLabel = new JLabel(currentStock + " (Read-Only)");
			currentStockLabel.setFont(currentStockLabel.getFont().deriveFont(Font.ITALIC));
			inputPanel.add(currentStockLabel, gbc);
			// Buttons...
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JButton saveButton = new JButton("Save Changes");
			JButton cancelButton = new JButton("Cancel");
			saveButton.addActionListener(e -> saveChanges());
			cancelButton.addActionListener(e -> dispose());
			buttonPanel.add(saveButton);
			buttonPanel.add(cancelButton);
			JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JButton deleteButton = new JButton("Delete Item");
			deleteButton.setForeground(Color.WHITE);
			deleteButton.setBackground(Color.RED.darker());
			deleteButton.addActionListener(e -> deleteItem());
			leftButtonPanel.add(deleteButton);
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.add(leftButtonPanel, BorderLayout.WEST);
			bottomPanel.add(buttonPanel, BorderLayout.EAST);
			add(inputPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
		}

		private void saveChanges() {
			// Validation...
			String name = nameField.getText().trim();
			String packType = packTypeField.getText().trim();
			Object selectedCategoryObj = categoryComboBox.getSelectedItem();
			if (name.isEmpty() || packType.isEmpty() || selectedCategoryObj == null) {
				JOptionPane.showMessageDialog(this, "Name, Pack Type, and Category are required.", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			String category = selectedCategoryObj.toString();
			int minQty = (Integer) minQtySpinner.getValue();
			BigDecimal srp, wsp;
			try {
				srp = BigDecimal.valueOf(((Number) srpSpinner.getValue()).doubleValue());
				wsp = BigDecimal.valueOf(((Number) wspSpinner.getValue()).doubleValue());
			} catch (ClassCastException cce) {
				JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Call parent's DB method
			boolean success = parentPanel.updateItemInDatabase(editingItemId, name, packType, category, minQty, srp,
					wsp);
			if (success) {
				JOptionPane.showMessageDialog(this, "Item updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
				parentPanel.refreshTable();
				dispose();
			}
		}

		private void deleteItem() {
			int confirm = JOptionPane.showConfirmDialog(this,
					"Delete item '" + nameField.getText() + "' (ID: " + editingItemId + ")?\nThis cannot be undone.",
					"Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION) {
				boolean success = parentPanel.deleteItemFromDatabase(editingItemId);
				if (success) {
					JOptionPane.showMessageDialog(this, "Item deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
					parentPanel.refreshTable();
					dispose();
				}
			}
		}
	} // --- End Inner Classes ---

	@Override
	public boolean onBeforeHide() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBeforeShow() {
		// TODO Auto-generated method stub
		return false;
	}
} // --- End InventoryScene Class ---
