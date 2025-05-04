package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

// --- Imports ---
import com.github.hanzm_10.murico.swingapp.scenes.ordermenu.InsufficientStockException;
import com.github.hanzm_10.murico.swingapp.service.database.CheckoutService; // Import the service
import com.github.hanzm_10.murico.swingapp.service.database.OrderLineItemData; // Import DTO

// Import the nested record and listener interfaces
import com.github.hanzm_10.murico.swingapp.scenes.ordermenu.CheckoutSearchComponent.ItemData;
import com.github.hanzm_10.murico.swingapp.scenes.ordermenu.CheckoutSearchComponent.ItemSelectedListener;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode; // For VAT calculation
import java.sql.SQLException; // For catching service exceptions
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
// --- End Imports ---

/**
 * Main panel for the checkout process. Orchestrates search, table, receipt,
 * and finalization by interacting with dedicated components and services.
 * Implements listeners to react to events from child components.
 */
public class CheckoutPanel extends JPanel implements
        ItemSelectedListener, // Listener for search results
        QuantityChangeListener, // Listener for table quantity changes
        RemoveItemListener // Listener for table remove requests
{

    // --- UI Components ---
    private CheckoutSearchComponent searchComponent; // Dedicated search component
    private CheckoutTableComponent tableComponent;   // Dedicated table component
    private CheckoutReceiptComponent receiptComponent; // Dedicated receipt component
    private JLabel totalLabel;                       // Total display
    private JButton finalizeButton;                  // Finalize button

    // --- State & Configuration ---
    private final int currentBranchId; // Although branching is gone, might represent store ID or keep for compatibility
    private final Map<Integer, Integer> currentStockCache = new HashMap<>(); // Key: itemStockId, Value: quantity
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₱ #,##0.00");
    // Example VAT Rate (adjust as needed or make configurable)
    private static final BigDecimal VAT_RATE = new BigDecimal("0.12"); // 12%
    private static final BigDecimal ONE_PLUS_VAT = BigDecimal.ONE.add(VAT_RATE);


    /**
     * Creates the main Checkout Panel.
     * @param branchId The ID of the branch/store this checkout operates in.
     */
    public CheckoutPanel(int branchId) {
        // Store branchId even if schema changed, might be useful for context/logging
        this.currentBranchId = branchId;
        setLayout(new BorderLayout(10, 10)); // Gaps between components
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around the panel
        initializeCheckoutUI();
        clearCheckoutState(); // Start with a clean state
    }

    /**
     * Initializes the main UI structure and child components.
     */
    private void initializeCheckoutUI() {
        // --- Top: Item Search Component ---
        // Pass 'this' as the ItemSelectedListener
        searchComponent = new CheckoutSearchComponent(this);
        add(searchComponent, BorderLayout.NORTH);

        // --- Center: Checkout Table Component ---
        // Pass the shared cache and 'this' for quantity/remove listeners
        tableComponent = new CheckoutTableComponent(this.currentStockCache, this, this);
        add(tableComponent, BorderLayout.CENTER);

        // --- Bottom: Total Panel ---
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        totalPanel.add(new JLabel("Total: "));
        totalLabel = new JLabel(CURRENCY_FORMAT.format(0.00));
        totalLabel.setFont(new Font("Montserrat Bold", Font.BOLD, 18));
        totalPanel.add(totalLabel);
        add(totalPanel, BorderLayout.SOUTH);

        // --- Right: Receipt Component & Finalize Button ---
        JPanel rightSidePanel = new JPanel(new BorderLayout(5, 10));
        rightSidePanel.setPreferredSize(new Dimension(320, 0));

        // Pass branchId for display purposes
        receiptComponent = new CheckoutReceiptComponent(this.currentBranchId);
        rightSidePanel.add(receiptComponent, BorderLayout.CENTER);

        JPanel finalizeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        finalizeButton = new JButton("Finalize Order");
        finalizeButton.setFont(new Font("Montserrat Bold", Font.BOLD, 14));
        finalizeButton.addActionListener(e -> finalizeOrder()); // Attach listener
        finalizeButtonPanel.add(finalizeButton);
        rightSidePanel.add(finalizeButtonPanel, BorderLayout.SOUTH);

        add(rightSidePanel, BorderLayout.EAST);
    }

    // --- Implementation of Listener Interfaces ---

    /**
     * Called by CheckoutSearchComponent when an item (with specific packaging/stock ID)
     * is selected from suggestions.
     * @param selectedItem The data of the selected item stock entry.
     */
    @Override
    public void itemSelected(ItemData selectedItem) {
        System.out.println("CheckoutPanel received item selection: " + selectedItem.itemName() + " (StockID: " + selectedItem.itemStockId() + ")");
        DefaultTableModel model = tableComponent.getTableModel();

        // Check if this SPECIFIC item_stock_id already exists in the table
        int existingRow = -1;
        for (int i = 0; i < model.getRowCount(); i++) {
            // Get itemStockId from the hidden column
            Integer itemStockIdInTable = (Integer) model.getValueAt(i, CheckoutTableComponent.HIDDEN_COL_ITEM_STOCK_ID);
            if (itemStockIdInTable != null && itemStockIdInTable.equals(selectedItem.itemStockId())) {
                existingRow = i;
                break;
            }
        }

        if (existingRow != -1) {
            // Item stock entry exists, increment quantity if stock allows
            int currentQtyInTable = (Integer) model.getValueAt(existingRow, CheckoutTableComponent.COL_QUANTITY);
            int maxStock = currentStockCache.getOrDefault(selectedItem.itemStockId(), 0); // Use cache

            if (currentQtyInTable < maxStock) {
                model.setValueAt(currentQtyInTable + 1, existingRow, CheckoutTableComponent.COL_QUANTITY);
                // Trigger updates manually after modifying model directly
                quantityChanged(existingRow, currentQtyInTable + 1, selectedItem.itemStockId());
            } else {
                JOptionPane.showMessageDialog(this,
                    "Cannot add more '" + selectedItem.itemName() + " (" + selectedItem.packagingName() + ")'. Max stock (" + maxStock + ") reached.",
                    "Stock Limit", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            // Item stock entry not found, add as a new row if stock > 0
            if (selectedItem.currentStock() > 0) {
                 // Add/Update the stock cache
                 currentStockCache.put(selectedItem.itemStockId(), selectedItem.currentStock());
                 System.out.println("Adding ItemStockID " + selectedItem.itemStockId() + " with cached stock " + selectedItem.currentStock());

                // Prepare row data VECTOR, including hidden itemStockId at the end
                Vector<Object> rowData = new Vector<>();
                rowData.add(selectedItem.itemName() + " (" + selectedItem.packagingName() + ")"); // Display Name
                rowData.add(selectedItem.itemId());           // Item ID (for display)
                rowData.add(1);                               // Initial Quantity
                rowData.add(selectedItem.price());            // Price (SRP)
                rowData.add("Remove");                        // Action Text
                rowData.add(selectedItem.itemStockId());      // HIDDEN Item Stock ID

                tableComponent.addRow(rowData); // Add row via table component

                // Update totals and receipt preview
                updateCheckoutTotal();
                receiptComponent.updateReceipt(tableComponent.getTableModel(), -1); // Pass model
            } else {
                 JOptionPane.showMessageDialog(this,
                    "'" + selectedItem.itemName() + " (" + selectedItem.packagingName() + ")' is out of stock.",
                    "Out of Stock", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Called by CheckoutTableComponent's SpinnerCellEditor when quantity changes.
     * @param modelRowIndex The row index in the table model.
     * @param newQuantity The new quantity value.
     * @param itemStockId The ID of the item stock entry whose quantity changed.
     */
    @Override
    public void quantityChanged(int modelRowIndex, int newQuantity, int itemStockId) {
        System.out.println("CheckoutPanel notified of quantity change: Row=" + modelRowIndex + ", ItemStockID=" + itemStockId + ", NewQty=" + newQuantity);
        // Quantity already updated in the model, just update downstream UI
        updateCheckoutTotal();
        receiptComponent.updateReceipt(tableComponent.getTableModel(), -1); // Update preview
    }

    /**
     * Called by CheckoutTableComponent's ButtonEditor when remove button is clicked.
     * @param modelRowIndex The row index in the table model.
     * @param itemStockId The ID of the item stock entry to be removed.
     */
    @Override
    public void itemRemovalRequested(int modelRowIndex, int itemStockId) {
        System.out.println("CheckoutPanel notified of removal request: Row=" + modelRowIndex + ", ItemStockID=" + itemStockId);
        // Remove the item from local stock cache first
        currentStockCache.remove(itemStockId);
        // Tell table component to remove the row from its model
        tableComponent.removeRowByIndex(modelRowIndex);
        // Update total and receipt preview
        updateCheckoutTotal();
        receiptComponent.updateReceipt(tableComponent.getTableModel(), -1);
    }

    // --- Core Logic Methods ---

    /**
     * Recalculates the total price based on items in the table component's model
     * and updates the totalLabel.
     */
    private void updateCheckoutTotal() {
        if (totalLabel == null || tableComponent == null || tableComponent.getTableModel() == null) {
            System.err.println("Cannot update total: Required components/model are null.");
            return;
        }
        DefaultTableModel model = tableComponent.getTableModel();
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                Object quantityObj = model.getValueAt(i, CheckoutTableComponent.COL_QUANTITY);
                Object priceObj = model.getValueAt(i, CheckoutTableComponent.COL_PRICE);
                if (quantityObj instanceof Integer && priceObj instanceof BigDecimal) {
                    int quantity = (Integer) quantityObj;
                    BigDecimal price = (BigDecimal) priceObj;
                    if (quantity > 0) total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
                } else { System.err.println("Data type error in total calc row " + i); }
            } catch (Exception e) { System.err.println("Error calculating total row " + i + ": " + e); e.printStackTrace(); }
        }
        totalLabel.setText(CURRENCY_FORMAT.format(total));
        System.out.println("Checkout total updated: " + CURRENCY_FORMAT.format(total));
    }


    /**
     * Orchestrates the order finalization process using CheckoutService.
     */
    /**
     * Orchestrates the order finalization process by preparing data and calling CheckoutService.
     */
    private void finalizeOrder() {
        System.out.println("Finalize button clicked.");
        // Ensure table component and its model exist
        if (tableComponent == null || tableComponent.getTableModel() == null) {
             JOptionPane.showMessageDialog(this, "Checkout table component is not ready.", "Internal Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        DefaultTableModel model = tableComponent.getTableModel();

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cannot finalize an empty order.", "Empty Order", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Finalize order?\nTotal: " + totalLabel.getText(), "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) { System.out.println("Finalization cancelled."); return; }

        // 1. Prepare data for the service
        List<OrderLineItemData> orderItems = new ArrayList<>();
        BigDecimal calculatedTotal = BigDecimal.ZERO; // Calculate total accurately here

        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                 // Retrieve data including hidden itemStockId and base Item ID
                 String productNameFromTable = (String) model.getValueAt(i, CheckoutTableComponent.COL_PRODUCT_NAME);
                 Integer itemId = (Integer) model.getValueAt(i, CheckoutTableComponent.COL_ITEM_ID_DISPLAY);
                 Integer quantity = (Integer) model.getValueAt(i, CheckoutTableComponent.COL_QUANTITY);
                 BigDecimal price = (BigDecimal) model.getValueAt(i, CheckoutTableComponent.COL_PRICE);
                 Integer itemStockId = (Integer) model.getValueAt(i, CheckoutTableComponent.HIDDEN_COL_ITEM_STOCK_ID);

                 // Validation
                 if(itemStockId == null || itemId == null || productNameFromTable == null || quantity == null || price == null || quantity <= 0) {
                      JOptionPane.showMessageDialog(this, "Invalid data in checkout row " + (i+1), "Data Error", JOptionPane.ERROR_MESSAGE); return;
                 }

                 // Extract just item name if needed (for DTO consistency)
                 String itemNameForDTO = productNameFromTable;
                 int parenIndex = productNameFromTable.indexOf('(');
                 if (parenIndex > 0) { itemNameForDTO = productNameFromTable.substring(0, parenIndex).trim(); }

                 // Create the DTO for the service
                 orderItems.add(new OrderLineItemData(itemStockId, itemId, itemNameForDTO, quantity, price));

                 // Calculate running total based on table data
                 calculatedTotal = calculatedTotal.add(price.multiply(BigDecimal.valueOf(quantity)));

            } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
                 JOptionPane.showMessageDialog(this, "Internal data error processing checkout table row " + (i+1) + ".", "Error", JOptionPane.ERROR_MESSAGE);
                 e.printStackTrace(); return;
            }
        } // End loop

        // --- Prepare other parameters for the service ---
        Integer customerId = null; // Placeholder for walk-in, TODO: Implement customer selection
        int employeeId = 1;        // !!! TODO: Get actual logged-in employee ID !!!

        // 2. Call the service - Pass the list, calculated total, customer/employee IDs
        CheckoutService checkoutService = new CheckoutService();
        try {
            // Call the updated service method (removed payment/VAT/pure cost details)
            int generatedOrderId = checkoutService.finalizeOrder(
                orderItems,
                calculatedTotal, // Pass the calculated total (service might use this for sales table)
                customerId,
                employeeId
            );

            // 3. Handle Success (UI updates)
            JOptionPane.showMessageDialog(this, "Order #" + generatedOrderId + " finalized!", "Complete", JOptionPane.INFORMATION_MESSAGE);
            // Update receipt preview with final ID
            receiptComponent.updateReceipt(model, generatedOrderId);
            int printConfirm = JOptionPane.showConfirmDialog(this, "Print receipt?", "Print", JOptionPane.YES_NO_OPTION);
            if (printConfirm == JOptionPane.YES_OPTION) {
                // Ask Receipt component to print, passing necessary data
                receiptComponent.printReceipt(model, generatedOrderId);
            }
            clearCheckoutState(); // Reset for next order
            // TODO: Consider refreshing Order History / Transaction History

        } catch (InsufficientStockException e) {
            JOptionPane.showMessageDialog(this, "Order failed: " + e.getMessage(), "Stock Unavailable", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Order failed (Database Error):\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Log DB errors
        } catch (IllegalArgumentException e) {
             JOptionPane.showMessageDialog(this, "Order failed (Input Error): " + e.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) { // Catch any other unexpected errors
             JOptionPane.showMessageDialog(this, "An unexpected error occurred during finalization:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
    }

    /**
     * Clears the checkout state by resetting components.
     */
    private void clearCheckoutState() {
         if (tableComponent != null) tableComponent.clearTable();
         if (totalLabel != null) totalLabel.setText(CURRENCY_FORMAT.format(0.00));
         if (receiptComponent != null) receiptComponent.updateReceipt(null, -1);
         if (searchComponent != null) searchComponent.clearSearchField();
         currentStockCache.clear();
         System.out.println("Checkout state cleared.");
     }

    /** Provide getter for table model if needed by receipt component's button */
    public DefaultTableModel getCheckoutTableModel() {
        return tableComponent != null ? tableComponent.getTableModel() : null;
    }

    // --- Helper: Style Icon Button / Label ---
    private void styleIconButtonLabel(JLabel label) {
         label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
         label.setToolTipText("Search");
         label.setBorder(new EmptyBorder(0, 0, 0, 3));
    }

} // End CheckoutPanel