package com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs;

import com.github.hanzm_10.murico.swingapp.scenes.inventory.InventoryScene; // To call back parent

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.math.BigDecimal;
import java.math.RoundingMode; // For setting scale

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class EditItemStockDialog extends JDialog {

    private final InventoryScene parentScene;
    private final int editingItemStockId;

    // UI Components
    private JLabel productNameLabel;
    private JLabel currentStockLabel;
    private JSpinner sellingPriceSpinner; // For item_stocks.price_php
    private JSpinner minQtySpinner;       // For item_stocks.minimum_quantity
    // Optional: Add SRP spinner if needed
    // private JSpinner srpSpinner;

    public EditItemStockDialog(Window owner, InventoryScene parent, int itemStockId, String productName, int minQty, BigDecimal unitPrice, int currentStock) {
        super(owner, "Edit Stock Details", Dialog.ModalityType.APPLICATION_MODAL);
        this.parentScene = parent;
        this.editingItemStockId = itemStockId;

        // --- UI Setup ---
        setSize(400, 300); // Adjusted size
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15)); // More padding

        // --- Title Panel ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Edit: " + productName);
        titleLabel.setFont(new Font("Montserrat Bold", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        titlePanel.add(new JLabel("(Stock ID: " + itemStockId + ")"));
        add(titlePanel, BorderLayout.NORTH);


        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Stock Details"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Read-only Info
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; // Labels don't fill horizontally
        inputPanel.add(new JLabel("Current Stock:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        currentStockLabel = new JLabel(currentStock + " unit(s)");
        currentStockLabel.setFont(currentStockLabel.getFont().deriveFont(Font.ITALIC));
        inputPanel.add(currentStockLabel, gbc);
        gbc.weightx = 0.0; // Reset weight

        // Editable Fields
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Selling Price (₱):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        // Use BigDecimal for spinner model if possible, or double and convert
        sellingPriceSpinner = new JSpinner(new SpinnerNumberModel(
            unitPrice != null ? unitPrice.doubleValue() : 0.00, // Initial value
            0.00,         // Minimum value
            1000000.00,   // Maximum value (adjust as needed)
            0.01          // Step
        ));
        // Set editor for currency format
        JSpinner.NumberEditor priceEditor = new JSpinner.NumberEditor(sellingPriceSpinner, "#,##0.00");
        sellingPriceSpinner.setEditor(priceEditor);
        // Ensure the editor's text field allows appropriate input length if needed
        // JFormattedTextField priceTextField = ((JSpinner.NumberEditor)sellingPriceSpinner.getEditor()).getTextField();
        // // Optional: Add validation/formatter if needed
        inputPanel.add(sellingPriceSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Minimum Quantity:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        minQtySpinner = new JSpinner(new SpinnerNumberModel(
            minQty,       // Initial value
            0,            // Minimum value
            100000,       // Maximum value (adjust as needed)
            1             // Step
        ));
        inputPanel.add(minQtySpinner, gbc);

        // Optional: Add SRP field here if needed, similar to Selling Price

        add(inputPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveChanges());
        cancelButton.addActionListener(e -> dispose()); // Simply close the dialog

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void saveChanges() {
        // 1. Get values from spinners
        BigDecimal newPrice;
        int newMinQty;

        try {
            // Important: Get value from spinner model, then convert to BigDecimal for precision
            Object priceValue = sellingPriceSpinner.getValue();
            if (priceValue instanceof BigDecimal) { // Should ideally use BigDecimal model
                 newPrice = (BigDecimal) priceValue;
            } else if (priceValue instanceof Number) {
                 newPrice = BigDecimal.valueOf(((Number)priceValue).doubleValue());
            } else {
                 throw new ClassCastException("Unexpected type for price spinner value");
            }
             // Ensure correct scale (2 decimal places)
            newPrice = newPrice.setScale(2, RoundingMode.HALF_UP);


            Object minQtyValue = minQtySpinner.getValue();
             if (minQtyValue instanceof Integer) {
                 newMinQty = (Integer) minQtyValue;
             } else {
                 throw new ClassCastException("Unexpected type for min quantity spinner value");
             }


            // Basic validation
            if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
                 JOptionPane.showMessageDialog(this, "Selling Price cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                 sellingPriceSpinner.requestFocusInWindow();
                 return;
            }
            if (newMinQty < 0) {
                 JOptionPane.showMessageDialog(this, "Minimum Quantity cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                 minQtySpinner.requestFocusInWindow();
                 return;
            }

        } catch (ClassCastException cce) {
            JOptionPane.showMessageDialog(this, "Invalid data format in input fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            cce.printStackTrace(); // Log the error for debugging
            return;
        }

        // 2. Call parent's update method
        boolean success = parentScene.updateItemStockDetails(editingItemStockId, newPrice, newMinQty);

        // 3. Handle result
        if (success) {
            JOptionPane.showMessageDialog(this, "Stock details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            parentScene.refreshTableData(); // Refresh the inventory table in the parent scene
            dispose(); // Close this dialog
        } else {
            // The update method in InventoryScene should have shown an error dialog already
            System.err.println("Update failed for item stock ID: " + editingItemStockId);
            // Optionally show another message here, but might be redundant
        }
    }
}