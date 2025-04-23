import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame; // Needed for Dialog owner
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class InventoryPanel extends JPanel {

    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> categoryFilterComboBox;
    private JButton editItemButton;
    private JTextField searchField;
    private JFrame ownerFrame; // Reference to the main application frame for dialog parenting

    // Store current Branch ID (TODO: Make this dynamic, perhaps passed in constructor)
    private int currentBranchId = 1;

    /**
     * Creates the Inventory Panel.
     * @param owner The main JFrame, used for parenting dialogs.
     */
    public InventoryPanel(JFrame owner) {
        this.ownerFrame = owner;
        setLayout(new BorderLayout(0, 0)); // Main layout for this panel
        initializeInventoryUI();
        // Load initial data
        populateInventoryTable(inventoryTable, currentBranchId, "All Categories");
        populateCategoryFilter();
    }

    private void initializeInventoryUI() {
        // --- Top Bar Section for Inventory (Title + Actions) ---
        JPanel topBarPanel = new JPanel(new BorderLayout(0, 0));
        add(topBarPanel, BorderLayout.NORTH); // Add to this InventoryPanel

        // --- Title ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblInventoryTitle = new JLabel("INVENTORY");
        lblInventoryTitle.setFont(new Font("Montserrat ExtraBold", Font.BOLD, 30));
        titlePanel.add(lblInventoryTitle);
        titlePanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        topBarPanel.add(titlePanel, BorderLayout.NORTH);

        // --- Action Panel ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actionPanel.setBackground(new Color(240, 240, 240));
        topBarPanel.add(actionPanel, BorderLayout.CENTER);

        JButton btnAddItem = new JButton("Add Item (+)");
        btnAddItem.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
        btnAddItem.addActionListener(e -> openAddItemDialog());
        actionPanel.add(btnAddItem);

        editItemButton = new JButton("Edit Item");
        editItemButton.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
        editItemButton.setEnabled(false);
        editItemButton.addActionListener(e -> openEditItemDialog());
        actionPanel.add(editItemButton);

        JLabel filterLabel = new JLabel("Filter by Category:");
        filterLabel.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
        actionPanel.add(filterLabel);

        categoryFilterComboBox = new JComboBox<>();
        categoryFilterComboBox.setFont(new Font("Montserrat SemiBold", Font.BOLD, 12));
        categoryFilterComboBox.setPreferredSize(new Dimension(180, 25));
        categoryFilterComboBox.addItem("Loading Categories...");
        categoryFilterComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoryFilterComboBox.getSelectedItem();
            if (selectedCategory != null && !selectedCategory.contains("Loading") && !selectedCategory.contains("Error")) {
                 populateInventoryTable(inventoryTable, currentBranchId, selectedCategory);
            }
        });
        actionPanel.add(categoryFilterComboBox);

        actionPanel.add(Box.createHorizontalGlue()); // Pushes search field to the right

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField(20);
        searchField.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { performSearch(); }
            @Override public void removeUpdate(DocumentEvent e) { performSearch(); }
            @Override public void changedUpdate(DocumentEvent e) { performSearch(); }
        });
        JLabel searchIcon = new JLabel("\uD83D\uDD0D");
        searchIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        searchIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        searchIcon.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent evt) { performSearch(); }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchIcon, BorderLayout.EAST);
        actionPanel.add(searchPanel);

        // --- Inventory Table Area ---
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER); // Add table scroll pane to this InventoryPanel

        inventoryTable = new JTable();
        // Configure Table
        inventoryTable.setCellSelectionEnabled(false);
        inventoryTable.setRowSelectionAllowed(true);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.setFont(new Font("Montserrat SemiBold", Font.BOLD, 12));
        inventoryTable.setRowHeight(25);
        inventoryTable.getTableHeader().setFont(new Font("Montserrat Bold", Font.BOLD, 12));
        inventoryTable.getTableHeader().setReorderingAllowed(false);
        inventoryTable.setFillsViewportHeight(true);

        // Listener for Edit button
        inventoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                editItemButton.setEnabled(inventoryTable.getSelectedRow() != -1);
            }
        });

        // Initial model
        String[] initialColumns = {"Status"};
        Object[][] initialData = {{"Initializing table..."}};
        inventoryTableModel = new DefaultTableModel(initialData, initialColumns) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        inventoryTable.setModel(inventoryTableModel);

        scrollPane.setViewportView(inventoryTable);
    }


    // --- All Inventory-related methods moved here ---

    public void refreshTable() {
        System.out.println("Refreshing inventory table...");
        String selectedCategory = "All Categories";
        if (categoryFilterComboBox != null && categoryFilterComboBox.getSelectedItem() != null) {
             selectedCategory = (String) categoryFilterComboBox.getSelectedItem();
        }
        populateInventoryTable(inventoryTable, currentBranchId, selectedCategory);
    }

    private void populateCategoryFilter() {
        List<String> categories = new ArrayList<>();
        categories.add("All Categories");
        String sql = "SELECT DISTINCT item_category FROM items ORDER BY item_category";
        // Use try-with-resources for better resource management
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String category = rs.getString("item_category");
                if (category != null && !category.trim().isEmpty()) {
                    categories.add(category);
                }
            }
            SwingUtilities.invokeLater(() -> {
                categoryFilterComboBox.setModel(new DefaultComboBoxModel<>(categories.toArray(new String[0])));
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ownerFrame, "Error loading categories: " + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(() -> {
                categoryFilterComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Error Loading"}));
            });
        }
        // Connection, Statement, ResultSet are auto-closed by try-with-resources
    }

    private void performSearch() {
        String searchText = searchField.getText();
        if (sorter == null) return;

        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                 sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
            } catch (PatternSyntaxException e) {
                 System.err.println("Invalid regex pattern in search: " + searchText);
                 sorter.setRowFilter(null);
            }
        }
    }

    private void openAddItemDialog() {
        AddItemDialog dialog = new AddItemDialog(ownerFrame, this); // Pass ownerFrame
        dialog.setVisible(true);
    }

    private void openEditItemDialog() {
        int selectedViewRow = inventoryTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(ownerFrame, "Please select an item to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = inventoryTable.convertRowIndexToModel(selectedViewRow);
        TableModel model = inventoryTable.getModel();
        try {
            int itemId = (Integer) model.getValueAt(modelRow, 0);
            String name = (String) model.getValueAt(modelRow, 2);
            String category = (String) model.getValueAt(modelRow, 3);
            String packType = (String) model.getValueAt(modelRow, 4);
            int currentStock = (Integer) model.getValueAt(modelRow, 5);
            BigDecimal srp = (BigDecimal) model.getValueAt(modelRow, 6);
            BigDecimal wsp = (BigDecimal) model.getValueAt(modelRow, 7);
            int minQty = fetchMinimumQuantityForItem(itemId);

            EditItemDialog dialog = new EditItemDialog(ownerFrame, this, itemId, name, packType, category, minQty, srp, wsp, currentStock);
            dialog.setVisible(true);
        } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ownerFrame, "Error retrieving item data from table.\n" + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int fetchMinimumQuantityForItem(int itemId) {
        String sql = "SELECT item_minimum_quantity FROM items WHERE _item_id = ?";
        int minQty = 0;
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    minQty = rs.getInt("item_minimum_quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider showing a less intrusive error or logging it
            // JOptionPane.showMessageDialog(ownerFrame, "Could not fetch min quantity: "+e.getMessage(), "DB Error", JOptionPane.WARNING_MESSAGE);
        }
        return minQty;
    }

    public void populateInventoryTable(JTable table, int branchId, String categoryFilter) {
        final String[] columnNames = {
            "ID", "Created At", "Name", "Category", "Pack Type", "Stock Qty", "SRP", "WSP"
        };
        inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                 switch (columnIndex) {
                    case 0: return Integer.class;    // ID
                    case 1: return Timestamp.class; // Created At
                    case 5: return Integer.class;    // Stock Qty
                    case 6: return BigDecimal.class; // SRP
                    case 7: return BigDecimal.class; // WSP
                    default: return String.class;   // Name, Category, Pack Type
                }
            }
        };

        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT i._item_id, i._item_created_at, i.item_name, i.item_category, " +
            "i.item_pack_type, COALESCE(ib.item_quantity, 0) AS item_stock_quantity, " +
            "i.item_srp, i.item_wsp " +
            "FROM items i " +
            "LEFT JOIN items_in_branch ib ON i._item_id = ib._item_id AND ib._company_branch_id = ? "
        );
        boolean hasCategoryFilter = categoryFilter != null && !categoryFilter.equals("All Categories");
        if (hasCategoryFilter) {
            sqlBuilder.append("WHERE i.item_category = ? ");
        }
        sqlBuilder.append("ORDER BY i.item_name");

        String sql = sqlBuilder.toString();
        Connection conn = null; // Keep conn outside try-with-resources for the finally block with DatabaseManager
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connect();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, branchId);
                if (hasCategoryFilter) { pstmt.setString(2, categoryFilter); }
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("_item_id"));
                    row.add(rs.getTimestamp("_item_created_at"));
                    row.add(rs.getString("item_name"));
                    row.add(rs.getString("item_category"));
                    row.add(rs.getString("item_pack_type"));
                    row.add(rs.getInt("item_stock_quantity"));
                    row.add(rs.getBigDecimal("item_srp"));
                    row.add(rs.getBigDecimal("item_wsp"));
                    inventoryTableModel.addRow(row);
                }

                table.setModel(inventoryTableModel);
                sorter = new TableRowSorter<>(inventoryTableModel);
                table.setRowSorter(sorter);

                // Adjust Column Alignment
                if (table.getColumnCount() > 0) {
                    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
                    leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
                    table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
                    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                    rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
                    if (table.getColumnCount() > 7) {
                        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
                        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
                        table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
                    }
                }
                performSearch();
                System.out.println("Inventory table populated for branch " + branchId + ", category: " + categoryFilter);
            } else {
                 handleDbConnectionError(table, columnNames);
            }
        } catch (SQLException e) {
             handleDbQueryError(table, e, columnNames);
        } finally {
            // Close resources manually as conn was not in try-with-resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace();}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace();}
            DatabaseManager.closeConnection(conn); // Use manager to close conn
        }
    }

    private void handleDbConnectionError(JTable table, String[] columnNames) {
        System.err.println("Failed to get database connection.");
        JOptionPane.showMessageDialog(ownerFrame, "Could not connect to the database.\nPlease check settings.", "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        DefaultTableModel errorModel = new DefaultTableModel(new Object[][]{{"Error: Connection Failed"}}, columnNames);
        table.setModel(errorModel);
        table.setRowSorter(null); sorter = null;
    }
    private void handleDbQueryError(JTable table, SQLException e, String[] columnNames) {
        System.err.println("SQL Error fetching inventory data: " + e.getMessage()); e.printStackTrace();
        JOptionPane.showMessageDialog(ownerFrame, "An error occurred while fetching inventory data:\n" + e.getMessage(), "Database Query Error", JOptionPane.ERROR_MESSAGE);
        DefaultTableModel errorModel = new DefaultTableModel(new Object[][]{{"Error: Query Failed"}}, columnNames);
        table.setModel(errorModel);
        table.setRowSorter(null); sorter = null;
    }

    // --- Database Modification Methods ---
    public boolean addNewItemToDatabase(String name, String packType, String category, int minQty, BigDecimal srp, BigDecimal wsp) {
        String sql = "INSERT INTO items (item_name, item_pack_type, item_category, item_minimum_quantity, item_srp, item_wsp) VALUES (?, ?, ?, ?, ?, ?)";
        boolean success = false;
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, packType);
            pstmt.setString(3, category);
            pstmt.setInt(4, minQty);
            pstmt.setBigDecimal(5, srp);
            pstmt.setBigDecimal(6, wsp);
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ownerFrame, "Error adding item to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }

    public boolean updateItemInDatabase(int itemId, String name, String packType, String category, int minQty, BigDecimal srp, BigDecimal wsp) {
        String sql = "UPDATE items SET item_name = ?, item_pack_type = ?, item_category = ?, " +
                     "item_minimum_quantity = ?, item_srp = ?, item_wsp = ? WHERE _item_id = ?";
        boolean success = false;
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, packType);
            pstmt.setString(3, category);
            pstmt.setInt(4, minQty);
            pstmt.setBigDecimal(5, srp);
            pstmt.setBigDecimal(6, wsp);
            pstmt.setInt(7, itemId);
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ownerFrame, "Error updating item in database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }

    public boolean deleteItemFromDatabase(int itemId) {
        // WARNING: Consider implications for items_in_branch, orders etc. Use DB cascades or manual deletes.
        String deleteItemSql = "DELETE FROM items WHERE _item_id = ?";
        boolean success = false;
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmtItem = conn.prepareStatement(deleteItemSql)) {
            // Optional: Add delete from items_in_branch within a transaction here
            pstmtItem.setInt(1, itemId);
            int rowsAffected = pstmtItem.executeUpdate();
            success = (rowsAffected == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ownerFrame, "Error deleting item: " + e.getMessage() + "\n(Related records might prevent deletion)", "Database Error", JOptionPane.ERROR_MESSAGE);
            success = false;
        }
        return success;
    }


    // --- Inner Classes for Dialogs ---

    // AddItemDialog (Pass ownerFrame and 'this' InventoryPanel instance)
    class AddItemDialog extends JDialog {
        // ... (Fields: nameField, packTypeField, categoryComboBox, minQtySpinner, srpSpinner, wspSpinner)
        private JTextField nameField;
        private JTextField packTypeField;
        private JComboBox<String> categoryComboBox;
        private JSpinner minQtySpinner;
        private JSpinner srpSpinner;
        private JSpinner wspSpinner;
        private InventoryPanel parentPanel; // Changed from MainFrame to InventoryPanel

        public AddItemDialog(JFrame owner, InventoryPanel parent) { // Takes InventoryPanel
            super(owner, "Add New Inventory Item", true);
            this.parentPanel = parent; // Store InventoryPanel reference
            // --- Setup Dialog UI (similar to before) ---
            setSize(450, 350);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout(10, 10));
            ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

            JPanel inputPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
             gbc.insets = new Insets(5, 5, 5, 5); gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Item Name:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; nameField = new JTextField(20); inputPanel.add(nameField, gbc); gbc.weightx = 0.0;

            gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Pack Type:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; packTypeField = new JTextField(20); inputPanel.add(packTypeField, gbc);

            gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Category:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; categoryComboBox = new JComboBox<>();
            // Use parentPanel's filter combo model
            ComboBoxModel<String> filterModel = parentPanel.categoryFilterComboBox.getModel();
            List<String> itemCategories = new ArrayList<>();
             for (int i = 0; i < filterModel.getSize(); i++) { String cat = filterModel.getElementAt(i); if (cat != null && !cat.equals("All Categories") && !cat.contains("Loading") && !cat.contains("Error")) { itemCategories.add(cat); }}
            if (!itemCategories.contains("to_be_assigned")) itemCategories.add("to_be_assigned");
            categoryComboBox.setModel(new DefaultComboBoxModel<>(itemCategories.toArray(new String[0])));
             if (!itemCategories.isEmpty()) categoryComboBox.setSelectedIndex(0);
            inputPanel.add(categoryComboBox, gbc);

            gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(new JLabel("Minimum Quantity:"), gbc);
            gbc.gridx = 1; gbc.gridy = 3; minQtySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1)); inputPanel.add(minQtySpinner, gbc);

            gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(new JLabel("SRP ($):"), gbc);
            gbc.gridx = 1; gbc.gridy = 4; srpSpinner = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 10000.00, 0.01)); srpSpinner.setEditor(new JSpinner.NumberEditor(srpSpinner, "0.00")); inputPanel.add(srpSpinner, gbc);

            gbc.gridx = 0; gbc.gridy = 5; inputPanel.add(new JLabel("WSP ($):"), gbc);
            gbc.gridx = 1; gbc.gridy = 5; wspSpinner = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 10000.00, 0.01)); wspSpinner.setEditor(new JSpinner.NumberEditor(wspSpinner, "0.00")); inputPanel.add(wspSpinner, gbc);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Save Item"); JButton cancelButton = new JButton("Cancel");
            saveButton.addActionListener(e -> saveItem()); cancelButton.addActionListener(e -> dispose());
            buttonPanel.add(saveButton); buttonPanel.add(cancelButton);

            add(inputPanel, BorderLayout.CENTER); add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveItem() {
            // Validation...
            String name = nameField.getText().trim(); String packType = packTypeField.getText().trim(); Object selectedCategoryObj = categoryComboBox.getSelectedItem();
             if (name.isEmpty() || packType.isEmpty() || selectedCategoryObj == null) { JOptionPane.showMessageDialog(this, "Name, Pack Type, and Category cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
            String category = selectedCategoryObj.toString();
            int minQty = (Integer) minQtySpinner.getValue(); BigDecimal srp, wsp;
            try { srp = BigDecimal.valueOf((Double) srpSpinner.getValue()); wsp = BigDecimal.valueOf((Double) wspSpinner.getValue()); } catch (ClassCastException cce) { try { srp = (BigDecimal) srpSpinner.getValue(); wsp = (BigDecimal) wspSpinner.getValue(); } catch (ClassCastException cce2) { JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; } }

            // Call parentPanel's method
            boolean success = parentPanel.addNewItemToDatabase(name, packType, category, minQty, srp, wsp);
            if (success) {
                JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                parentPanel.refreshTable(); // Use parentPanel's refresh method
                dispose();
            }
        }
    }

    // EditItemDialog (Pass ownerFrame and 'this' InventoryPanel instance)
    class EditItemDialog extends JDialog {
        // ... (Fields similar to AddItemDialog + currentStockLabel + editingItemId)
        private JTextField nameField;
        private JTextField packTypeField;
        private JComboBox<String> categoryComboBox;
        private JSpinner minQtySpinner;
        private JSpinner srpSpinner;
        private JSpinner wspSpinner;
        private JLabel currentStockLabel;
        private InventoryPanel parentPanel; // Changed from MainFrame
        private int editingItemId;

        public EditItemDialog(JFrame owner, InventoryPanel parent, int itemId, String name, String packType, String category, int minQty, BigDecimal srp, BigDecimal wsp, int currentStock) {
            super(owner, "Edit Inventory Item (ID: " + itemId + ")", true);
            this.parentPanel = parent; // Store InventoryPanel reference
            this.editingItemId = itemId;
            // --- Setup Dialog UI (similar to before, pre-filled) ---
             setSize(450, 400); setLocationRelativeTo(owner); setLayout(new BorderLayout(10, 10));
            ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

            JPanel inputPanel = new JPanel(new GridBagLayout()); GridBagConstraints gbc = new GridBagConstraints();
             gbc.insets = new Insets(5, 5, 5, 5); gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Item Name:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; nameField = new JTextField(20); nameField.setText(name); inputPanel.add(nameField, gbc); gbc.weightx = 0.0;

            gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Pack Type:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; packTypeField = new JTextField(20); packTypeField.setText(packType); inputPanel.add(packTypeField, gbc);

            gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Category:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; categoryComboBox = new JComboBox<>();
             ComboBoxModel<String> filterModel = parentPanel.categoryFilterComboBox.getModel(); List<String> itemCategories = new ArrayList<>();
             for (int i = 0; i < filterModel.getSize(); i++) { String cat = filterModel.getElementAt(i); if (cat != null && !cat.equals("All Categories") && !cat.contains("Loading") && !cat.contains("Error")) { itemCategories.add(cat); }}
            if (!itemCategories.contains("to_be_assigned")) itemCategories.add("to_be_assigned");
            categoryComboBox.setModel(new DefaultComboBoxModel<>(itemCategories.toArray(new String[0]))); categoryComboBox.setSelectedItem(category);
            inputPanel.add(categoryComboBox, gbc);

            gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(new JLabel("Minimum Quantity:"), gbc);
            gbc.gridx = 1; gbc.gridy = 3; minQtySpinner = new JSpinner(new SpinnerNumberModel(minQty, 0, 10000, 1)); inputPanel.add(minQtySpinner, gbc);

            gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(new JLabel("SRP ($):"), gbc);
            gbc.gridx = 1; gbc.gridy = 4; srpSpinner = new JSpinner(new SpinnerNumberModel(srp.doubleValue(), 0.00, 10000.00, 0.01)); srpSpinner.setEditor(new JSpinner.NumberEditor(srpSpinner, "0.00")); inputPanel.add(srpSpinner, gbc);

            gbc.gridx = 0; gbc.gridy = 5; inputPanel.add(new JLabel("WSP ($):"), gbc);
            gbc.gridx = 1; gbc.gridy = 5; wspSpinner = new JSpinner(new SpinnerNumberModel(wsp.doubleValue(), 0.00, 10000.00, 0.01)); wspSpinner.setEditor(new JSpinner.NumberEditor(wspSpinner, "0.00")); inputPanel.add(wspSpinner, gbc);

            gbc.gridx = 0; gbc.gridy = 6; inputPanel.add(new JLabel("Current Stock:"), gbc);
            gbc.gridx = 1; gbc.gridy = 6; currentStockLabel = new JLabel(String.valueOf(currentStock) + " (Read-Only)"); currentStockLabel.setFont(currentStockLabel.getFont().deriveFont(Font.ITALIC)); inputPanel.add(currentStockLabel, gbc);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Save Changes"); JButton cancelButton = new JButton("Cancel");
            saveButton.addActionListener(e -> saveChanges()); cancelButton.addActionListener(e -> dispose());
            buttonPanel.add(saveButton); buttonPanel.add(cancelButton);

            JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton deleteButton = new JButton("Delete Item"); deleteButton.setForeground(Color.WHITE); deleteButton.setBackground(Color.RED.darker());
            deleteButton.addActionListener(e -> deleteItem()); leftButtonPanel.add(deleteButton);

            JPanel bottomPanel = new JPanel(new BorderLayout()); bottomPanel.add(leftButtonPanel, BorderLayout.WEST); bottomPanel.add(buttonPanel, BorderLayout.EAST);

            add(inputPanel, BorderLayout.CENTER); add(bottomPanel, BorderLayout.SOUTH);
        }

        private void saveChanges() {
            // Validation...
             String name = nameField.getText().trim(); String packType = packTypeField.getText().trim(); Object selectedCategoryObj = categoryComboBox.getSelectedItem();
             if (name.isEmpty() || packType.isEmpty() || selectedCategoryObj == null) { JOptionPane.showMessageDialog(this, "Name, Pack Type, and Category cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; }
             String category = selectedCategoryObj.toString();
            int minQty = (Integer) minQtySpinner.getValue(); BigDecimal srp, wsp;
            try { srp = BigDecimal.valueOf((Double) srpSpinner.getValue()); wsp = BigDecimal.valueOf((Double) wspSpinner.getValue()); } catch (ClassCastException cce) { try { srp = (BigDecimal) srpSpinner.getValue(); wsp = (BigDecimal) wspSpinner.getValue(); } catch (ClassCastException cce2) { JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error", JOptionPane.WARNING_MESSAGE); return; } }

            // Call parentPanel's method
            boolean success = parentPanel.updateItemInDatabase(editingItemId, name, packType, category, minQty, srp, wsp);
            if (success) {
                JOptionPane.showMessageDialog(this, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                parentPanel.refreshTable(); // Use parentPanel's refresh method
                dispose();
            }
        }

        private void deleteItem() {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete item '" + nameField.getText() + "' (ID: " + editingItemId + ")?\nThis cannot be undone.", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                // Call parentPanel's method
                boolean success = parentPanel.deleteItemFromDatabase(editingItemId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    parentPanel.refreshTable(); // Use parentPanel's refresh method
                    dispose();
                }
            }
        }
    }
}