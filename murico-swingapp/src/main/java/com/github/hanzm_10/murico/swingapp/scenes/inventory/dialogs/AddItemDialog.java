package com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs;

import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.scenes.inventory.InventoryScene;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemDialog extends JDialog {

    private final InventoryScene parentScene;

    // UI Components
    private JTextField itemNameField;
    private JTextArea itemDescriptionArea;
    private JComboBox<ComboBoxItem> categoryComboBox;
    private JComboBox<ComboBoxItem> packagingComboBox;
    private JComboBox<ComboBoxItem> supplierComboBox; // Optional
    private JSpinner initialQuantitySpinner;
    private JSpinner minQuantitySpinner;
    private JSpinner sellingPriceSpinner; // item_stocks.price_php
    private JSpinner srpSpinner;          // item_stocks.srp_php
    private JSpinner costPriceSpinner;    // suppliers_items.wsp_php (enabled if supplier selected)

    // Store maps for ComboBox IDs
    private Map<String, Integer> categoryMap = new HashMap<>();
    private Map<String, Integer> packagingMap = new HashMap<>();
    private Map<String, Integer> supplierMap = new HashMap<>();


    public AddItemDialog(Window owner, InventoryScene parent) {
        super(owner, "Add New Item", Dialog.ModalityType.APPLICATION_MODAL);
        this.parentScene = parent;

        setSize(500, 600); // Increased height for more fields
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- Title Panel ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Add New Inventory Item");
        titleLabel.setFont(new Font("Montserrat Bold", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Input Panel ---
        JScrollPane scrollableInputPanel = new JScrollPane(createInputPanel());
        scrollableInputPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableInputPanel.setBorder(null); // Remove border from scroll pane itself
        add(scrollableInputPanel, BorderLayout.CENTER);


        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Item");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveNewItem());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Populate ComboBoxes
        populateCategories();
        populatePackagings();
        populateSuppliers();

        // Initial state for cost price
        costPriceSpinner.setEnabled(false);
        supplierComboBox.addActionListener(e -> {
            ComboBoxItem selectedSupplier = (ComboBoxItem) supplierComboBox.getSelectedItem();
            costPriceSpinner.setEnabled(selectedSupplier != null && selectedSupplier.getId() != -1);
        });
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Item Details"),
            new EmptyBorder(10, 10, 10, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        int yPos = 0;

        // Item Name
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Item Name*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        itemNameField = new JTextField(25);
        inputPanel.add(itemNameField, gbc);
        gbc.weightx = 0.0; yPos++;

        // Item Description
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.NORTHWEST;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.5;
        itemDescriptionArea = new JTextArea(3, 25);
        itemDescriptionArea.setLineWrap(true);
        itemDescriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(itemDescriptionArea);
        inputPanel.add(descScrollPane, gbc);
        gbc.weighty = 0.0; yPos++; gbc.anchor = GridBagConstraints.WEST; // Reset

        // Category
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Category*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        categoryComboBox = new JComboBox<>();
        inputPanel.add(categoryComboBox, gbc);
        yPos++;

        // Packaging
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Packaging*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        packagingComboBox = new JComboBox<>();
        inputPanel.add(packagingComboBox, gbc);
        yPos++;

        // Initial Quantity
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Initial Quantity*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        initialQuantitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 100000, 1));
        inputPanel.add(initialQuantitySpinner, gbc);
        yPos++;

        // Minimum Quantity
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Min. Stock Qty*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        minQuantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        inputPanel.add(minQuantitySpinner, gbc);
        yPos++;

        // Selling Price (price_php)
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Selling Price (₱)*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        sellingPriceSpinner = createCurrencySpinner(0.00);
        inputPanel.add(sellingPriceSpinner, gbc);
        yPos++;

        // SRP (srp_php)
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("SRP (₱)*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        srpSpinner = createCurrencySpinner(0.00);
        inputPanel.add(srpSpinner, gbc);
        yPos++;

        // Supplier (Optional)
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Supplier:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        supplierComboBox = new JComboBox<>();
        inputPanel.add(supplierComboBox, gbc);
        yPos++;

        // Cost Price / WSP (suppliers_items.wsp_php)
        gbc.gridx = 0; gbc.gridy = yPos; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Cost Price (₱):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        costPriceSpinner = createCurrencySpinner(0.00);
        inputPanel.add(costPriceSpinner, gbc);
        yPos++;


        return inputPanel;
    }

    private JSpinner createCurrencySpinner(double initialValue) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initialValue, 0.00, 1000000.00, 0.01));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#,##0.00");
        spinner.setEditor(editor);
        return spinner;
    }

    private void populateComboBox(JComboBox<ComboBoxItem> comboBox, Map<String, Integer> map, String sql, String idColumn, String nameColumn, String defaultText) {
        map.clear();
        List<ComboBoxItem> items = new ArrayList<>();
        items.add(new ComboBoxItem(-1, defaultText)); // Default/placeholder item

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
            items.add(new ComboBoxItem(-1, "Error Loading"));
        }
        comboBox.setModel(new DefaultComboBoxModel<>(items.toArray(new ComboBoxItem[0])));
    }

    private void populateCategories() {
        populateComboBox(categoryComboBox, categoryMap, "SELECT _item_category_id, name FROM item_categories ORDER BY name", "_item_category_id", "name", "-- Select Category --");
    }

    private void populatePackagings() {
        populateComboBox(packagingComboBox, packagingMap, "SELECT _packaging_id, name FROM packagings ORDER BY name", "_packaging_id", "name", "-- Select Packaging --");
    }

    private void populateSuppliers() {
        populateComboBox(supplierComboBox, supplierMap, "SELECT _supplier_id, name FROM suppliers ORDER BY name", "_supplier_id", "name", "-- Select Supplier (Optional) --");
    }


    private void saveNewItem() {
        // 1. Validate Inputs
        String itemName = itemNameField.getText().trim();
        String itemDescription = itemDescriptionArea.getText().trim(); // Can be empty
        ComboBoxItem selectedCategory = (ComboBoxItem) categoryComboBox.getSelectedItem();
        ComboBoxItem selectedPackaging = (ComboBoxItem) packagingComboBox.getSelectedItem();
        ComboBoxItem selectedSupplier = (ComboBoxItem) supplierComboBox.getSelectedItem();

        if (itemName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item Name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            itemNameField.requestFocusInWindow(); return;
        }
        if (selectedCategory == null || selectedCategory.getId() == -1) {
            JOptionPane.showMessageDialog(this, "Category is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            categoryComboBox.requestFocusInWindow(); return;
        }
        if (selectedPackaging == null || selectedPackaging.getId() == -1) {
            JOptionPane.showMessageDialog(this, "Packaging is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            packagingComboBox.requestFocusInWindow(); return;
        }

        int initialQty = (Integer) initialQuantitySpinner.getValue();
        int minQty = (Integer) minQuantitySpinner.getValue();
        BigDecimal sellingPrice = getSpinnerBigDecimal(sellingPriceSpinner);
        BigDecimal srp = getSpinnerBigDecimal(srpSpinner);
        BigDecimal costPrice = getSpinnerBigDecimal(costPriceSpinner); // Will be 0 if disabled

        if (sellingPrice.compareTo(BigDecimal.ZERO) < 0 || srp.compareTo(BigDecimal.ZERO) < 0 || (costPriceSpinner.isEnabled() && costPrice.compareTo(BigDecimal.ZERO) < 0)) {
            JOptionPane.showMessageDialog(this, "Prices cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (initialQty < 0 || minQty < 0) {
            JOptionPane.showMessageDialog(this, "Quantities cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (costPriceSpinner.isEnabled() && (selectedSupplier == null || selectedSupplier.getId() == -1)) {
             JOptionPane.showMessageDialog(this, "If Cost Price is entered, a Supplier must be selected.", "Validation Error", JOptionPane.WARNING_MESSAGE);
             supplierComboBox.requestFocusInWindow(); return;
        }


        // 2. Perform Database Operations within a Transaction
        Connection conn = null;
        boolean success = false;
        int newItemId = -1;

        try {
            conn = MySqlFactoryDao.createConnection();
            conn.setAutoCommit(false); // Start transaction

            // A. Insert into 'items' table
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

            // D. Optionally, insert into 'suppliers_items'
            if (selectedSupplier != null && selectedSupplier.getId() != -1) {
                String sqlSupplierLink = "INSERT INTO suppliers_items (_supplier_id, _item_id, srp_php, wsp_php) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmtSupLink = conn.prepareStatement(sqlSupplierLink)) {
                    pstmtSupLink.setInt(1, selectedSupplier.getId());
                    pstmtSupLink.setInt(2, newItemId);
                    pstmtSupLink.setBigDecimal(3, srp); // SRP from item_stocks can be default here
                    pstmtSupLink.setBigDecimal(4, costPrice); // WSP from the form
                    pstmtSupLink.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            success = true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.println("Transaction is being rolled back");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(this, "Error saving new item: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Item '" + itemName + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parentScene.refreshTableData(); // Refresh the inventory table
            dispose(); // Close dialog
        }
    }

    private BigDecimal getSpinnerBigDecimal(JSpinner spinner) {
        Object value = spinner.getValue();
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP);
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number)value).doubleValue()).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP); // Default or throw error
    }

    // Helper class for ComboBox items to store ID and display name
    private static class ComboBoxItem {
        private final int id;
        private final String name;

        public ComboBoxItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() { return id; }
        public String getName() { return name; }
        @Override public String toString() { return name; }
    }
}