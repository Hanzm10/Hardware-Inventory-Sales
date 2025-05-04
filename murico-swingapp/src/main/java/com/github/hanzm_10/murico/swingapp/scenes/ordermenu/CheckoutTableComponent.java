package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

// --- Necessary Imports ---
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao; // Use this for DB

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection; // Needed for stock lookup
import java.sql.PreparedStatement; // Needed for stock lookup
import java.sql.ResultSet; // Needed for stock lookup
import java.sql.SQLException; // Needed for stock lookup
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;
// --- End Imports ---

// Listener to notify when quantity in the table changes
interface QuantityChangeListener {
    // Pass itemStockId instead of itemId for precise stock reference
    void quantityChanged(int modelRowIndex, int newQuantity, int itemStockId);
}

// Listener to notify when the remove button for a row is clicked
interface RemoveItemListener {
    // Pass itemStockId instead of itemId
    void itemRemovalRequested(int modelRowIndex, int itemStockId);
}

public class CheckoutTableComponent extends JPanel {

    private JTable checkoutTable;
    private DefaultTableModel checkoutTableModel;

    // Dependencies passed from parent
    // Stock cache might still be useful, but keyed by itemStockId now
    private final Map<Integer, Integer> currentStockCache;
    private final QuantityChangeListener quantityChangeListener;
    private final RemoveItemListener removeItemListener;

    // Column indices constants for VISIBLE columns
    public static final int COL_PRODUCT_NAME = 0;
    public static final int COL_ITEM_ID_DISPLAY = 1; // Item ID (for display if needed, might remove)
    public static final int COL_QUANTITY = 2;
    public static final int COL_PRICE = 3;
    public static final int COL_ACTION_REMOVE = 4;
    // Index for the *hidden* column holding the crucial item_stock_id
    static final int HIDDEN_COL_ITEM_STOCK_ID = 5;

    /**
     * Creates the Checkout Table Component.
     * @param stockCache Reference map holding current stock levels (keyed by itemStockId).
     * @param qtyListener Listener to notify when quantity changes.
     * @param removeListener Listener to notify when removal is requested.
     */
    public CheckoutTableComponent(Map<Integer, Integer> stockCache,
                                  QuantityChangeListener qtyListener,
                                  RemoveItemListener removeListener) {
        this.currentStockCache = stockCache; // Store reference
        this.quantityChangeListener = qtyListener;
        this.removeItemListener = removeListener;

        // Basic validation of listeners
        if (qtyListener == null || removeListener == null || stockCache == null) {
             throw new IllegalArgumentException("Listeners and stock cache cannot be null.");
        }

        setLayout(new BorderLayout()); // Use BorderLayout to hold the JScrollPane
        initializeTableUI();
    }

    /**
     * Sets up the JTable, TableModel, renderers, and editors.
     */
    private void initializeTableUI() {
        // Define VISIBLE columns first
        String[] visibleCols = {"Product Name", "Item ID", "Quantity", "Price", "Action"};
        checkoutTableModel = new DefaultTableModel(visibleCols, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == COL_QUANTITY || column == COL_ACTION_REMOVE;
            }
            @Override public Class<?> getColumnClass(int columnIndex) {
                 switch (columnIndex) {
                     case COL_ITEM_ID_DISPLAY: return Integer.class; // item_id for display
                     case COL_QUANTITY: return Integer.class;
                     case COL_PRICE: return BigDecimal.class;
                     case COL_ACTION_REMOVE: return JButton.class;
                     default: return String.class; // Product Name
                 }
            }
        };

        checkoutTable = new JTable(checkoutTableModel);
        checkoutTable.setFillsViewportHeight(true);
        checkoutTable.setRowHeight(30);
        checkoutTable.getTableHeader().setFont(new Font("Montserrat Bold", Font.BOLD, 12));
        checkoutTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        checkoutTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        // Make the table itself non-focusable if desired, focus goes to editors
        // checkoutTable.setFocusable(false);

        // --- Add the HIDDEN column for item_stock_id to the model ---
        // This column won't be visible in the table UI but holds the critical ID.
        checkoutTableModel.addColumn("ItemStockID");

        // --- Hide the ItemStockID column ---
        TableColumnModel columnModel = checkoutTable.getColumnModel();
        TableColumn itemStockIdColumn = columnModel.getColumn(HIDDEN_COL_ITEM_STOCK_ID);
        // Remove from view (preferred method)
        columnModel.removeColumn(itemStockIdColumn);
        // Alternative methods (less clean):
        // itemStockIdColumn.setMinWidth(0);
        // itemStockIdColumn.setMaxWidth(0);
        // itemStockIdColumn.setPreferredWidth(0);

        // --- Set Custom Editors/Renderers for VISIBLE columns ---
        int maxStockDefault = 999;
        checkoutTable.getColumnModel().getColumn(COL_QUANTITY).setCellEditor(
            new SpinnerCellEditor(1, maxStockDefault, this.currentStockCache, this.quantityChangeListener) // Pass cache & listener
        );
        checkoutTable.getColumnModel().getColumn(COL_ACTION_REMOVE).setCellRenderer(
            new ButtonRenderer("Remove")
        );
        checkoutTable.getColumnModel().getColumn(COL_ACTION_REMOVE).setCellEditor(
            new ButtonEditor(new JCheckBox(), "Remove", this.removeItemListener) // Pass listener
        );

        // --- Adjust VISIBLE Column Widths ---
        columnModel.getColumn(COL_PRODUCT_NAME).setPreferredWidth(200);
        columnModel.getColumn(COL_ITEM_ID_DISPLAY).setMaxWidth(80); // Display item ID might be narrower
        TableColumn qtyCol = columnModel.getColumn(COL_QUANTITY);
        qtyCol.setMinWidth(60); qtyCol.setPreferredWidth(60); qtyCol.setMaxWidth(80);
        columnModel.getColumn(COL_PRICE).setPreferredWidth(80);
        TableColumn removeCol = columnModel.getColumn(COL_ACTION_REMOVE);
        removeCol.setMinWidth(80); removeCol.setPreferredWidth(80); removeCol.setMaxWidth(90);

        // Add table to scroll pane and scroll pane to this panel
        JScrollPane scrollPane = new JScrollPane(checkoutTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    // --- Public Methods for Interaction ---

    /**
     * Gets the underlying table model.
     * @return The DefaultTableModel used by the checkout table.
     */
    public DefaultTableModel getTableModel() {
        return checkoutTableModel;
    }

    /**
     * Adds a new item row to the checkout table.
     * The vector MUST include the hidden itemStockId at the correct index.
     * @param rowData Vector containing data for the new row
     *                (Name, DisplayItemID, Qty, Price, ActionText, HiddenItemStockID).
     */
    public void addRow(Vector<Object> rowData) {
        if (checkoutTableModel != null && rowData != null && rowData.size() > HIDDEN_COL_ITEM_STOCK_ID) {
            // Ensure the hidden ID is present before adding
             if(rowData.get(HIDDEN_COL_ITEM_STOCK_ID) == null || !(rowData.get(HIDDEN_COL_ITEM_STOCK_ID) instanceof Integer)) {
                  System.err.println("CheckoutTableComponent Error: Attempted to add row without valid ItemStockID!");
                  JOptionPane.showMessageDialog(this, "Internal error adding item.", "Error", JOptionPane.ERROR_MESSAGE);
                  return;
             }
            checkoutTableModel.addRow(rowData);
            System.out.println("Row added to checkout table. StockID: " + rowData.get(HIDDEN_COL_ITEM_STOCK_ID));
        } else {
            System.err.println("CheckoutTableComponent Error: Cannot add row - model null or rowData invalid.");
        }
    }

    /**
     * Removes a row from the checkout table based on its model index.
     * @param modelRowIndex The index of the row in the model to remove.
     */
    public void removeRowByIndex(int modelRowIndex) {
        if (checkoutTableModel != null && modelRowIndex >= 0 && modelRowIndex < checkoutTableModel.getRowCount()) {
            checkoutTableModel.removeRow(modelRowIndex);
        } else if (checkoutTableModel != null) {
             System.err.println("CheckoutTableComponent: Attempted to remove invalid row index: " + modelRowIndex + " (row count: " + checkoutTableModel.getRowCount() + ")");
        }
    }

    /**
     * Clears all items from the checkout table.
     */
    public void clearTable() {
        if (checkoutTableModel != null) {
            checkoutTableModel.setRowCount(0);
            System.out.println("Checkout table cleared.");
            // Note: Doesn't clear the external stock cache, parent panel should do that
        }
    }


    // --- Inner Class for JSpinner Cell Editor ---
    class SpinnerCellEditor extends AbstractCellEditor implements TableCellEditor {
        final JSpinner spinner;
        int originalValue;
        int itemStockId; // Use item_stock_id now
        int maxStock = 999;
        final Map<Integer, Integer> stockCache;
        final QuantityChangeListener qtyListener;

        SpinnerCellEditor(int min, int max, Map<Integer, Integer> cache, QuantityChangeListener listener) {
            this.stockCache = cache;
            this.qtyListener = listener;
            spinner = new JSpinner(new SpinnerNumberModel(min, min, max, 1));
            // Optional styling...
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Set initial value safely
            int initialValue = 1; /* ... safe casting ... */
             if (value instanceof Integer) initialValue = (Integer) value; else if (value instanceof Number) initialValue = ((Number)value).intValue();
            spinner.setValue(initialValue); originalValue = initialValue;

            // Determine Max Stock using item_stock_id from hidden column
            itemStockId = -1; maxStock = 999;
            int modelRow = table.convertRowIndexToModel(row);
            SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
            model.setMinimum(1); // Always allow at least 1

            try {
                Object idObj = table.getModel().getValueAt(modelRow, HIDDEN_COL_ITEM_STOCK_ID); // Get from HIDDEN column
                if (idObj instanceof Integer) {
                    itemStockId = (Integer) idObj;
                    // Get max stock from cache using itemStockId
                    maxStock = stockCache.getOrDefault(itemStockId, 0);

                    if (!stockCache.containsKey(itemStockId)) {
                        System.err.println("Spinner Warning: Stock for ItemStockID " + itemStockId + " not cached! Querying DB...");
                        // --- Optional: Query DB if not in cache (can be slow) ---
                        maxStock = queryStockForItemStockId(itemStockId);
                        stockCache.put(itemStockId, maxStock); // Cache the result
                        // --------------------------------------------------------
                        if (!stockCache.containsKey(itemStockId)) { // Still not found after query?
                             System.err.println("Spinner Error: Could not determine stock for ItemStockID " + itemStockId + ". Using default max.");
                             maxStock = 999; // Fallback max if query fails too
                        }
                    }
                     System.out.println("SpinnerCellEditor: ItemStockID=" + itemStockId + ", Max Stock=" + maxStock);

                } else {
                     System.err.println("Spinner Error: Invalid ItemStockID type at modelRow " + modelRow);
                }

            } catch (Exception e) {
                 System.err.println("Error getting ItemStockID/stock for spinner: " + e); e.printStackTrace();
            }

            // Configure Spinner Bounds
            int effectiveMax = Math.max(1, Math.max(originalValue, maxStock));
            if (maxStock <= 0 && stockCache.containsKey(itemStockId)) {
                 effectiveMax = Math.max(1, originalValue); // Allow decreasing if > 0, else 1
            }
            model.setMaximum(effectiveMax);

            // Optional focus handling...
             SwingUtilities.invokeLater(() -> { Component ed=spinner.getEditor();if(ed instanceof JSpinner.DefaultEditor){JFormattedTextField ftf=((JSpinner.DefaultEditor)ed).getTextField();ftf.requestFocusInWindow();ftf.selectAll();}});

            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            // Return capped value
             Object spinnerValue = spinner.getValue(); int currentValue = 1; if (spinnerValue instanceof Integer) currentValue = (Integer) spinnerValue; else if (spinnerValue instanceof Number) currentValue = ((Number)spinnerValue).intValue();
             SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel(); int currentMax = 999; if (model.getMaximum() instanceof Integer) currentMax = (Integer)model.getMaximum(); else if (model.getMaximum() instanceof Number) currentMax = ((Number)model.getMaximum()).intValue();
             int finalValue = Math.max(1, Math.min(currentValue, currentMax));
             return finalValue;
        }

        @Override
        public boolean stopCellEditing() {
            try {
                spinner.commitEdit(); // Validate
                int newValue = (Integer) getCellEditorValue(); // Get final capped value

                if (newValue != originalValue) {
                     System.out.println("Spinner value changed from " + originalValue + " to " + newValue + " for itemStockId " + itemStockId);
                     if (qtyListener != null && itemStockId != -1) { // Check listener and valid ID
                          // Find the corresponding model row index again if needed (might have changed if table updates during edit)
                          int modelRow = -1;
                          for(int i=0; i<checkoutTableModel.getRowCount(); i++){
                               if(itemStockId == (Integer) checkoutTableModel.getValueAt(i, HIDDEN_COL_ITEM_STOCK_ID)){
                                    modelRow = i; break;
                               }
                          }
                          if(modelRow != -1) {
                               qtyListener.quantityChanged(modelRow, newValue, itemStockId); // Notify listener
                          } else {
                               System.err.println("Spinner Error: Could not find model row for itemStockId " + itemStockId + " after editing.");
                          }
                     }
                }
            } catch (java.text.ParseException e) { Toolkit.getDefaultToolkit().beep(); return false; } // Invalid input
            return super.stopCellEditing();
        }

        // Helper to query stock if not found in cache (use carefully, can impact performance)
        private int queryStockForItemStockId(int stockId) {
            String sql = "SELECT quantity FROM item_stocks WHERE _item_stock_id = ?";
            int stock = 0; // Default to 0 if not found or error
             System.out.println("Querying stock for item_stock_id: " + stockId);
             try (Connection conn = MySqlFactoryDao.createConnection(); // Use correct factory
                  PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, stockId);
                 try (ResultSet rs = pstmt.executeQuery()) {
                     if (rs.next()) {
                         stock = rs.getInt("quantity");
                     } else {
                          System.err.println("No stock record found for item_stock_id: " + stockId);
                     }
                 }
             } catch (SQLException e) {
                 System.err.println("Database error querying stock for item_stock_id " + stockId + ": " + e.getMessage());
                 e.printStackTrace();
             }
             return stock;
        }

    } // --- End SpinnerCellEditor ---


    // --- Inner Classes for JButton Cell Renderer and Editor ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) { setOpaque(true); setFocusPainted(false); setBorderPainted(false); setText(text); }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c){ return this; }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int editingModelRow;
        private int editingItemStockId; // Use item_stock_id now
        private final RemoveItemListener removeListener;

        public ButtonEditor(JCheckBox checkBox, String text, RemoveItemListener listener) {
            super(checkBox);
            this.label = text;
            this.removeListener = listener;
            button = new JButton(text);
            button.setOpaque(true); button.setFocusPainted(false); button.setBorderPainted(false);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editingModelRow = table.convertRowIndexToModel(row);
            editingItemStockId = -1; // Reset
            // Get item_stock_id from the hidden column
            try {
                 Object idObj = table.getModel().getValueAt(editingModelRow, HIDDEN_COL_ITEM_STOCK_ID);
                 if (idObj instanceof Integer) {
                     editingItemStockId = (Integer) idObj;
                 } else {
                      System.err.println("ButtonEditor Error: Invalid ItemStockID type at modelRow " + editingModelRow);
                 }
            } catch (Exception e) {
                 System.err.println("ButtonEditor Error: Could not get itemStockId for row " + editingModelRow + ": " + e);
                 e.printStackTrace();
            }
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed && removeListener != null && editingModelRow != -1 && editingItemStockId != -1) {
                 System.out.println("Remove button action triggered for model row: " + editingModelRow + ", ItemStockID: " + editingItemStockId);
                 // Notify listener with itemStockId
                 final int row = editingModelRow; // Need final vars for lambda
                 final int stockId = editingItemStockId;
                 SwingUtilities.invokeLater(() -> removeListener.itemRemovalRequested(row, stockId));
            }
            isPushed = false;
            return label;
        }

        @Override public boolean stopCellEditing() { isPushed = false; return super.stopCellEditing(); }
        @Override protected void fireEditingStopped() { super.fireEditingStopped(); }

    } // --- End ButtonEditor ---

} // End CheckoutTableComponent