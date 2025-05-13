package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs;

// import javax.swing.border.TitledBorder; // Not using titled border for main form
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
// AssetManager might not be needed here if no icons are directly used in this dialog's new design
import com.github.hanzm_10.murico.swingapp.scenes.home.InventoryScene;

public class AddItemDialog extends JDialog {

	private static class ComboBoxItem {
		// ... (same as before) ...
		private final int id;
		private final String name;

		public ComboBoxItem(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private final InventoryScene parentScene;
	// --- UI Components based on YOUR provided image of the old dialog ---
	private JTextField itemNameField;
	private JTextArea itemDescriptionArea;
	private JComboBox<ComboBoxItem> categoryComboBox;
	private JComboBox<ComboBoxItem> packagingComboBox;
	private JSpinner initialQuantitySpinner;
	private JSpinner minQuantitySpinner;
	private JSpinner sellingPriceSpinner; // item_stocks.price_php
	private JSpinner srpSpinner; // item_stocks.srp_php
	private JComboBox<ComboBoxItem> supplierComboBox; // For selecting existing supplier

	private JSpinner costPriceSpinner; // suppliers_items.wsp_php (enabled if supplier selected)
	// Maps for ComboBox IDs
	private Map<String, Integer> categoryMap = new HashMap<>();
	private Map<String, Integer> packagingMap = new HashMap<>();

	private Map<String, Integer> supplierMap = new HashMap<>(); // For the supplier JComboBox

	public AddItemDialog(Window owner, InventoryScene parent) {
		super(owner, "Add New Item", Dialog.ModalityType.APPLICATION_MODAL); // Generic Title
		this.parentScene = parent;

		setSize(450, 680); // Adjusted height for all fields
		setLocationRelativeTo(owner);
		setLayout(new BorderLayout(0, 0));

		Container contentPane = getContentPane();
		((JPanel) contentPane).setBorder(new EmptyBorder(20, 25, 20, 25)); // Outer padding
		contentPane.setBackground(Color.WHITE); // Background to match modern mockup

		// --- Header Panel (like the modern mockup) ---
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setOpaque(false);
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel titleLabel = new JLabel("Add New Item"); // Title
		titleLabel.setFont(new Font("Montserrat", Font.BOLD, 26)); // Font from modern mockup
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(titleLabel);

		JLabel subtitleLabel = new JLabel("Input all required information for the new item."); // Subtitle
		subtitleLabel.setFont(new Font("Montserrat", Font.PLAIN, 12));
		subtitleLabel.setForeground(Color.GRAY);
		subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		subtitleLabel.setBorder(new EmptyBorder(5, 0, 15, 0)); // Space below subtitle
		headerPanel.add(subtitleLabel);
		add(headerPanel, BorderLayout.NORTH);

		// --- Form Panel (using GridBagLayout for vertical stacking) ---
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setOpaque(false); // Transparent background
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 2, 0); // Small bottom padding for labels
		gbc.weightx = 1.0;

		// Item Name*
		formPanel.add(createFormLabel("Item Name*"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		itemNameField = createStyledTextField();
		formPanel.add(itemNameField, gbc);

		// Description
		formPanel.add(createFormLabel("Description"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		itemDescriptionArea = new JTextArea(3, 20);
		itemDescriptionArea.setFont(new Font("Montserrat", Font.PLAIN, 14));
		itemDescriptionArea.setLineWrap(true);
		itemDescriptionArea.setWrapStyleWord(true);
		JScrollPane descScrollPane = new JScrollPane(itemDescriptionArea);
		descScrollPane.setPreferredSize(new Dimension(100, 60)); // Explicit preferred size for text area
		descScrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
		formPanel.add(descScrollPane, gbc);

		// Category*
		formPanel.add(createFormLabel("Category*"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		categoryComboBox = createStyledComboBox();
		populateCategories();
		formPanel.add(categoryComboBox, gbc);

		// Packaging*
		formPanel.add(createFormLabel("Packaging*"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		packagingComboBox = createStyledComboBox();
		populatePackagings();
		formPanel.add(packagingComboBox, gbc);

		// Initial Quantity*
		formPanel.add(createFormLabel("Initial Quantity*"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		initialQuantitySpinner = createStyledSpinner(1, 0, 10000, 1, "#,##0");
		formPanel.add(initialQuantitySpinner, gbc);

		// Min. Stock Qty*
		formPanel.add(createFormLabel("Min. Stock Qty*"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		minQuantitySpinner = createStyledSpinner(0, 0, 10000, 1, "#,##0");
		formPanel.add(minQuantitySpinner, gbc);

		// Selling Price (₱)* (item_stocks.price_php)
		formPanel.add(createFormLabel("Selling Price (₱)*"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		sellingPriceSpinner = createStyledSpinner(0.00, 0.00, 100000.00, 0.01, "#,##0.00");
		formPanel.add(sellingPriceSpinner, gbc);

		// SRP (₱)* (item_stocks.srp_php)
		formPanel.add(createFormLabel("SRP (₱)*"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		srpSpinner = createStyledSpinner(0.00, 0.00, 100000.00, 0.01, "#,##0.00");
		formPanel.add(srpSpinner, gbc);

		// Supplier (Optional ComboBox)
		formPanel.add(createFormLabel("Supplier (Optional)"), gbc);
		gbc.insets = new Insets(0, 0, 10, 0);
		supplierComboBox = createStyledComboBox();
		populateSuppliers();
		formPanel.add(supplierComboBox, gbc);

		// Cost Price (₱) (suppliers_items.wsp_php)
		formPanel.add(createFormLabel("Cost Price (₱ from Supplier)"), gbc);
		gbc.insets = new Insets(0, 0, 15, 0); // More space before buttons
		costPriceSpinner = createStyledSpinner(0.00, 0.00, 100000.00, 0.01, "#,##0.00");
		costPriceSpinner.setEnabled(false); // Initially disabled
		supplierComboBox.addActionListener(e -> { // Enable/disable based on supplier selection
			ComboBoxItem selectedSupplier = (ComboBoxItem) supplierComboBox.getSelectedItem();
			costPriceSpinner.setEnabled(selectedSupplier != null && selectedSupplier.getId() != -1);
		});
		formPanel.add(costPriceSpinner, gbc);

		// Use a JScrollPane for the formPanel if it might get too long
		JScrollPane formScrollPane = new JScrollPane(formPanel);
		formScrollPane.setBorder(null); // No border for the scrollpane itself
		formScrollPane.getViewport().setBackground(Color.WHITE); // Match dialog background
		add(formScrollPane, BorderLayout.CENTER);

		// --- Button Panel (Styled like modern mockup) ---
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); // Centered buttons
		buttonPanel.setOpaque(false); // Transparent background
		buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0)); // Space above buttons

		JButton cancelButton = createStyledButton("Cancel", new Color(0xE53935), Color.WHITE); // Reddish
		cancelButton.addActionListener(e -> dispose());

		JButton confirmButton = createStyledButton("Save Item", new Color(0x00695C), Color.WHITE); // Tealish (was
																									// "Confirm
																									// Product")
		confirmButton.addActionListener(e -> saveNewItem());

		buttonPanel.add(cancelButton);
		buttonPanel.add(confirmButton);
		add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	// --- Helper methods for creating styled components (mostly from previous
	// version) ---
	private JLabel createFormLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Montserrat", Font.BOLD, 12)); // Slightly smaller label font
		label.setForeground(new Color(50, 50, 50)); // Darker gray
		return label;
	}

	private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
		JButton button = new JButton(text);
		button.setFont(new Font("Montserrat", Font.BOLD, 14));
		button.setBackground(bgColor);
		button.setForeground(fgColor);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(160, 40));
		// Basic border, true rounded needs custom painting or L&F
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(bgColor.darker(), 1),
				new EmptyBorder(8, 20, 8, 20) // More padding for button text
		));
		return button;
	}

	private JComboBox<ComboBoxItem> createStyledComboBox() {
		JComboBox<ComboBoxItem> comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Montserrat", Font.PLAIN, 13));
		comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 33));
		comboBox.setBackground(Color.WHITE);
		// For truly custom arrow/border like mockup, L&F customization or custom UI
		// delegate needed
		comboBox.setBorder(new LineBorder(new Color(200, 200, 200)));
		return comboBox;
	}

	private JSpinner createStyledSpinner(double initial, double min, double max, double step, String format) {
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(initial, min, max, step));
		spinner.setFont(new Font("Montserrat", Font.PLAIN, 13));
		spinner.setPreferredSize(new Dimension(spinner.getPreferredSize().width, 33));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, format);
		spinner.setEditor(editor);
		// To style spinner buttons like mockup (contained within border) is complex,
		// often requires custom JSpinner UI or a LookAndFeel that supports it.
		// Default spinner buttons will be outside or themed by L&F.
		// Basic border for the text field part:
		Component editorComponent = spinner.getEditor();
		if (editorComponent instanceof JSpinner.DefaultEditor) {
			JTextField textField = ((JSpinner.DefaultEditor) editorComponent).getTextField();
			textField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200)),
					new EmptyBorder(5, 8, 5, 8)));
			textField.setBackground(Color.WHITE); // Ensure text field is white
		} else { // Fallback border for the whole spinner
			spinner.setBorder(new LineBorder(new Color(200, 200, 200)));
		}
		spinner.setBackground(Color.WHITE);
		return spinner;
	}

	private JTextField createStyledTextField() {
		JTextField textField = new JTextField(20);
		textField.setFont(new Font("Montserrat", Font.PLAIN, 13));
		textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 33));
		textField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200)),
				new EmptyBorder(5, 8, 5, 8)));
		return textField;
	}

	private BigDecimal getSpinnerBigDecimal(JSpinner spinner) {
		// ... (same as before) ...
		Object value = spinner.getValue();
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP);
		} else if (value instanceof Number) {
			return BigDecimal.valueOf(((Number) value).doubleValue()).setScale(2, RoundingMode.HALF_UP);
		}
		return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
	}

	private void populateCategories() {
		populateComboBox(categoryComboBox, categoryMap,
				"SELECT _item_category_id, name FROM item_categories ORDER BY name", "_item_category_id", "name",
				"-- Select Category* --");
	}

	// --- Data Population for ComboBoxes ---
	private void populateComboBox(JComboBox<ComboBoxItem> comboBox, Map<String, Integer> map, String sql,
			String idColumn, String nameColumn, String defaultText) {
		map.clear();
		List<ComboBoxItem> items = new ArrayList<>();
		items.add(new ComboBoxItem(-1, defaultText));
		try (Connection conn = MySqlFactoryDao.createConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				int id = rs.getInt(idColumn);
				String name = rs.getString(nameColumn);
				items.add(new ComboBoxItem(id, name));
				map.put(name, id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			items.clear();
			items.add(new ComboBoxItem(-1, "Error Loading " + defaultText.substring(defaultText.lastIndexOf(' ') + 1)));
		}
		comboBox.setModel(new DefaultComboBoxModel<>(items.toArray(new ComboBoxItem[0])));
	}

	private void populatePackagings() {
		populateComboBox(packagingComboBox, packagingMap, "SELECT _packaging_id, name FROM packagings ORDER BY name",
				"_packaging_id", "name", "-- Select Packaging* --");
	}

	private void populateSuppliers() {
		populateComboBox(supplierComboBox, supplierMap, "SELECT _supplier_id, name FROM suppliers ORDER BY name",
				"_supplier_id", "name", "-- Select Supplier (Optional) --");
	}

	// --- Save Logic (Updated to use current fields) ---
	private void saveNewItem() {
		// 1. Get values
		String itemName = itemNameField.getText().trim();
		String itemDescription = itemDescriptionArea.getText().trim();
		ComboBoxItem selectedCategory = (ComboBoxItem) categoryComboBox.getSelectedItem();
		ComboBoxItem selectedPackaging = (ComboBoxItem) packagingComboBox.getSelectedItem();
		ComboBoxItem selectedSupplier = (ComboBoxItem) supplierComboBox.getSelectedItem();
		int initialQty = (Integer) initialQuantitySpinner.getValue();
		int minQty = (Integer) minQuantitySpinner.getValue();
		BigDecimal sellingPrice = getSpinnerBigDecimal(sellingPriceSpinner);
		BigDecimal srp = getSpinnerBigDecimal(srpSpinner);
		BigDecimal costPrice = getSpinnerBigDecimal(costPriceSpinner);

		// 2. Validation
		if (itemName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Item Name is required.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			itemNameField.requestFocusInWindow();
			return;
		}
		if (selectedCategory == null || selectedCategory.getId() == -1) {
			JOptionPane.showMessageDialog(this, "Category is required.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			categoryComboBox.requestFocusInWindow();
			return;
		}
		if (selectedPackaging == null || selectedPackaging.getId() == -1) {
			JOptionPane.showMessageDialog(this, "Packaging is required.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			packagingComboBox.requestFocusInWindow();
			return;
		}
		if (sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
			JOptionPane.showMessageDialog(this, "Selling Price must be greater than zero.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			sellingPriceSpinner.requestFocusInWindow();
			return;
		}
		if (srp.compareTo(BigDecimal.ZERO) < 0) { // SRP can be 0
			JOptionPane.showMessageDialog(this, "SRP cannot be negative.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			srpSpinner.requestFocusInWindow();
			return;
		}
		if (initialQty < 0 || minQty < 0) {
			JOptionPane.showMessageDialog(this, "Quantities cannot be negative.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			(initialQty < 0 ? initialQuantitySpinner : minQuantitySpinner).requestFocusInWindow();
			return;
		}
		if (selectedSupplier != null && selectedSupplier.getId() != -1 && costPrice.compareTo(BigDecimal.ZERO) < 0) {
			JOptionPane.showMessageDialog(this, "If a supplier is selected, Cost Price cannot be negative.",
					"Validation Error", JOptionPane.WARNING_MESSAGE);
			costPriceSpinner.requestFocusInWindow();
			return;
		}
		if ((selectedSupplier == null || selectedSupplier.getId() == -1) && costPrice.compareTo(BigDecimal.ZERO) > 0) {
			JOptionPane.showMessageDialog(this, "Please select a supplier if providing a Cost Price.",
					"Validation Error", JOptionPane.WARNING_MESSAGE);
			supplierComboBox.requestFocusInWindow();
			return;
		}

		// Database Operations
		Connection conn = null;
		boolean success = false;
		int newItemId = -1;

		try {
			conn = MySqlFactoryDao.createConnection();
			conn.setAutoCommit(false);

			// A. Insert into 'items' table
			// Assuming 'items' table DOES NOT have a 'brand' column based on original
			// schema.
			// If it does: ALTER TABLE items ADD COLUMN brand VARCHAR(100);
			String sqlItem = "INSERT INTO items (name, description) VALUES (?, ?)";
			try (PreparedStatement pstmtItem = conn.prepareStatement(sqlItem, Statement.RETURN_GENERATED_KEYS)) {
				pstmtItem.setString(1, itemName);
				pstmtItem.setString(2, itemDescription.isEmpty() ? null : itemDescription);
				pstmtItem.executeUpdate();
				try (ResultSet generatedKeys = pstmtItem.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						newItemId = generatedKeys.getInt(1);
					} else {
						throw new SQLException("Creating item failed, no ID obtained.");
					}
				}
			}

			// B. Insert into 'item_stocks' table
			String sqlStock = "INSERT INTO item_stocks (_item_id, _packaging_id, quantity, minimum_quantity, srp_php, price_php) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmtStock = conn.prepareStatement(sqlStock)) {
				pstmtStock.setInt(1, newItemId);
				pstmtStock.setInt(2, selectedPackaging.getId());
				pstmtStock.setInt(3, initialQty);
				pstmtStock.setInt(4, minQty);
				pstmtStock.setBigDecimal(5, srp);
				pstmtStock.setBigDecimal(6, sellingPrice);
				pstmtStock.executeUpdate();
			}

			// C. Insert into 'item_categories_items'
			String sqlCategoryLink = "INSERT INTO item_categories_items (_item_category_id, _item_id) VALUES (?, ?)";
			try (PreparedStatement pstmtCatLink = conn.prepareStatement(sqlCategoryLink)) {
				pstmtCatLink.setInt(1, selectedCategory.getId());
				pstmtCatLink.setInt(2, newItemId);
				pstmtCatLink.executeUpdate();
			}

			// D. Insert into 'suppliers_items' if supplier was selected
			if (selectedSupplier != null && selectedSupplier.getId() != -1) {
				String sqlSupplierLink = "INSERT INTO suppliers_items (_supplier_id, _item_id, srp_php, wsp_php) VALUES (?, ?, ?, ?)";
				try (PreparedStatement pstmtSupLink = conn.prepareStatement(sqlSupplierLink)) {
					pstmtSupLink.setInt(1, selectedSupplier.getId());
					pstmtSupLink.setInt(2, newItemId);
					pstmtSupLink.setBigDecimal(3, srp); // Using item's SRP for this link's SRP
					pstmtSupLink.setBigDecimal(4, costPrice);
					pstmtSupLink.executeUpdate();
				}
			}

			conn.commit();
			success = true;

		} catch (SQLException e) {
			// ... (rollback and error handling) ...
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException excep) {
					excep.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(this, "Error saving new item: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			// ... (close connection) ...
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		if (success) {
			JOptionPane.showMessageDialog(this, "Item '" + itemName + "' added successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			parentScene.refreshTableData();
			dispose();
		}
	}
}