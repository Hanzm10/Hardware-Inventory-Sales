package com.github.hanzm_10.murico.swingapp.scenes.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
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

// Local imports (assuming these packages will be created)
import com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers.*;
import com.github.hanzm_10.murico.swingapp.scenes.inventory.editors.ButtonEditor;
// import com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.*; // Import dialogs when created

// Other imports
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;

public class InventoryScene extends JPanel implements Scene {

    // --- Constants for Columns ---
    public static final int COL_PRODUCT_NAME = 0;
    public static final int COL_ITEM_ID = 1;
    public static final int COL_CATEGORY = 2;
    public static final int COL_PACK_TYPE = 3; // NEW COLUMN
    public static final int COL_SUPPLIER = 4;
    public static final int COL_STOCK_LEVEL = 5;
    public static final int COL_UNIT_PRICE = 6;
    public static final int COL_ACTION = 7;
    public static final int HIDDEN_COL_ITEM_STOCK_ID = 8; // Index shifts

    // --- UI Components ---
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JButton filterButton;
    private JButton addButton;

    private boolean uiInitialized = false;

    public InventoryScene() {
        setLayout(new BorderLayout(0, 0));
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


    private void initializeInventoryUI() {
        System.out.println(getSceneName() + ": Initializing UI components...");
        this.setBackground(Color.WHITE);

        JPanel topBarPanel = createTopBar();
        this.add(topBarPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        scrollPane.setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);

        inventoryTable = createInventoryTable();
        scrollPane.setViewportView(inventoryTable);

        setupTableRenderersAndEditors();
    }

    private JPanel createTopBar() {
        JPanel topBarPanel = new JPanel(new BorderLayout(10, 0));
        topBarPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        topBarPanel.setBackground(new Color(0x2E8B57)); // Teal color

        addButton = createIconButton("/icons/add_button.png", "Add New Item");
        addButton.addActionListener(e -> openAddItemDialog());
        topBarPanel.add(addButton, BorderLayout.WEST);

        JPanel centerTitlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        centerTitlePanel.setOpaque(false);
        JLabel bellLabel = createIconLabel("/icons/bell_icon.png", "Notifications");
        centerTitlePanel.add(bellLabel);
        JLabel lblInventoryTitle = new JLabel("INVENTORY");
        lblInventoryTitle.setFont(new Font("Montserrat ExtraBold", Font.BOLD, 24));
        lblInventoryTitle.setForeground(Color.WHITE);
        centerTitlePanel.add(lblInventoryTitle);
        topBarPanel.add(centerTitlePanel, BorderLayout.CENTER);

        JPanel rightActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightActionPanel.setOpaque(false);
        filterButton = createIconButton("/icons/filter_icon.png", "Filter Options");
        // TODO: Add ActionListener for filterButton
        rightActionPanel.add(filterButton);
        searchField = new JTextField(20);
        searchField.setFont(new Font("Montserrat Medium", Font.PLAIN, 12));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });
        rightActionPanel.add(searchField);
        JLabel searchIconLabel = createIconLabel("/icons/search_icon.png", "Search");
        rightActionPanel.add(searchIconLabel);
        topBarPanel.add(rightActionPanel, BorderLayout.EAST);

        return topBarPanel;
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
                    case COL_PACK_TYPE: return String.class; // New column
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
        columnModel.getColumn(COL_PACK_TYPE).setPreferredWidth(100); // New column width
        columnModel.getColumn(COL_SUPPLIER).setPreferredWidth(120);
        columnModel.getColumn(COL_STOCK_LEVEL).setPreferredWidth(180);
        columnModel.getColumn(COL_UNIT_PRICE).setPreferredWidth(100);
        columnModel.getColumn(COL_ACTION).setPreferredWidth(50);
        columnModel.getColumn(COL_ACTION).setMinWidth(50);
        columnModel.getColumn(COL_ACTION).setMaxWidth(50);

        columnModel.getColumn(COL_PRODUCT_NAME).setCellRenderer(new ProductNameRenderer());
        columnModel.getColumn(COL_ITEM_ID).setCellRenderer(new ItemIdRenderer());
        // COL_CATEGORY and COL_PACK_TYPE use default String renderer
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
             System.err.println("Warning: Could not hide column at index " + columnIndex + ". Table column count: " + (table != null ? table.getColumnCount() : "null table"));
         }
     }

    public void refreshTableData() {
        System.out.println(getSceneName() + ": Refreshing table data...");
        if (inventoryTable != null) {
            populateInventoryTable();
        } else {
            System.err.println("Inventory table is null, cannot refresh.");
        }
    }

    private void performSearch() {
        if (searchField == null || sorter == null) return;
        String searchText = searchField.getText();
        try {
             List<RowFilter<Object, Object>> filters = new ArrayList<>();
             if (!searchText.trim().isEmpty()) {
                 String regex = "(?i)" + searchText;
                 filters.add(RowFilter.regexFilter(regex, COL_PRODUCT_NAME));
                 filters.add(RowFilter.regexFilter(regex, COL_ITEM_ID));
                 filters.add(RowFilter.regexFilter(regex, COL_CATEGORY));
                 filters.add(RowFilter.regexFilter(regex, COL_PACK_TYPE)); // Include Pack Type
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
        Window owner = SwingUtilities.getWindowAncestor(this);
        // Ensure fully qualified name or correct import
        com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.AddItemDialog dialog =
            new com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs.AddItemDialog(owner, this);
        dialog.setVisible(true);
    }

    public void openEditItemDialog(int viewRow) {
        if (inventoryTable == null) return;
        int modelRow = inventoryTable.convertRowIndexToModel(viewRow);
        TableModel model = inventoryTable.getModel();

        try {
            String productName = (String) model.getValueAt(modelRow, COL_PRODUCT_NAME);
            // String packType = (String) model.getValueAt(modelRow, COL_PACK_TYPE); // Can retrieve if needed by dialog
            StockInfo stockInfo = (StockInfo) model.getValueAt(modelRow, COL_STOCK_LEVEL);
            BigDecimal unitPrice = (BigDecimal) model.getValueAt(modelRow, COL_UNIT_PRICE);
            int itemStockId = (Integer) model.getValueAt(modelRow, HIDDEN_COL_ITEM_STOCK_ID);

            System.out.println("Editing Item Stock ID: " + itemStockId + ", Product: " + productName);

             Window owner = SwingUtilities.getWindowAncestor(this);
             // Ensure fully qualified name or correct import for dialogs package
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
            LEFT JOIN packagings p ON ist._packaging_id = p._packaging_id -- Corrected JOIN for packagings
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
                row.add(rs.getString("pack_type_name") != null ? rs.getString("pack_type_name") : "N/A"); // Added Pack Type
                row.add(rs.getString("supplier_name") != null ? rs.getString("supplier_name") : "N/A");
                row.add(new StockInfo(rs.getInt("quantity"), rs.getInt("minimum_quantity")));
                row.add(rs.getBigDecimal("unit_price"));
                row.add("..."); // Action button
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

    private JButton createIconButton(String iconPath, String toolTip) {
         JButton button = new JButton();
         try {
             URL iconUrl = getClass().getResource(iconPath);
             if (iconUrl != null) {
                 ImageIcon icon = new ImageIcon(iconUrl);
                  button.setIcon(icon);
             } else {
                  System.err.println("Icon not found: " + iconPath + ". Using text fallback.");
                  button.setText(toolTip.length() > 1 ? toolTip.substring(0, 1) : toolTip);
             }
         } catch (Exception e) {
             System.err.println("Error loading icon: " + iconPath);
             button.setText(toolTip.length() > 1 ? toolTip.substring(0, 1) : toolTip);
         }
         button.setToolTipText(toolTip);
         button.setBorderPainted(false);
         button.setContentAreaFilled(false);
         button.setFocusPainted(false);
         button.setOpaque(false);
         button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
         button.setPreferredSize(new Dimension(30, 30));
         return button;
     }

    private JLabel createIconLabel(String iconPath, String toolTip) {
         JLabel label = new JLabel();
         try {
             URL iconUrl = getClass().getResource(iconPath);
             if (iconUrl != null) {
                 label.setIcon(new ImageIcon(iconUrl));
             } else {
                 System.err.println("Icon not found: " + iconPath);
             }
         } catch (Exception e) {
             System.err.println("Error loading icon: " + iconPath);
         }
         label.setToolTipText(toolTip);
         label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
         label.setPreferredSize(new Dimension(30, 30));
         return label;
     }

    public boolean updateItemStockDetails(int itemStockId, BigDecimal newPrice, int newMinQuantity) {
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
