package com.github.hanzm_10.murico.swingapp.scenes.home.order_menu;

import java.awt.BorderLayout;
import java.awt.Color;
// import java.awt.Cursor; // Not strictly needed now
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.CheckoutSearchComponent.ItemData;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.CheckoutSearchComponent.ItemSelectedListener;
import com.github.hanzm_10.murico.swingapp.service.database.CheckoutService;
import com.github.hanzm_10.murico.swingapp.service.database.OrderLineItemData;


public class CheckoutPanel extends JPanel implements ItemSelectedListener,
		QuantityChangeListener,
		RemoveItemListener {

	private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₱ #,##0.00");
	private static final DecimalFormat INPUT_CURRENCY_FORMAT = new DecimalFormat("#,##0.00");

	private CheckoutSearchComponent searchComponent;
	private CheckoutTableComponent tableComponent;
	private CheckoutReceiptComponent receiptComponent;

	private JLabel totalLabelValue;
	private JButton finalizeButton;
	private JTextField cashTenderedField;
	private JButton cashEnterButton;
	private JLabel changeLabelValue;

	private final int currentBranchId;
	private final Map<Integer, Integer> currentStockCache = new HashMap<>();
	private int lastFinalizedOrderId = -1;
	private BigDecimal currentTotalAmount = BigDecimal.ZERO;
    private BigDecimal currentCashTenderedForPreview = BigDecimal.ZERO;

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
        currentCashTenderedForPreview = BigDecimal.ZERO;
		if (totalLabelValue != null) {
			totalLabelValue.setText(CURRENCY_FORMAT.format(currentTotalAmount));
		}
		if (cashTenderedField != null) {
			cashTenderedField.setText("");
		}
		if (changeLabelValue != null) {
			changeLabelValue.setText(CURRENCY_FORMAT.format(BigDecimal.ZERO));
		}
		if (receiptComponent != null) {
			receiptComponent.updateReceipt(null, -1, currentCashTenderedForPreview);
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
		if (tableComponent == null || tableComponent.getTableModel() == null || totalLabelValue == null) { // Added totalLabelValue null check
			JOptionPane.showMessageDialog(this, "Checkout components are not ready.", "Internal Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		DefaultTableModel model = tableComponent.getTableModel();

		if (model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "Cannot finalize an empty order.", "Empty Order", JOptionPane.WARNING_MESSAGE);
			return;
		}

        // Ensure total is up-to-date before final validation
        recalculateTotalFromTable(); // Recalculate total just in case

		BigDecimal cashFromFieldAtFinalize = getCashTenderedAmountFromField();
		if (cashFromFieldAtFinalize == null) {
			JOptionPane.showMessageDialog(this, "Please enter a valid cash tendered amount and press 'Enter' next to it.", "Invalid Cash", JOptionPane.WARNING_MESSAGE);
			cashTenderedField.requestFocusInWindow();
			return;
		}
		if (cashFromFieldAtFinalize.compareTo(currentTotalAmount) < 0) {
			JOptionPane.showMessageDialog(this, "Cash tendered (" + CURRENCY_FORMAT.format(cashFromFieldAtFinalize) + ") is less than the total amount due (" + CURRENCY_FORMAT.format(currentTotalAmount) + ").", "Insufficient Payment", JOptionPane.WARNING_MESSAGE);
			cashTenderedField.requestFocusInWindow();
			return;
		}
        // Update currentCashTenderedForPreview to the validated amount from the field
        currentCashTenderedForPreview = cashFromFieldAtFinalize;


        BigDecimal changeForDialog = currentCashTenderedForPreview.subtract(currentTotalAmount);
        if (changeForDialog.compareTo(BigDecimal.ZERO) < 0) changeForDialog = BigDecimal.ZERO;


		int confirm = JOptionPane.showConfirmDialog(this, "Finalize order?\nTotal: " + totalLabelValue.getText() + "\nCash Tendered: " + CURRENCY_FORMAT.format(currentCashTenderedForPreview) + "\nChange: " + CURRENCY_FORMAT.format(changeForDialog), "Confirm", JOptionPane.YES_NO_OPTION);
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

				if (itemStockId == null || itemId == null || productNameFromTable == null || quantity == null || price == null || quantity <= 0) {
					JOptionPane.showMessageDialog(this, "Invalid data in checkout row " + (i + 1), "Data Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String itemNameForDTO = productNameFromTable;
				int parenIndex = productNameFromTable.indexOf('(');
				if (parenIndex > 0) {
					itemNameForDTO = productNameFromTable.substring(0, parenIndex).trim();
				}
				orderItems.add(new OrderLineItemData(itemStockId, itemId, itemNameForDTO, quantity, price));
			} catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(this, "Internal data error processing checkout table row " + (i + 1) + ".", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return;
			}
		}

		Integer customerId = null;
		int employeeId = 1;

		CheckoutService checkoutService = new CheckoutService();
		try {
			// Pass the definitively correct currentTotalAmount
			int generatedOrderId = checkoutService.finalizeOrder(orderItems, currentTotalAmount, customerId, employeeId);
			this.lastFinalizedOrderId = generatedOrderId;

			JOptionPane.showMessageDialog(this, "Order #" + generatedOrderId + " finalized!", "Complete", JOptionPane.INFORMATION_MESSAGE);
			receiptComponent.updateReceipt(model, generatedOrderId, currentCashTenderedForPreview);
			int printConfirm = JOptionPane.showConfirmDialog(this, "Print receipt?", "Print", JOptionPane.YES_NO_OPTION);
			if (printConfirm == JOptionPane.YES_OPTION) {
				receiptComponent.printReceipt(model, generatedOrderId, currentCashTenderedForPreview);
			}
			clearCheckoutState();

		} catch (InsufficientStockException e) {
			JOptionPane.showMessageDialog(this, "Order failed: " + e.getMessage(), "Stock Unavailable", JOptionPane.WARNING_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Order failed (Database Error):\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, "Order failed (Input Error): " + e.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "An unexpected error occurred during finalization:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public DefaultTableModel getCheckoutTableModel() {
		return tableComponent != null ? tableComponent.getTableModel() : null;
	}

	private BigDecimal getCashTenderedAmountFromField() {
		if (cashTenderedField == null) return null;
		String cashInput = cashTenderedField.getText();
		if (cashInput == null || cashInput.trim().isEmpty()) {
			return BigDecimal.ZERO;
		}
		try {
            Number parsedNumber = INPUT_CURRENCY_FORMAT.parse(cashInput.trim());
            BigDecimal amount = new BigDecimal(parsedNumber.toString());
			if (amount.compareTo(BigDecimal.ZERO) < 0) {
				return null;
			}
			return amount.setScale(2, RoundingMode.HALF_UP);
		} catch (java.text.ParseException e) {
			System.err.println("Invalid cash amount format during get: " + cashInput);
			return null;
		}
	}
    
    public BigDecimal getCashTenderedAmount() {
        return this.currentCashTenderedForPreview;
    }

	public int getLastFinalizedOrderId() {
		return this.lastFinalizedOrderId;
	}

	private void initializeCheckoutUI() {
        // ... (searchComponent, tableComponent setup is the same) ...
		searchComponent = new CheckoutSearchComponent(this);
		add(searchComponent, BorderLayout.NORTH);

		tableComponent = new CheckoutTableComponent(this.currentStockCache, this, this);
		add(tableComponent, BorderLayout.CENTER);

		JPanel bottomOuterPanel = new JPanel(new BorderLayout());
		JPanel paymentDetailsPanel = new JPanel(new GridBagLayout());
		paymentDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
				new EmptyBorder(5,5,5,5)
		));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 5, 2, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel cashLabel = new JLabel("Cash Tendered:");
		cashLabel.setFont(new Font("Montserrat Medium", Font.PLAIN, 14));
		gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
		paymentDetailsPanel.add(cashLabel, gbc);

		cashTenderedField = new JTextField(10);
		cashTenderedField.setFont(new Font("Montserrat Medium", Font.PLAIN, 14));
		cashTenderedField.setHorizontalAlignment(JTextField.RIGHT);
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
		paymentDetailsPanel.add(cashTenderedField, gbc);

		cashEnterButton = new JButton("Enter");
        cashEnterButton.setFont(new Font("Montserrat Medium", Font.PLAIN, 12));
        cashEnterButton.setMargin(new Insets(2,5,2,5));
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        paymentDetailsPanel.add(cashEnterButton, gbc);

		JLabel changeTextLabel = new JLabel("Change:");
		changeTextLabel.setFont(new Font("Montserrat Medium", Font.PLAIN, 14));
		gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
		paymentDetailsPanel.add(changeTextLabel, gbc);

		changeLabelValue = new JLabel(CURRENCY_FORMAT.format(BigDecimal.ZERO));
		changeLabelValue.setFont(new Font("Montserrat Bold", Font.BOLD, 14));
		changeLabelValue.setHorizontalAlignment(JLabel.RIGHT);
		gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
		paymentDetailsPanel.add(changeLabelValue, gbc);
        gbc.gridwidth = 1;

		bottomOuterPanel.add(paymentDetailsPanel, BorderLayout.NORTH);

		JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		totalPanel.add(new JLabel("Total:"));
		totalLabelValue = new JLabel(CURRENCY_FORMAT.format(0.00));
		totalLabelValue.setFont(new Font("Montserrat Bold", Font.BOLD, 18));
		totalPanel.add(totalLabelValue);
		bottomOuterPanel.add(totalPanel, BorderLayout.SOUTH);
		add(bottomOuterPanel, BorderLayout.SOUTH);

		JPanel rightSidePanel = new JPanel(new BorderLayout(5, 10));
		rightSidePanel.setPreferredSize(new Dimension(320, 0));
		receiptComponent = new CheckoutReceiptComponent();
		rightSidePanel.add(receiptComponent, BorderLayout.CENTER);

		JPanel finalizeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		finalizeButton = new JButton("Finalize Order");
		finalizeButton.setFont(new Font("Montserrat Bold", Font.BOLD, 14));
		finalizeButton.addActionListener(e -> finalizeOrder());
		finalizeButtonPanel.add(finalizeButton);
		rightSidePanel.add(finalizeButtonPanel, BorderLayout.SOUTH);
		add(rightSidePanel, BorderLayout.EAST);

		cashEnterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                // 1. Refresh the total from the table
                recalculateTotalFromTable(); // This updates currentTotalAmount and totalLabelValue

                // 2. Process cash tendered against the refreshed total
				boolean formatErrorShown = processCashTenderedAndShowErrors(); // New method name for clarity
                
                // 3. If no format error, explicitly check for insufficient payment
                if (!formatErrorShown) {
                    BigDecimal cashParsed = getCashTenderedAmountFromField(); // Re-parse for this check
                    // Check if cashParsed is not null (valid format) and >= 0 before comparing to total
                    if (cashParsed != null && cashParsed.compareTo(BigDecimal.ZERO) >= 0 && cashParsed.compareTo(currentTotalAmount) < 0) {
                         JOptionPane.showMessageDialog(CheckoutPanel.this, "Cash tendered is less than the total amount.", "Insufficient Payment", JOptionPane.WARNING_MESSAGE);
                    }
                    // If cashParsed is null (bad format) but field is not empty, processCashTenderedAndShowErrors already showed the error.
                    // If cashParsed is ZERO (empty field), no insufficient payment error for just pressing Enter on empty.
                }
			}
		});
	}

    /**
     * Processes the cash tendered from the input field, intended to be called by "Enter" button.
     * Shows errors for bad format or insufficient payment. Updates UI.
     * @return true if a format error dialog was shown, false otherwise.
     */
	private boolean processCashTenderedAndShowErrors() {
		BigDecimal cashParsed = getCashTenderedAmountFromField();
        boolean formatErrorDialogShown = false;

		if (cashParsed == null) { // Indicates bad format or negative value
            if (!cashTenderedField.getText().trim().isEmpty()) { // Only show error if field had non-empty invalid content
                JOptionPane.showMessageDialog(this, "Invalid cash amount entered. Please enter a valid non-negative number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                formatErrorDialogShown = true;
            }
            // Reset preview values if parsing failed or field is empty
            currentCashTenderedForPreview = BigDecimal.ZERO;
            changeLabelValue.setText(CURRENCY_FORMAT.format(BigDecimal.ZERO));
        } else {
            // Parsed successfully (non-negative number)
            currentCashTenderedForPreview = cashParsed;
            BigDecimal change = currentCashTenderedForPreview.subtract(currentTotalAmount);
            if (change.compareTo(BigDecimal.ZERO) < 0) {
                change = BigDecimal.ZERO; // Don't show negative change, "insufficient" warning handled by caller
            }
            changeLabelValue.setText(CURRENCY_FORMAT.format(change));
        }

		// Update receipt preview always
		if (receiptComponent != null && tableComponent != null) {
			receiptComponent.updateReceipt(tableComponent.getTableModel(), lastFinalizedOrderId, currentCashTenderedForPreview);
		}
        return formatErrorDialogShown;
	}


	@Override
	public void itemRemovalRequested(int modelRowIndex, int itemStockId) {
		System.out.println("Removal: Row=" + modelRowIndex + ", ItemStockID=" + itemStockId);
		currentStockCache.remove(itemStockId);
		tableComponent.removeRowByIndex(modelRowIndex);
		updateCheckoutTotalAndRefreshPaymentDetails();
	}

	@Override
	public void itemSelected(ItemData selectedItem) {
        // ... (existing itemSelected logic) ...
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
				quantityChanged(existingRow, currentQtyInTable + 1, selectedItem.itemStockId());
			} else {
				JOptionPane.showMessageDialog(this, "Cannot add more '" + selectedItem.itemName() + " (" + selectedItem.packagingName() + ")'. Max stock (" + maxStock + ") reached.", "Stock Limit", JOptionPane.WARNING_MESSAGE);
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
				updateCheckoutTotalAndRefreshPaymentDetails();
			} else {
				JOptionPane.showMessageDialog(this, "'" + selectedItem.itemName() + " (" + selectedItem.packagingName() + ")' is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	@Override
	public void quantityChanged(int modelRowIndex, int newQuantity, int itemStockId) {
		System.out.println("Qty Changed: Row=" + modelRowIndex + ", ItemStockID=" + itemStockId + ", NewQty=" + newQuantity);
		updateCheckoutTotalAndRefreshPaymentDetails();
	}

    /**
     * Recalculates currentTotalAmount from the table and updates the totalLabelValue.
     */
    private void recalculateTotalFromTable() {
        if (totalLabelValue == null || tableComponent == null || tableComponent.getTableModel() == null) {
			System.err.println("Cannot update total: Required components/model are null.");
			return;
		}
		DefaultTableModel model = tableComponent.getTableModel();
		currentTotalAmount = BigDecimal.ZERO;
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
		System.out.println("Checkout total recalculated: " + CURRENCY_FORMAT.format(currentTotalAmount));
    }


	/**
	 * Recalculates total, then updates payment details (change, receipt preview)
     * without showing format errors for the cash field.
	 */
	private void updateCheckoutTotalAndRefreshPaymentDetails() {
        recalculateTotalFromTable(); // This updates currentTotalAmount and totalLabelValue

        // Now, process cash tendered against the *new* total, but don't show format errors
        // The old `processCashTendered(false)` logic is effectively what `processCashTenderedAndShowErrors`
        // does when its internal showError... flag is false (which happens if it's not called by Enter).
        // Let's simplify: just update the receipt based on currentCashTenderedForPreview
        // The Enter button is the explicit trigger for re-evaluating cash input.

        // If an item changes, the total changes. The change should reflect this against the
        // *last entered cash*.
        BigDecimal cashForThisUpdate = currentCashTenderedForPreview; // Use last "good" cash
        BigDecimal change = cashForThisUpdate.subtract(currentTotalAmount);
        if (change.compareTo(BigDecimal.ZERO) < 0) {
            change = BigDecimal.ZERO;
        }
        changeLabelValue.setText(CURRENCY_FORMAT.format(change));

        if (receiptComponent != null && tableComponent != null) {
			receiptComponent.updateReceipt(tableComponent.getTableModel(), lastFinalizedOrderId, cashForThisUpdate);
		}
	}
}