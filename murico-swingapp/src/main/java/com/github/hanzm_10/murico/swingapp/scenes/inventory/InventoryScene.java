package com.github.hanzm_10.murico.swingapp.scenes.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
// import java.awt.event.ActionEvent; // Not strictly needed if using lambdas
// import java.awt.event.ActionListener; // Not strictly needed if using lambdas
import java.math.BigDecimal;
import java.net.URISyntaxException;
// import java.net.URL; // No longer needed directly here for icon loading
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern; // Import for Pattern.quote
import java.util.regex.PatternSyntaxException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

// Import your AssetManager
import com.github.hanzm_10.murico.swingapp.assets.AssetManager;

// Local imports for renderers and editors
import com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers.*;
import com.github.hanzm_10.murico.swingapp.scenes.inventory.editors.ButtonEditor;
// Import your new dialogs
import com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.AddItemDialog;
import com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.EditItemStockDialog;
import com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.InventoryFilterDialog;
import com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.RestockItemDialog;


// Other imports
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

// Custom exception for stock issues (ensure this is defined or imported correctly)
// For example, if it's in the same package or a subpackage of service.database:
// import com.github.hanzm_10.murico.swingapp.service.database.InsufficientStockException;
class InsufficientStockException extends Exception { // Placeholder if not already defined
    public InsufficientStockException(String message) {
        super(message);
    }
}


public class InventoryScene extends JPanel implements Scene {

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

    // --- UI Components ---
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JButton filterButton;
    private JButton addButton;

    private boolean uiInitialized = false;

    // Fields to store current filter state
    private String activeCategoryFilter = "ALL";
    private String activeSupplierFilter = "ALL";

    public InventoryScene() {
        super(new BorderLayout(0, 10)); // Main layout with vertical gap
        System.out.println("InventoryScene instance created.");
    }

    // --- Scene Interface Implementation ---
    @Override public String getSceneName() { return "inventory"; }
    @Override public JPanel getSceneView() { return this; }
    @Override public void onBeforeHide() {}
    @Override public void onBeforeShow() {}

    @Override
    public void onCreate() {
        if (!uiInitialized) {
            initializeInventoryUI();
            uiInitialized = true;
        }
    }

    @Override
    public void onShow() {
        System.out.println(getSceneName() + ": onShow");
        if (!uiInitialized) {
            onCreate();
        }
        // On show, clear search and reset filters to "ALL" then refresh
        if (searchField != null) searchField.setText(""); // Clears search text
        this.activeCategoryFilter = "ALL"; // Reset active filters
        this.activeSupplierFilter = "ALL";
        refreshTableData(); // This will populate and then apply the (now cleared) filters
    }

     @Override
    public void onHide() {
        System.out.println(getSceneName() + ": onHide");
    }

    @Override
    public boolean onDestroy() {
        inventoryTable = null;
        inventoryTableModel = null;
        sorter = null;
        searchField = null;
        filterButton = null;
        addButton = null;
        removeAll();
        uiInitialized = false;
        return true;
    }
    // --- End Scene Interface Implementation ---

    private void initializeInventoryUI() {
        System.out.println(getSceneName() + ": Initializing UI components...");
        this.setBackground(Color.WHITE);

        // --- 1. Scene Header Panel (Bell and Title) ---
        JPanel sceneHeaderPanel = new JPanel(new BorderLayout(10, 0));
        sceneHeaderPanel.setBorder(new EmptyBorder(10, 15, 5, 15));
        sceneHeaderPanel.setOpaque(false);

        JPanel titleAndBellPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        titleAndBellPanel.setOpaque(false);
        JLabel bellLabel = createIconLabelWithAssetManager("/icons/bell_icon.svg", "Notifications", 26, 26);
        bellLabel.setBorder(new EmptyBorder(0, 0, 0, 5)); // Add right margin to bell
        titleAndBellPanel.add(bellLabel);
        JLabel lblInventoryTitle = new JLabel("INVENTORY");
        lblInventoryTitle.setFont(new Font("Montserrat ExtraBold", Font.BOLD, 28));
        titleAndBellPanel.add(lblInventoryTitle);
        sceneHeaderPanel.add(titleAndBellPanel, BorderLayout.EAST);
        this.add(sceneHeaderPanel, BorderLayout.NORTH);

        // --- 2. Main Content Panel ---
        JPanel mainContentPanel = new JPanel(new BorderLayout(0,0));
        mainContentPanel.setOpaque(false);

        JPanel tealTopBarPanel = createTealTopBar();
        mainContentPanel.add(tealTopBarPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        scrollPane.setBackground(Color.WHITE);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        inventoryTable = createInventoryTable();
        scrollPane.setViewportView(inventoryTable);
        setupTableRenderersAndEditors();
        this.add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createTealTopBar() {
        JPanel tealTopBarPanel = new JPanel(new BorderLayout(10, 0));
        tealTopBarPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        tealTopBarPanel.setBackground(new Color(0x337E8F));
        

        addButton = createStyledIconButton("/icons/add_button.svg", "Add New Item", 24, 24);

        addButton.setBackground(new Color(0x00,true));
        addButton.setBorder(null);
        addButton.addActionListener(e -> openAddItemDialog());
        JPanel addBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        addBtnPanel.setOpaque(false);
        addBtnPanel.add(addButton);
        tealTopBarPanel.add(addBtnPanel, BorderLayout.WEST);

        JPanel rightActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActionPanel.setOpaque(false);
        filterButton = createStyledIconButton("/icons/filter_icon.svg", "Filter Options", 22, 22);
        filterButton.setBackground(new Color(0x00,true));
        filterButton.setBorder(null);
        filterButton.addActionListener(e -> openFilterDialog()); // Attach listener
        rightActionPanel.add(filterButton);
        searchField = new JTextField(20);
        searchField.setFont(new Font("Montserrat Medium", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(searchField.getPreferredSize().width, 28));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });
        rightActionPanel.add(searchField);
        // JLabel searchIconLabel = createIconLabelWithAssetManager("/icons/search_icon.svg", "Search", 20, 20);
        // rightActionPanel.add(searchIconLabel); // Optional search icon next to field
        tealTopBarPanel.add(rightActionPanel, BorderLayout.EAST);

        return tealTopBarPanel;
    }

    private JTable createInventoryTable() {
        JTable table = new JTable();
        table.setRowHeight(40);
        table.setFont(new Font("Montserrat", Font.PLAIN, 12));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.getTableHeader().setFont(new Font("Montserrat Bold", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(0xF5F5F5));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        String[] columnNames = {"Product Name", "Item ID", "Category", "Pack Type", "Supplier", "Stock Level", "Unit Price", "", "_ItemStockID"};
        inventoryTableModel = new DefaultTableModel(null, columnNames) {
            @Override public boolean isCellEditable(int row, int column) { return column == COL_ACTION; }
            @Override public Class<?> getColumnClass(int i) {
                 switch (i) {
                    case COL_ITEM_ID: return String.class;
                    case COL_PACK_TYPE: return String.class;
                    case COL_STOCK_LEVEL: return StockInfo.class;
                    case COL_UNIT_PRICE: return BigDecimal.class;
                    case COL_ACTION: return JButton.class;
                    case HIDDEN_COL_ITEM_STOCK_ID : return Integer.class;
                    default: return String.class;
                }
            }
        };
        table.setModel(inventoryTableModel);
        sorter = new TableRowSorter<>(inventoryTableModel);
        table.setRowSorter(sorter);
        return table;
    }

     private void setupTableRenderersAndEditors() {
        if (inventoryTable == null) return;
        TableColumnModel columnModel = inventoryTable.getColumnModel();

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
        hideColumn(inventoryTable, HIDDEN_COL_ITEM_STOCK_ID);
    }

    private void hideColumn(JTable table, int columnIndex) {
         if (table != null && table.getColumnCount() > columnIndex && columnIndex >= 0) {
             TableColumnModel tcm = table.getColumnModel();
             tcm.getColumn(columnIndex).setMinWidth(0);
             tcm.getColumn(columnIndex).setMaxWidth(0);
             tcm.getColumn(columnIndex).setWidth(0);
             tcm.getColumn(columnIndex).setPreferredWidth(0);
         } else {
             System.err.println("Warning: Could not hide column at index " + columnIndex);
         }
     }

    public void refreshTableData() {
        System.out.println(getSceneName() + ": Refreshing table data...");
        if (inventoryTable != null && inventoryTableModel != null) { // Added null check for model
            populateInventoryTable();
            applyTableFilters(activeCategoryFilter, activeSupplierFilter);
        } else {
            System.err.println("Inventory table or model is null, cannot refresh.");
        }
    }

    private void performSearch() {
        applyTableFilters(activeCategoryFilter, activeSupplierFilter);
    }

    private void openFilterDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        InventoryFilterDialog dialog = new InventoryFilterDialog(owner, this, activeCategoryFilter, activeSupplierFilter);
        dialog.setVisible(true);
    }

    public void applyTableFilters(String category, String supplier) {
        if (sorter == null) {
            System.err.println("Sorter not initialized. Cannot apply filters.");
            return;
        }
        this.activeCategoryFilter = (category == null || category.trim().isEmpty()) ? "ALL" : category;
        this.activeSupplierFilter = (supplier == null || supplier.trim().isEmpty()) ? "ALL" : supplier;
        System.out.println("Applying filters - Category: " + activeCategoryFilter + ", Supplier: " + activeSupplierFilter);

        List<RowFilter<Object, Object>> combinedFilters = new ArrayList<>();
        if (!"ALL".equalsIgnoreCase(activeCategoryFilter)) {
            combinedFilters.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(activeCategoryFilter) + "$", COL_CATEGORY));
        }
        if (!"ALL".equalsIgnoreCase(activeSupplierFilter)) {
            combinedFilters.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(activeSupplierFilter) + "$", COL_SUPPLIER));
        }

        String searchText = (searchField != null) ? searchField.getText().trim() : "";
        if (!searchText.isEmpty()) {
            try {
                String regex = "(?i)" + Pattern.quote(searchText);
                List<RowFilter<Object,Object>> textSearchORFilters = new ArrayList<>();
                textSearchORFilters.add(RowFilter.regexFilter(regex, COL_PRODUCT_NAME));
                textSearchORFilters.add(RowFilter.regexFilter(regex, COL_ITEM_ID));
                // Add other text-searchable columns as needed (e.g., pack type, category if not exact match)
                combinedFilters.add(RowFilter.orFilter(textSearchORFilters));
            } catch (PatternSyntaxException pse) {
                System.err.println("Search text created invalid regex: " + pse.getMessage());
            }
        }

        if (combinedFilters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(combinedFilters));
        }
    }

    private void openAddItemDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        AddItemDialog dialog = new AddItemDialog(owner, this);
        dialog.setVisible(true);
    }

    public void openEditItemDialog(int viewRow) {
        if (inventoryTable == null || inventoryTableModel == null) return;
        int modelRow = inventoryTable.convertRowIndexToModel(viewRow);
        if (modelRow < 0 || modelRow >= inventoryTableModel.getRowCount()) {
             System.err.println("EditItem: Invalid modelRow ("+modelRow+") from viewRow: " + viewRow);
             return;
        }
        try {
            String productName = (String) inventoryTableModel.getValueAt(modelRow, COL_PRODUCT_NAME);
            StockInfo stockInfo = (StockInfo) inventoryTableModel.getValueAt(modelRow, COL_STOCK_LEVEL);
            BigDecimal unitPrice = (BigDecimal) inventoryTableModel.getValueAt(modelRow, COL_UNIT_PRICE);
            int itemStockId = (Integer) inventoryTableModel.getValueAt(modelRow, HIDDEN_COL_ITEM_STOCK_ID);
            System.out.println("Opening Edit Dialog for Item Stock ID: " + itemStockId);
            Window owner = SwingUtilities.getWindowAncestor(this);
            EditItemStockDialog dialog = new EditItemStockDialog(owner, this, itemStockId, productName,
                     stockInfo.getMinimumQuantity(), unitPrice, stockInfo.getQuantity());
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving item data for editing: " + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openRestockDialog(int modelRow) {
        if (inventoryTable == null || inventoryTableModel == null || modelRow < 0 || modelRow >= inventoryTableModel.getRowCount()) {
            System.err.println("RestockItem: Invalid modelRow: " + modelRow);
            return;
        }
        try {
            int itemStockId = (Integer) inventoryTableModel.getValueAt(modelRow, HIDDEN_COL_ITEM_STOCK_ID);
            String productName = (String) inventoryTableModel.getValueAt(modelRow, COL_PRODUCT_NAME);
            StockInfo stockInfo = (StockInfo) inventoryTableModel.getValueAt(modelRow, COL_STOCK_LEVEL);
            int currentQuantity = stockInfo.getQuantity();
            int coreItemId = stockInfo.getItemId();
            System.out.println("Opening Restock Dialog for Item Stock ID: " + itemStockId + ", Core Item ID: " + coreItemId);
            Window owner = SwingUtilities.getWindowAncestor(this);
            RestockItemDialog dialog = new RestockItemDialog(owner, this, itemStockId, coreItemId, productName, currentQuantity);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error preparing for restock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleDeleteItem(int modelRow) {
         if (inventoryTable == null || inventoryTableModel == null || modelRow < 0 || modelRow >= inventoryTableModel.getRowCount()) {
            System.err.println("ArchiveItem: Invalid modelRow: " + modelRow);
            return;
        }
        try {
            StockInfo stockInfo = (StockInfo) inventoryTableModel.getValueAt(modelRow, COL_STOCK_LEVEL);
            int itemIdToArchive = stockInfo.getItemId();
            String productName = (String) inventoryTableModel.getValueAt(modelRow, COL_PRODUCT_NAME);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to archive the item '" + productName + "' (Item ID: " + itemIdToArchive + ")?\n" +
                    "This will hide the item and all its stock variants from active views.\n" +
                    "It can usually be recovered by an administrator.",
                    "Confirm Archive Item", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Attempting to archive core Item ID: " + itemIdToArchive);
                boolean success = archiveCoreItem(itemIdToArchive);
                if (success) {
                   JOptionPane.showMessageDialog(this, "Item '" + productName + "' and its stock have been archived.", "Success", JOptionPane.INFORMATION_MESSAGE);
                   refreshTableData();
                } else {
                   System.err.println("Archiving failed for core item ID: " + itemIdToArchive + " (parent notification)");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error preparing for item archival: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateInventoryTable() {
        if (inventoryTable == null || inventoryTableModel == null) return;
        inventoryTableModel.setRowCount(0);
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
                inventoryTableModel.addRow(row);
            }
            System.out.println(getSceneName() + ": Table populated with " + inventoryTableModel.getRowCount() + " rows (excluding archived).");
            // Do not call performSearch() here, refreshTableData will call applyTableFilters
        } catch (SQLException e) {
             System.err.println("SQL Error fetching inventory data: " + e.getMessage());
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "An error occurred while fetching inventory data:\n" + e.getMessage(), "Database Query Error", JOptionPane.ERROR_MESSAGE);
             inventoryTableModel.setRowCount(0);
             Vector<Object> errorRow = new Vector<>();
             for (int i = 0; i < inventoryTableModel.getColumnCount(); i++) {
                 errorRow.add((i == 0) ? "Error Loading Data" : (i == HIDDEN_COL_ITEM_STOCK_ID ? -1 : ""));
             }
             inventoryTableModel.addRow(errorRow);
        }
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
            System.err.println("Error loading icon via AssetManager: " + svgPath + " - " + e.getMessage());
            e.printStackTrace();
            button.setText(toolTip.length() > 1 ? toolTip.substring(0, 1) : toolTip);
        }
        button.setToolTipText(toolTip);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createIconLabelWithAssetManager(String svgPath, String toolTip, int width, int height) {
        JLabel label = new JLabel();
        try {
            ImageIcon icon = AssetManager.getOrLoadIcon(svgPath);
            if (icon != null) {
                label.setIcon(icon);
            } else {
                System.err.println("AssetManager: Icon not found for label: " + svgPath);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon for label via AssetManager: " + svgPath + " - " + e.getMessage());
            e.printStackTrace();
        }
        label.setToolTipText(toolTip);
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }

    public boolean updateItemStockDetails(int itemStockId, BigDecimal newPrice, int newMinQuantity) {
         String sql = "UPDATE item_stocks SET price_php = ?, minimum_quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE _item_stock_id = ? AND is_deleted = FALSE";
         try (Connection conn = MySqlFactoryDao.createConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setBigDecimal(1, newPrice);
             pstmt.setInt(2, newMinQuantity);
             pstmt.setInt(3, itemStockId);
             int rowsAffected = pstmt.executeUpdate();
             return rowsAffected > 0;
         } catch (SQLException e) {
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "Error updating stock details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
             return false;
         }
     }

    public boolean processRestock(int itemStockId, int quantityToAdd, int quantityBefore) {
        if (quantityToAdd <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity to add must be positive.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
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
                    throw new SQLException("Restock failed: Item stock record not found, not updated, or already deleted. Stock ID: " + itemStockId);
                }
            }
            String insertRestockSql = "INSERT INTO item_restocks (_item_stock_id, quantity_before, quantity_after, quantity_added) VALUES (?, ?, ?, ?)";
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
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            JOptionPane.showMessageDialog(this, "Error processing restock: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); } }
        }
        return success;
    }

    public boolean archiveCoreItem(int itemId) {
        String updateItemSql = "UPDATE items SET is_deleted = TRUE, updated_at = CURRENT_TIMESTAMP WHERE _item_id = ? AND is_deleted = FALSE";
        String updateItemStocksSql = "UPDATE item_stocks SET is_deleted = TRUE, updated_at = CURRENT_TIMESTAMP WHERE _item_id = ? AND is_deleted = FALSE";
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
                    System.out.println("Archived " + stockRowsAffected + " stock variants for item ID: " + itemId);
                }
                conn.commit();
                success = true;
                System.out.println("Successfully archived item ID: " + itemId + " and its stock.");
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Item not found or already archived.", "Archive Not Performed", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Item ID: " + itemId + " not found or already archived. No changes made.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            JOptionPane.showMessageDialog(this, "Error archiving item: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
        return success;
    }
}