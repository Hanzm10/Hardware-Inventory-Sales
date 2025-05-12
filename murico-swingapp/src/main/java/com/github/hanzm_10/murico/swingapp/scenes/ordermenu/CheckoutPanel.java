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
package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.scenes.ordermenu.CheckoutSearchComponent.ItemData;
import com.github.hanzm_10.murico.swingapp.scenes.ordermenu.CheckoutSearchComponent.ItemSelectedListener;
import com.github.hanzm_10.murico.swingapp.service.database.CheckoutService;
import com.github.hanzm_10.murico.swingapp.service.database.OrderLineItemData;

/** Main panel for the checkout process. */
public class CheckoutPanel extends JPanel implements ItemSelectedListener, QuantityChangeListener, RemoveItemListener {

	private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₱ #,##0.00");
	private static final DecimalFormat INPUT_CURRENCY_FORMAT = new DecimalFormat("#,##0.00"); // For parsing text field

	private CheckoutSearchComponent searchComponent;
	private CheckoutTableComponent tableComponent;
	private CheckoutReceiptComponent receiptComponent;

	private JLabel totalLabelValue; // Renamed for clarity
	private JButton finalizeButton;
	private JTextField cashTenderedField; // Added
	private JLabel changeLabelValue; // Added

	private final int currentBranchId;
	private final Map<Integer, Integer> currentStockCache = new HashMap<>();
	private int lastFinalizedOrderId = -1;
	private BigDecimal currentTotalAmount = BigDecimal.ZERO; // To store the current order total

	public CheckoutPanel(int branchId) {
		this.currentBranchId = branchId;
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		initializeCheckoutUI();
		clearCheckoutState();
	}

	private void clearCheckoutState() {
		if (tableComponent != null) {
			tableComponent.clearTable();
		}
		currentTotalAmount = BigDecimal.ZERO;
		if (totalLabelValue != null) {
			totalLabelValue.setText(CURRENCY_FORMAT.format(currentTotalAmount));
		}
		if (cashTenderedField != null) {
			cashTenderedField.setText(""); // Clear cash field
		}
		if (changeLabelValue != null) {
			changeLabelValue.setText(CURRENCY_FORMAT.format(BigDecimal.ZERO)); // Reset change
		}
		if (receiptComponent != null) {
			receiptComponent.updateReceipt(null, -1, BigDecimal.ZERO);
		}
		if (searchComponent != null) {
			searchComponent.clearSearchField();
		}
		currentStockCache.clear();
		lastFinalizedOrderId = -1;
		System.out.println("Checkout state cleared.");
	}

	private void finalizeOrder() {
		System.out.println("Finalize button clicked.");
		if (tableComponent == null || tableComponent.getTableModel() == null) {
			JOptionPane.showMessageDialog(this, "Checkout table component is not ready.", "Internal Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		DefaultTableModel model = tableComponent.getTableModel();

		if (model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "Cannot finalize an empty order.", "Empty Order",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		BigDecimal cashTendered = getCashTenderedAmount(); // Get from JTextField
		if (cashTendered == null) { // Invalid input in field
			JOptionPane.showMessageDialog(this, "Please enter a valid cash tendered amount.", "Invalid Cash",
					JOptionPane.WARNING_MESSAGE);
			cashTenderedField.requestFocusInWindow();
			return;
		}
		if (cashTendered.compareTo(currentTotalAmount) < 0) {
			JOptionPane.showMessageDialog(this,
					"Cash tendered (" + CURRENCY_FORMAT.format(cashTendered) + ") is less than the total amount due ("
							+ CURRENCY_FORMAT.format(currentTotalAmount) + ").",
					"Insufficient Payment", JOptionPane.WARNING_MESSAGE);
			cashTenderedField.requestFocusInWindow();
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Finalize order?\nTotal: " + totalLabelValue.getText() + "\nCash Tendered: "
						+ CURRENCY_FORMAT.format(cashTendered) + "\nChange: " + changeLabelValue.getText(),
				"Confirm", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			System.out.println("Finalization cancelled.");
			return;
		}

		List<OrderLineItemData> orderItems = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			try {
				String productNameFromTable = (String) model.getValueAt(i, CheckoutTableComponent.COL_PRODUCT_NAME);
				Integer itemId = (Integer) model.getValueAt(i, CheckoutTableComponent.COL_ITEM_ID_DISPLAY);
				Integer quantity = (Integer) model.getValueAt(i, CheckoutTableComponent.COL_QUANTITY);
				BigDecimal price = (BigDecimal) model.getValueAt(i, CheckoutTableComponent.COL_PRICE);
				Integer itemStockId = (Integer) model.getValueAt(i, CheckoutTableComponent.HIDDEN_COL_ITEM_STOCK_ID);

				if (itemStockId == null || itemId == null || productNameFromTable == null || quantity == null
						|| price == null || quantity <= 0) {
					JOptionPane.showMessageDialog(this, "Invalid data in checkout row " + (i + 1), "Data Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String itemNameForDTO = productNameFromTable;
				int parenIndex = productNameFromTable.indexOf('(');
				if (parenIndex > 0) {
					itemNameForDTO = productNameFromTable.substring(0, parenIndex).trim();
				}
				orderItems.add(new OrderLineItemData(itemStockId, itemId, itemNameForDTO, quantity, price));
			} catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(this,
						"Internal data error processing checkout table row " + (i + 1) + ".", "Error",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return;
			}
		}

		Integer customerId = null;
		int employeeId = 1; // TODO: Get actual employee ID

		CheckoutService checkoutService = new CheckoutService();
		try {
			int generatedOrderId = checkoutService.finalizeOrder(orderItems, currentTotalAmount, customerId,
					employeeId);
			this.lastFinalizedOrderId = generatedOrderId;

			JOptionPane.showMessageDialog(this, "Order #" + generatedOrderId + " finalized!", "Complete",
					JOptionPane.INFORMATION_MESSAGE);
			// Receipt already updated by cashTenderedField listener, but confirm with final
			// ID
			receiptComponent.updateReceipt(model, generatedOrderId, cashTendered);
			int printConfirm = JOptionPane.showConfirmDialog(this, "Print receipt?", "Print",
					JOptionPane.YES_NO_OPTION);
			if (printConfirm == JOptionPane.YES_OPTION) {
				receiptComponent.printReceipt(model, generatedOrderId, cashTendered);
			}
			clearCheckoutState();

		} catch (InsufficientStockException e) {
			JOptionPane.showMessageDialog(this, "Order failed: " + e.getMessage(), "Stock Unavailable",
					JOptionPane.WARNING_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Order failed (Database Error):\n" + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, "Order failed (Input Error): " + e.getMessage(), "Input Error",
					JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "An unexpected error occurred during finalization:\n" + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public DefaultTableModel getCheckoutTableModel() {
		return tableComponent != null ? tableComponent.getTableModel() : null;
	}

	/**
	 * Gets the cash tendered amount from the JTextField.
	 *
	 * @return BigDecimal cash tendered, or null if input is invalid or empty.
	 */
	public BigDecimal getCashTenderedAmount() {
		if (cashTenderedField == null)
			return BigDecimal.ZERO; // Should not happen if UI initialized

		String cashInput = cashTenderedField.getText();
		if (cashInput == null || cashInput.trim().isEmpty()) {
			return BigDecimal.ZERO; // Treat empty as zero for preview, finalization will validate
		}
		try {
			// Use a more robust parsing that handles currency symbols and commas
			Number parsedNumber = INPUT_CURRENCY_FORMAT.parse(cashInput.trim());
			BigDecimal amount = new BigDecimal(parsedNumber.toString());

			if (amount.compareTo(BigDecimal.ZERO) < 0) {
				// Optionally show a message here or let finalizeOrder handle it
				return null; // Indicate invalid negative input
			}
			return amount.setScale(2, RoundingMode.HALF_UP);
		} catch (java.text.ParseException e) {
			// Optionally show a message for bad format
			System.err.println("Invalid cash amount format: " + cashInput);
			return null; // Indicate invalid format
		}
	}

	public int getLastFinalizedOrderId() {
		return this.lastFinalizedOrderId;
	}

	private void initializeCheckoutUI() {
		searchComponent = new CheckoutSearchComponent(this);
		add(searchComponent, BorderLayout.NORTH);

		tableComponent = new CheckoutTableComponent(this.currentStockCache, this, this);
		add(tableComponent, BorderLayout.CENTER);

		// --- Bottom Panel: Cash Input, Change, Total ---
		JPanel bottomOuterPanel = new JPanel(new BorderLayout());

		// Panel for Cash Tendered and Change (above Total)
		JPanel paymentDetailsPanel = new JPanel(new GridBagLayout());
		paymentDetailsPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY), // Top
						// border
						new EmptyBorder(5, 5, 5, 5) // Padding
				));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 5, 2, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel cashLabel = new JLabel("Cash Tendered:");
		cashLabel.setFont(new Font("Montserrat Medium", Font.PLAIN, 14));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		paymentDetailsPanel.add(cashLabel, gbc);

		cashTenderedField = new JTextField(10);
		cashTenderedField.setFont(new Font("Montserrat Medium", Font.PLAIN, 14));
		cashTenderedField.setHorizontalAlignment(JTextField.RIGHT);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		paymentDetailsPanel.add(cashTenderedField, gbc);

		JLabel changeTextLabel = new JLabel("Change:");
		changeTextLabel.setFont(new Font("Montserrat Medium", Font.PLAIN, 14));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		paymentDetailsPanel.add(changeTextLabel, gbc);

		changeLabelValue = new JLabel(CURRENCY_FORMAT.format(BigDecimal.ZERO));
		changeLabelValue.setFont(new Font("Montserrat Bold", Font.BOLD, 14));
		changeLabelValue.setHorizontalAlignment(JLabel.RIGHT);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		paymentDetailsPanel.add(changeLabelValue, gbc);

		bottomOuterPanel.add(paymentDetailsPanel, BorderLayout.NORTH);

		// Total Panel (below cash/change)
		JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY)); // Top border for total
		totalPanel.add(new JLabel("Total:"));
		totalLabelValue = new JLabel(CURRENCY_FORMAT.format(0.00));
		totalLabelValue.setFont(new Font("Montserrat Bold", Font.BOLD, 18));
		totalPanel.add(totalLabelValue);
		bottomOuterPanel.add(totalPanel, BorderLayout.SOUTH);

		add(bottomOuterPanel, BorderLayout.SOUTH);

		// --- Right Side Panel: Receipt and Finalize Button ---
		JPanel rightSidePanel = new JPanel(new BorderLayout(5, 10));
		rightSidePanel.setPreferredSize(new Dimension(320, 0)); // Keep preferred width for receipt
		receiptComponent = new CheckoutReceiptComponent();
		rightSidePanel.add(receiptComponent, BorderLayout.CENTER);

		JPanel finalizeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		finalizeButton = new JButton("Finalize Order");
		finalizeButton.setFont(new Font("Montserrat Bold", Font.BOLD, 14));
		finalizeButton.addActionListener(e -> finalizeOrder());
		finalizeButtonPanel.add(finalizeButton);
		rightSidePanel.add(finalizeButtonPanel, BorderLayout.SOUTH);
		add(rightSidePanel, BorderLayout.EAST);

		// Add listener to cash tendered field
		cashTenderedField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				handleCashChange();
			}

			public void removeUpdate(DocumentEvent e) {
				handleCashChange();
			}

			public void changedUpdate(DocumentEvent e) {
				handleCashChange();
			}
		});
	}

	/**
	 * Handles changes in the cashTenderedField. Updates change label and receipt
	 * preview.
	 */
	private void handleCashChange() {
		BigDecimal cashTendered = getCashTenderedAmount();
		if (cashTendered == null) { // Invalid format
			// Optionally clear change or show error indicator, for now, treat as 0 for
			// calculation
			cashTendered = BigDecimal.ZERO;
		}

		BigDecimal change = cashTendered.subtract(currentTotalAmount);
		if (change.compareTo(BigDecimal.ZERO) < 0) {
			change = BigDecimal.ZERO; // Do not show negative change
		}
		changeLabelValue.setText(CURRENCY_FORMAT.format(change));

		// Update receipt preview with current cash tendered (even if not yet finalized)
		if (receiptComponent != null && tableComponent != null) {
			receiptComponent.updateReceipt(tableComponent.getTableModel(), lastFinalizedOrderId, cashTendered);
		}
	}

	@Override
	public void itemRemovalRequested(int modelRowIndex, int itemStockId) {
		System.out.println("Removal: Row=" + modelRowIndex + ", ItemStockID=" + itemStockId);
		currentStockCache.remove(itemStockId);
		tableComponent.removeRowByIndex(modelRowIndex);
		updateCheckoutTotalAndPaymentDetails();
	}

	@Override
	public void itemSelected(ItemData selectedItem) {
		System.out.println("Selected: " + selectedItem.itemName() + " (StockID: " + selectedItem.itemStockId() + ")");
		DefaultTableModel model = tableComponent.getTableModel();
		int existingRow = -1;
		for (int i = 0; i < model.getRowCount(); i++) {
			Integer itemStockIdInTable = (Integer) model.getValueAt(i, CheckoutTableComponent.HIDDEN_COL_ITEM_STOCK_ID);
			if (itemStockIdInTable != null && itemStockIdInTable.equals(selectedItem.itemStockId())) {
				existingRow = i;
				break;
			}
		}

		if (existingRow != -1) {
			int currentQtyInTable = (Integer) model.getValueAt(existingRow, CheckoutTableComponent.COL_QUANTITY);
			int maxStock = currentStockCache.getOrDefault(selectedItem.itemStockId(), 0);
			if (currentQtyInTable < maxStock) {
				model.setValueAt(currentQtyInTable + 1, existingRow, CheckoutTableComponent.COL_QUANTITY);
				quantityChanged(existingRow, currentQtyInTable + 1, selectedItem.itemStockId()); // This will call
				// updateCheckoutTotalAndPaymentDetails
			} else {
				JOptionPane.showMessageDialog(this, "Cannot add more. Max stock (" + maxStock + ") reached.",
						"Stock Limit", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			if (selectedItem.currentStock() > 0) {
				currentStockCache.put(selectedItem.itemStockId(), selectedItem.currentStock());
				Vector<Object> rowData = new Vector<>();
				rowData.add(selectedItem.itemName() + " (" + selectedItem.packagingName() + ")");
				rowData.add(selectedItem.itemId());
				rowData.add(1);
				rowData.add(selectedItem.price());
				rowData.add("Remove");
				rowData.add(selectedItem.itemStockId());
				tableComponent.addRow(rowData);
				updateCheckoutTotalAndPaymentDetails();
			} else {
				JOptionPane.showMessageDialog(this, "Out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	@Override
	public void quantityChanged(int modelRowIndex, int newQuantity, int itemStockId) {
		System.out.println(
				"Qty Changed: Row=" + modelRowIndex + ", ItemStockID=" + itemStockId + ", NewQty=" + newQuantity);
		updateCheckoutTotalAndPaymentDetails();
	}

	private void styleIconButtonLabel(JLabel label) {
		/* Not currently used, keep if needed elsewhere */
	}

	/**
	 * Recalculates total, updates total label, and then updates payment details
	 * (change, receipt preview).
	 */
	private void updateCheckoutTotalAndPaymentDetails() {
		if (totalLabelValue == null || tableComponent == null || tableComponent.getTableModel() == null) {
			System.err.println("Cannot update total: Required components/model are null.");
			return;
		}
		DefaultTableModel model = tableComponent.getTableModel();
		currentTotalAmount = BigDecimal.ZERO; // Reset before recalculating
		for (int i = 0; i < model.getRowCount(); i++) {
			try {
				Object quantityObj = model.getValueAt(i, CheckoutTableComponent.COL_QUANTITY);
				Object priceObj = model.getValueAt(i, CheckoutTableComponent.COL_PRICE);
				if (quantityObj instanceof Integer && priceObj instanceof BigDecimal) {
					int quantity = (Integer) quantityObj;
					BigDecimal price = (BigDecimal) priceObj;
					if (quantity > 0) {
						currentTotalAmount = currentTotalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
					}
				}
			} catch (Exception e) {
				System.err.println("Error calculating total row " + i + ": " + e);
			}
		}
		totalLabelValue.setText(CURRENCY_FORMAT.format(currentTotalAmount));
		System.out.println("Checkout total updated: " + CURRENCY_FORMAT.format(currentTotalAmount));

		// After total is updated, recalculate change and update receipt
		handleCashChange(); // This will use the new currentTotalAmount
	}
}
