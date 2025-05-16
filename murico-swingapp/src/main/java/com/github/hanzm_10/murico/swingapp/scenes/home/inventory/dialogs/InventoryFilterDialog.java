package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.dialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.scenes.home.InventorySceneNew;

// Helper class for ComboBox items (can be shared or defined here)
class FilterComboBoxItem {
	private final String value; // Could be category name, supplier name, or a special value like "ALL"
	private final String display;

	public FilterComboBoxItem(String value, String display) {
		this.value = value;
		this.display = display;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return display;
	}
}

public class InventoryFilterDialog extends JDialog {

	private final InventorySceneNew parentScene;
	private JComboBox<FilterComboBoxItem> categoryFilterCombo;
	private JComboBox<FilterComboBoxItem> supplierFilterCombo;
	// Add more JComboBoxes or JSpinners for other filter criteria (e.g., stock
	// level ranges)

	private String currentCategoryFilter = "ALL"; // Store current selections
	private String currentSupplierFilter = "ALL";

	public InventoryFilterDialog(Window owner, InventorySceneNew parent, String initialCategory,
			String initialSupplier) {
		super(owner, "Filter Inventory", Dialog.ModalityType.APPLICATION_MODAL);
		this.parentScene = parent;
		this.currentCategoryFilter = initialCategory != null ? initialCategory : "ALL";
		this.currentSupplierFilter = initialSupplier != null ? initialSupplier : "ALL";

		setSize(350, 250);
		setLocationRelativeTo(owner);
		setLayout(new BorderLayout(10, 10));
		((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

		JPanel filterPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		// Category Filter
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.fill = GridBagConstraints.NONE;
		filterPanel.add(new JLabel("Category:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.8;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		categoryFilterCombo = new JComboBox<>();
		populateCategories();
		selectInitialValue(categoryFilterCombo, currentCategoryFilter);
		filterPanel.add(categoryFilterCombo, gbc);

		// Supplier Filter
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.2;
		gbc.fill = GridBagConstraints.NONE;
		filterPanel.add(new JLabel("Supplier:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.8;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		supplierFilterCombo = new JComboBox<>();
		populateSuppliers();
		selectInitialValue(supplierFilterCombo, currentSupplierFilter);
		filterPanel.add(supplierFilterCombo, gbc);

		// TODO: Add more filters (e.g., Stock Level - Low, Mid, High based on StockInfo
		// logic)

		add(filterPanel, BorderLayout.CENTER);

		// --- Button Panel ---
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton applyButton = new JButton("Apply Filters");
		JButton clearButton = new JButton("Clear Filters");
		JButton cancelButton = new JButton("Cancel");

		applyButton.addActionListener(e -> applyFilters());
		clearButton.addActionListener(e -> clearFiltersAndApply());
		cancelButton.addActionListener(e -> dispose());

		buttonPanel.add(applyButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void applyFilters() {
		FilterComboBoxItem selectedCategoryItem = (FilterComboBoxItem) categoryFilterCombo.getSelectedItem();
		FilterComboBoxItem selectedSupplierItem = (FilterComboBoxItem) supplierFilterCombo.getSelectedItem();

		String category = (selectedCategoryItem != null) ? selectedCategoryItem.getValue() : "ALL";
		String supplier = (selectedSupplierItem != null) ? selectedSupplierItem.getValue() : "ALL";

		// Pass filters back to InventoryScene
		parentScene.setCurrentCategoryFilter(category);
		parentScene.setCurrentSupplierFilter(supplier);
		dispose(); // Close dialog after applying
	}

	private void clearFiltersAndApply() {
		categoryFilterCombo.setSelectedIndex(0); // Select "All Categories"
		supplierFilterCombo.setSelectedIndex(0); // Select "All Suppliers"
		// currentCategoryFilter = "ALL"; // No need to set field here, applyFilters
		// will get from combo
		// currentSupplierFilter = "ALL";
		applyFilters(); // Apply the cleared (ALL) filters
	}

	private void populateCategories() {
		populateComboBox(categoryFilterCombo,
				"SELECT DISTINCT name FROM item_categories WHERE name IS NOT NULL ORDER BY name", "name", "name",
				"All Categories");
	}

	private void populateComboBox(JComboBox<FilterComboBoxItem> comboBox, String sql, String valueColumn,
			String displayColumn, String defaultDisplay) {
		List<FilterComboBoxItem> items = new ArrayList<>();
		items.add(new FilterComboBoxItem("ALL", defaultDisplay)); // "All" option

		try (Connection conn = MySqlFactoryDao.createConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				String value = rs.getString(valueColumn);
				String display = rs.getString(displayColumn);
				if (value != null && !value.trim().isEmpty()) { // Ensure value is not null or empty
					items.add(new FilterComboBoxItem(value, display != null ? display : value));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			items.clear();
			items.add(new FilterComboBoxItem("ERROR", "Error Loading"));
		}
		comboBox.setModel(new DefaultComboBoxModel<>(items.toArray(new FilterComboBoxItem[0])));
	}

	private void populateSuppliers() {
		populateComboBox(supplierFilterCombo,
				"SELECT DISTINCT name FROM suppliers WHERE name IS NOT NULL ORDER BY name", "name", "name",
				"All Suppliers");
	}

	private void selectInitialValue(JComboBox<FilterComboBoxItem> comboBox, String valueToSelect) {
		for (int i = 0; i < comboBox.getItemCount(); i++) {
			if (comboBox.getItemAt(i).getValue().equals(valueToSelect)) {
				comboBox.setSelectedIndex(i);
				return;
			}
		}
		comboBox.setSelectedIndex(0); // Default to "All" if not found
	}
}