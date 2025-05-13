package com.github.hanzm_10.murico.swingapp.scenes.inventory;

// --- Imports ---
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
// import java.net.URL; // No longer needed directly here for icon loading
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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
// import com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.*;

// Other imports
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

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

    public InventoryScene() {
        super(new BorderLayout(0, 10)); // Main layout with vertical gap
        System.out.println("InventoryScene instance created.");
    }

    // --- Scene Interface Implementation --- (Keep as is)
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
        refreshTableData();
        if (searchField != null) searchField.setText("");
        if (sorter != null) sorter.setRowFilter(null);
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

 // Inside InventoryScene.java

    private void initializeInventoryUI() {
        System.out.println(getSceneName() + ": Initializing UI components...");
        this.setBackground(Color.WHITE); // Background for the entire scene panel

        // --- 1. Create the Scene Header Panel (Title and Bell grouped to the right) ---
        JPanel sceneHeaderPanel = new JPanel(new BorderLayout(10, 0)); // Main layout for this header strip
        sceneHeaderPanel.setBorder(new EmptyBorder(10, 15, 5, 15)); // Padding for this header
        sceneHeaderPanel.setOpaque(false);

        // Create a panel to hold the title and bell, aligned to the right
        JPanel titleAndBellPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // Align components to the right, 5px horizontal gap
        titleAndBellPanel.setOpaque(false); // Make this inner panel transparent

        JLabel bellLabel = createIconLabelWithAssetManager("/icons/bell_icon.svg", "Notifications", 26, 26);
        // Add some left margin to the bell icon if needed for spacing from title
        // bellLabel.setBorder(new EmptyBorder(0, 5, 0, 0)); // e.g., 5px left margin
        titleAndBellPanel.add(bellLabel); // Add bell icon next to the title
        
        JLabel lblInventoryTitle = new JLabel("INVENTORY");
        lblInventoryTitle.setFont(new Font("Montserrat ExtraBold", Font.BOLD, 28));
        // lblInventoryTitle.setForeground(Color.DARK_GRAY); // Optional: set color
        titleAndBellPanel.add(lblInventoryTitle); // Add title first

        // Add the titleAndBellPanel to the EAST (right side) of the sceneHeaderPanel
        sceneHeaderPanel.add(titleAndBellPanel, BorderLayout.EAST);
        // If you want something on the far left of this sceneHeaderPanel (e.g., a logo), add it to BorderLayout.WEST

        // Add this new sceneHeaderPanel to the NORTH of the InventoryScene itself
        this.add(sceneHeaderPanel, BorderLayout.NORTH);


        // --- 2. Create the Main Content Panel (will hold teal bar and table) ---
        JPanel mainContentPanel = new JPanel(new BorderLayout(0,0));
        mainContentPanel.setOpaque(false);

        // --- 2a. Create the Teal Top Bar (Add, Filter, Search) ---
        JPanel tealTopBarPanel = createTealTopBar();
        mainContentPanel.add(tealTopBarPanel, BorderLayout.NORTH);

        // --- 2b. Create the Table with ScrollPane ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        scrollPane.setBackground(Color.WHITE);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        inventoryTable = createInventoryTable();
        scrollPane.setViewportView(inventoryTable);

        setupTableRenderersAndEditors();

        // Add the mainContentPanel to the CENTER of the InventoryScene
        this.add(mainContentPanel, BorderLayout.CENTER);
    }

    // ... (rest of InventoryScene.java remains the same: createTealTopBar, createInventoryTable, etc.)
    private JPanel createTealTopBar() {
        JPanel colorTopBarPanel = new JPanel(new BorderLayout(10, 0));
        colorTopBarPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        colorTopBarPanel.setBackground(new Color(0x337E8F)); // Darker Teal, adjust as needed

        // Use the version of createIconButtonWithAssetManager that takes width & height
        // and applies transparency/no border
        addButton = createStyledIconButton("/icons/add_button.svg", "Add New Item", 24, 24);
        addButton.addActionListener(e -> openAddItemDialog());
        JPanel addBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        addBtnPanel.setOpaque(false);
        addBtnPanel.add(addButton);
        colorTopBarPanel.add(addBtnPanel, BorderLayout.WEST);

        JPanel rightActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActionPanel.setOpaque(false);
        filterButton = createStyledIconButton("/icons/filter_icon.svg", "Filter Options", 22, 22);
        // TODO: Add ActionListener for filterButton
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
        // Optional: Keep search icon label if you like the look
        // JLabel searchIconLabel = createIconLabelWithAssetManager("/icons/search_icon.svg", "Search", 20, 20);
        // rightActionPanel.add(searchIconLabel);
        colorTopBarPanel.add(rightActionPanel, BorderLayout.EAST);

        return colorTopBarPanel;
    }

    private JTable createInventoryTable() {
        // ... (This method remains the same as in the previous "full revised InventoryScene" response) ...
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
        // ... (This method remains the same as in the previous "full revised InventoryScene" response) ...
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
        // ... (This method remains the same) ...
         if (table != null && table.getColumnCount() > columnIndex && columnIndex >= 0) {
             TableColumnModel tcm = table.getColumnModel();
             tcm.getColumn(columnIndex).setMinWidth(0);
             tcm.getColumn(columnIndex).setMaxWidth(0);
             tcm.getColumn(columnIndex).setWidth(0);
             tcm.getColumn(columnIndex).setPreferredWidth(0);
         } else {
             System.err.println("Warning: Could not hide column at index " + columnIndex + ". Table column count: " + (table != null ? table.getColumnCount() : "null table"));
         }
     }

    public void refreshTableData() {
        // ... (This method remains the same) ...
        System.out.println(getSceneName() + ": Refreshing table data...");
        if (inventoryTable != null) {
            populateInventoryTable();
        } else {
            System.err.println("Inventory table is null, cannot refresh.");
        }
    }

    private void performSearch() {
        // ... (This method remains the same) ...
        if (searchField == null || sorter == null) return;
        String searchText = searchField.getText();
        try {
             List<RowFilter<Object, Object>> filters = new ArrayList<>();
             if (!searchText.trim().isEmpty()) {
                 String regex = "(?i)" + searchText;
                 filters.add(RowFilter.regexFilter(regex, COL_PRODUCT_NAME));
                 filters.add(RowFilter.regexFilter(regex, COL_ITEM_ID));
                 filters.add(RowFilter.regexFilter(regex, COL_CATEGORY));
                 filters.add(RowFilter.regexFilter(regex, COL_PACK_TYPE));
                 filters.add(RowFilter.regexFilter(regex, COL_SUPPLIER));
                 sorter.setRowFilter(RowFilter.orFilter(filters));
             } else {
                 sorter.setRowFilter(null);
             }
        } catch (PatternSyntaxException e) {
            System.err.println("Invalid regex pattern: " + e.getMessage());
            sorter.setRowFilter(null);
        }
    }

    private void openAddItemDialog() {
        // ... (This method remains the same) ...
        Window owner = SwingUtilities.getWindowAncestor(this);
        com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.AddItemDialog dialog =
            new com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.AddItemDialog(owner, this);
        dialog.setVisible(true);
    }

    public void openEditItemDialog(int viewRow) {
        // ... (This method remains the same) ...
        if (inventoryTable == null) return;
        int modelRow = inventoryTable.convertRowIndexToModel(viewRow);
        TableModel model = inventoryTable.getModel();

        try {
            String productName = (String) model.getValueAt(modelRow, COL_PRODUCT_NAME);
            StockInfo stockInfo = (StockInfo) model.getValueAt(modelRow, COL_STOCK_LEVEL);
            BigDecimal unitPrice = (BigDecimal) model.getValueAt(modelRow, COL_UNIT_PRICE);
            int itemStockId = (Integer) model.getValueAt(modelRow, HIDDEN_COL_ITEM_STOCK_ID);

            System.out.println("Editing Item Stock ID: " + itemStockId + ", Product: " + productName);

             Window owner = SwingUtilities.getWindowAncestor(this);
             com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.EditItemStockDialog dialog =
                 new com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.EditItemStockDialog(
                     owner, this, itemStockId, productName,
                     stockInfo.getMinimumQuantity(), unitPrice, stockInfo.getQuantity());
             dialog.setVisible(true);

        } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving item data for editing.\n" + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateInventoryTable() {
        // ... (This method remains the same as in the previous "full revised InventoryScene" response, with the corrected SQL JOIN) ...
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
            LEFT JOIN packagings p ON ist._packaging_id = p._packaging_id -- Corrected JOIN
            LEFT JOIN item_categories_items ici ON i._item_id = ici._item_id
            LEFT JOIN item_categories ic ON ici._item_category_id = ic._item_category_id
            LEFT JOIN suppliers_items si ON i._item_id = si._item_id
            LEFT JOIN suppliers s ON si._supplier_id = s._supplier_id
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
                row.add(new StockInfo(rs.getInt("quantity"), rs.getInt("minimum_quantity")));
                row.add(rs.getBigDecimal("unit_price"));
                row.add("...");
                row.add(rs.getInt("_item_stock_id"));
                inventoryTableModel.addRow(row);
            }
            System.out.println(getSceneName() + ": Table populated with " + inventoryTableModel.getRowCount() + " rows.");
            performSearch();
        } catch (SQLException e) {
             System.err.println("SQL Error fetching inventory data: " + e.getMessage());
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "An error occurred while fetching inventory data:\n" + e.getMessage(),
                    "Database Query Error", JOptionPane.ERROR_MESSAGE);
             inventoryTableModel.setRowCount(0);
             Vector<Object> errorRow = new Vector<>();
             for (int i = 0; i < inventoryTableModel.getColumnCount(); i++) {
                 errorRow.add((i == 0) ? "Error Loading Data" : (i == HIDDEN_COL_ITEM_STOCK_ID ? -1 : ""));
             }
             inventoryTableModel.addRow(errorRow);
        }
    }

    // Renamed to createStyledIconButton to distinguish from a generic one
    private JButton createStyledIconButton(String svgPath, String toolTip, int width, int height) {
        JButton button = new JButton();
        try {
            // Assuming AssetManager.getOrLoadIcon is updated to take width and height
            ImageIcon icon = AssetManager.getOrLoadIcon(svgPath);
            if (icon != null) {
                button.setIcon(icon);
            } else {
                System.err.println("AssetManager: Icon not found or failed to load: " + svgPath + ". Using text fallback.");
                button.setText(toolTip.length() > 1 ? toolTip.substring(0, 1) : toolTip);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon via AssetManager: " + svgPath + " - " + e.getMessage());
            e.printStackTrace();
            button.setText(toolTip.length() > 1 ? toolTip.substring(0, 1) : toolTip);
        }
        button.setToolTipText(toolTip);
        button.setBorderPainted(false);     // Your preference: no border
        button.setContentAreaFilled(false); // Your preference: transparent background
        button.setFocusPainted(false);
        button.setOpaque(false);            // Also helps with transparency
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Renamed to createStyledIconLabel for consistency if needed
    private JLabel createIconLabelWithAssetManager(String svgPath, String toolTip, int width, int height) {
        JLabel label = new JLabel();
        try {
            ImageIcon icon = AssetManager.getOrLoadIcon(svgPath);
            if (icon != null) {
                label.setIcon(icon);
            } else {
                System.err.println("AssetManager: Icon not found or failed to load for label: " + svgPath);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon for label via AssetManager: " + svgPath + " - " + e.getMessage());
            e.printStackTrace();
        }
        label.setToolTipText(toolTip);
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }

    // Database modification method (example)
    public boolean updateItemStockDetails(int itemStockId, BigDecimal newPrice, int newMinQuantity) {
         // ... (This method remains the same) ...
         String sql = "UPDATE item_stocks SET price_php = ?, minimum_quantity = ? WHERE _item_stock_id = ?";
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
}