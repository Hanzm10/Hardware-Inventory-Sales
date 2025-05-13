package com.github.hanzm_10.murico.swingapp.scenes.inventory.dialogs;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.scenes.inventory.InventoryScene;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class RestockItemDialog extends JDialog {

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₱ #,##0.00");

    private final InventoryScene parentScene;
    private final int itemStockIdToRestock;
    private final int coreItemId; // Needed to fetch supplier and WSP
    private final String productNameToRestock;
    private final int currentQuantityBeforeRestock;

    private JLabel supplierLabelValue;
    private JLabel unitPriceLabelValue; // Displays WSP for 1 unit
    private JSpinner quantityToOrderSpinner;
    private JLabel orderQuantityLabelValue; // Displays quantity from spinner
    private JLabel orderPriceLabelValue;    // Displays WSP * ordered quantity
    private JLabel totalOrderPriceLabelValue;

    private BigDecimal wholesalePrice = BigDecimal.ZERO; // WSP fetched from DB

    public RestockItemDialog(Window owner, InventoryScene parent, int itemStockId, int itemId, String productName, int currentQuantity) {
        super(owner, "Restock Item (Simulate Supplier Order)", Dialog.ModalityType.APPLICATION_MODAL);
        this.parentScene = parent;
        this.itemStockIdToRestock = itemStockId;
        this.coreItemId = itemId;
        this.productNameToRestock = productName;
        this.currentQuantityBeforeRestock = currentQuantity;

        // --- Initialize UI Elements FIRST ---
        setTitle("Restock: " + productNameToRestock);
        setSize(450, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBackground(Color.WHITE);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Header
        JPanel headerPanel = new JPanel(new BorderLayout(10,0));
        headerPanel.setOpaque(false);
        JLabel productNameHeaderLabel = new JLabel(productNameToRestock);
        productNameHeaderLabel.setFont(new Font("Montserrat", Font.BOLD, 28));
        headerPanel.add(productNameHeaderLabel, BorderLayout.WEST);
        JLabel itemIdHeaderLabel = new JLabel("ITEM ID: #" + coreItemId);
        itemIdHeaderLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        itemIdHeaderLabel.setForeground(Color.GRAY);
        JPanel itemIdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        itemIdPanel.setOpaque(false);
        itemIdPanel.add(itemIdHeaderLabel);
        headerPanel.add(itemIdPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        // IMPORTANT: createInputPanel() initializes supplierLabelValue, unitPriceLabelValue, etc.
        JPanel mainPanel = createAndConfigureMainPanel(); // Call a method that sets up the fields
        add(mainPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10,0,0,0));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(0xFF3B30)); // Red
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.addActionListener(e -> dispose());
        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setBackground(new Color(0x007A87)); // Teal/Blue
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        confirmButton.setPreferredSize(new Dimension(150, 40));
        confirmButton.addActionListener(e -> confirmOrderAction());
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Now that UI fields are initialized, fetch data ---
        fetchSupplierAndWsp();

        // Listener for quantity spinner (needs wholesalePrice to be set by fetchSupplierAndWsp)
        quantityToOrderSpinner.addChangeListener(e -> updateOrderDisplay());
        updateOrderDisplay(); // Initial calculation based on default spinner value and fetched WSP

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private JPanel createAndConfigureMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false); // Transparent to show dialog's background
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10,0,10,0));

        // Supplier Section
        JPanel supplierSection = new JPanel(new BorderLayout(10,5));
        supplierSection.setOpaque(false);
        supplierSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0,0,5,0),"Supplier", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.BOLD, 12), Color.DARK_GRAY), // Styled title
            new EmptyBorder(5,0,10,0) // Padding below title
        ));
        supplierLabelValue = new JLabel("Fetching supplier..."); // Initialize class field
        supplierLabelValue.setFont(new Font("Montserrat", Font.BOLD, 16));
        JPanel supplierDisplayPanel = new JPanel(new BorderLayout());
        supplierDisplayPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(5,8,5,8)
        ));
        supplierDisplayPanel.add(supplierLabelValue, BorderLayout.CENTER);
        JLabel arrowLabel = new JLabel("▼");
        arrowLabel.setForeground(Color.GRAY);
        supplierDisplayPanel.add(arrowLabel, BorderLayout.EAST);
        supplierSection.add(supplierDisplayPanel, BorderLayout.CENTER);
        mainPanel.add(supplierSection);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));


        // Unit Price Display (WSP for 1 unit)
        unitPriceLabelValue = new JLabel(CURRENCY_FORMAT.format(wholesalePrice)); // Initialize class field
        JPanel unitPriceSection = createOrderLineDisplayPanel("Product (Unit Price)", new JLabel("x1"), unitPriceLabelValue);
        mainPanel.add(unitPriceSection);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Enter Quantity Section
        JPanel quantityInputSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        quantityInputSection.setOpaque(false);
        JLabel enterQtyLabel = new JLabel("Enter quantity:");
        enterQtyLabel.setFont(new Font("Montserrat", Font.BOLD, 14)); // Make it bold like mockup
        quantityInputSection.add(enterQtyLabel);
        quantityToOrderSpinner = new JSpinner(new SpinnerNumberModel(25, 1, 10000, 1));
        quantityToOrderSpinner.setPreferredSize(new Dimension(80, 28)); // Slightly taller
        quantityInputSection.add(quantityToOrderSpinner);
        mainPanel.add(quantityInputSection);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));


        mainPanel.add(new DashedLineSeparator());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        // Order Line Display (based on entered quantity)
        orderQuantityLabelValue = new JLabel("x25"); // Initialize class field
        orderPriceLabelValue = new JLabel("₱ --.--");  // Initialize class field
        mainPanel.add(createOrderLineDisplayPanel("Product (Order)", orderQuantityLabelValue, orderPriceLabelValue));
        mainPanel.add(Box.createRigidArea(new Dimension(0,15)));

        // Total Section
        JPanel totalSection = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalSection.setOpaque(false);
        JLabel totalTextLabel = new JLabel("Total:");
        totalTextLabel.setFont(new Font("Montserrat", Font.PLAIN, 18)); // Match mockup better
        totalSection.add(totalTextLabel);
        totalOrderPriceLabelValue = new JLabel(CURRENCY_FORMAT.format(BigDecimal.ZERO)); // Initialize class field
        totalOrderPriceLabelValue.setFont(new Font("Montserrat", Font.BOLD, 24));
        totalSection.add(totalOrderPriceLabelValue);
        mainPanel.add(totalSection);

        return mainPanel;
    }

    private JPanel createOrderLineDisplayPanel(String productName, JLabel quantityLabel, JLabel priceLabel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,5,2,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Product
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField productField = new JTextField(productName);
        productField.setEditable(false); productField.setBorder(null); productField.setOpaque(false);
        panel.add(productField, gbc);

        // Quantity
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.1; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        quantityLabel.setFont(new Font("Montserrat", Font.PLAIN, 12));
        panel.add(quantityLabel, gbc);

        // Price
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        priceLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
        panel.add(priceLabel, gbc);

        return panel;
    }


    private void fetchSupplierAndWsp() {
        // This query assumes an item is primarily linked to ONE supplier via suppliers_items
        // If an item can have multiple suppliers, you'd need a way to pick the default/preferred one.
        String sql = """
            SELECT s.name AS supplier_name, si.wsp_php
            FROM suppliers_items si
            JOIN suppliers s ON si._supplier_id = s._supplier_id
            WHERE si._item_id = ?
            LIMIT 1
            """;
        try (Connection conn = MySqlFactoryDao.createConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, coreItemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    supplierLabelValue.setText(rs.getString("supplier_name"));
                    wholesalePrice = rs.getBigDecimal("wsp_php");
                    if (wholesalePrice == null) wholesalePrice = BigDecimal.ZERO;
                    unitPriceLabelValue.setText(CURRENCY_FORMAT.format(wholesalePrice));
                } else {
                    supplierLabelValue.setText("N/A (No supplier link)");
                    wholesalePrice = BigDecimal.ZERO; // No WSP if no supplier
                    unitPriceLabelValue.setText(CURRENCY_FORMAT.format(wholesalePrice));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            supplierLabelValue.setText("Error loading supplier");
            wholesalePrice = BigDecimal.ZERO;
            unitPriceLabelValue.setText(CURRENCY_FORMAT.format(wholesalePrice));
            JOptionPane.showMessageDialog(this, "Error fetching supplier details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        updateOrderDisplay(); // Update total after fetching WSP
    }

    private void updateOrderDisplay() {
        int quantityToOrder = 0;
        try {
            quantityToOrder = (Integer) quantityToOrderSpinner.getValue();
        } catch (Exception e) { /* Spinner might not be fully initialized yet */ return; }

        if (quantityToOrder < 0) quantityToOrder = 0;

        orderQuantityLabelValue.setText("x" + quantityToOrder);
        BigDecimal lineTotal = wholesalePrice.multiply(BigDecimal.valueOf(quantityToOrder));
        orderPriceLabelValue.setText(CURRENCY_FORMAT.format(lineTotal));
        totalOrderPriceLabelValue.setText(CURRENCY_FORMAT.format(lineTotal)); // Assuming only one item type per restock dialog
    }

    private void confirmOrderAction() {
        int quantityToOrder = (Integer) quantityToOrderSpinner.getValue();
        if (quantityToOrder <= 0) {
            JOptionPane.showMessageDialog(this, "Please enter a quantity greater than zero to order.", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Show "Supplier Notified" Popup
        showNotificationPopup();

        // 2. Close this dialog
        dispose();

        // 3. Schedule stock update after a delay
        int delayMilliseconds = 60 * 1000; // 1 minute
        Timer stockUpdateTimer = new Timer(delayMilliseconds, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Timer fired: Processing restock for Item Stock ID: " + itemStockIdToRestock);
                boolean success = parentScene.processRestock(itemStockIdToRestock, quantityToOrder, currentQuantityBeforeRestock);
                if (success) {
                    System.out.println("Restock successful, refreshing parent table.");
                    parentScene.refreshTableData();
                    // Optionally, show a small system tray notification or log it.
                } else {
                    System.err.println("Delayed restock processing failed for Item Stock ID: " + itemStockIdToRestock);
                    // Maybe show an error to the user if this is critical and they are still around
                }
            }
        });
        stockUpdateTimer.setRepeats(false); // Only fire once
        stockUpdateTimer.start();

        System.out.println("Order confirmed for " + quantityToOrder + " units. Stock update scheduled in 1 minute.");
    }

    private void showNotificationPopup() {
        JDialog notificationDialog = new JDialog(this, "Notification", true); // Modal
        notificationDialog.setSize(350, 200);
        notificationDialog.setLocationRelativeTo(this);
        notificationDialog.setLayout(new BorderLayout());
        notificationDialog.getContentPane().setBackground(Color.WHITE);

        // Checkmark Icon (using AssetManager if you have a checkmark SVG)
        ImageIcon checkIcon = null;
        try {
            checkIcon = AssetManager.getOrLoadIcon("/icons/checkmark_icon.svg"); // Adjust path and size
        } catch (Exception e) { e.printStackTrace(); }

        JLabel iconLabel; // Declare first
        if (checkIcon != null) {
            iconLabel = new JLabel(checkIcon); // Constructor for Icon
        } else {
            iconLabel = new JLabel("✔"); // Constructor for String
            iconLabel.setFont(new Font("Montserrat", Font.BOLD, 48)); // Make text checkmark big if icon fails
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBorder(new EmptyBorder(20,0,10,0));


        JLabel messageLabel = new JLabel("Supplier has successfully been notified!");
        messageLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(new EmptyBorder(0,0,20,0));

        notificationDialog.add(iconLabel, BorderLayout.CENTER);
        notificationDialog.add(messageLabel, BorderLayout.SOUTH);

        // Auto-close timer for the notification popup
        Timer autoCloseTimer = new Timer(3000, e -> notificationDialog.dispose()); // Closes after 3 seconds
        autoCloseTimer.setRepeats(false);
        autoCloseTimer.start();

        notificationDialog.setVisible(true);
    }

    // Custom component for dashed line
    static class DashedLineSeparator extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.LIGHT_GRAY);
            Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5, 5}, 0);
            g2d.setStroke(dashed);
            g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            g2d.dispose();
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, 10); // Height of the separator
        }
    }
}