package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.NumberUtils;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class RestockItemDialog extends JDialog {

	// Custom component for dashed line
	static class DashedLineSeparator extends JComponent {
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(super.getPreferredSize().width, 10); // Height of the separator
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(Color.LIGHT_GRAY);
			Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 5, 5 },
					0);
			g2d.setStroke(dashed);
			g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
			g2d.dispose();
		}
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(RestockItemDialog.class);

	private final int itemStockIdToRestock;
	private final int coreItemId; // Needed to fetch supplier and WSP
	private final String productNameToRestock;

	private final int currentQuantityBeforeRestock;
	private JLabel supplierLabelValue;
	private JLabel unitPriceLabelValue; // Displays WSP for 1 unit
	private JSpinner quantityToOrderSpinner;
	private JLabel orderQuantityLabelValue; // Displays quantity from spinner
	private JLabel orderPriceLabelValue; // Displays WSP * ordered quantity

	private JPanel headerPanel;

	private JLabel itemIdHeaderLabel; // Displays item ID in header
	private JLabel productNameHeaderLabel;

	private JLabel totalOrderPriceLabelValue;

	private JButton confirmButton;
	private JButton cancelButton;

	private BigDecimal wholesalePrice = BigDecimal.ZERO; // WSP fetched from DB

	private Runnable onUpdate;

	public RestockItemDialog(Window owner, int itemStockId, int itemId, String productName, int currentQuantity,
			Runnable onUpdate) {
		super(owner, "Restock Item (Simulate Supplier Order)", Dialog.ModalityType.APPLICATION_MODAL);

		this.itemStockIdToRestock = itemStockId;
		this.coreItemId = itemId;
		this.productNameToRestock = productName;
		this.currentQuantityBeforeRestock = currentQuantity;
		this.onUpdate = onUpdate;

		setTitle("Restock: " + productNameToRestock);

		setLayout(new MigLayout("insets 16, flowy", "[grow]", "[grow]"));

		headerPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]"));

		productNameHeaderLabel = LabelFactory.createBoldLabel(productNameToRestock, 28);
		itemIdHeaderLabel = LabelFactory.createBoldLabel("ITEM ID: #" + coreItemId, 14, Color.GRAY);

		headerPanel.add(productNameHeaderLabel, "grow");
		headerPanel.add(itemIdHeaderLabel, "grow");

		JPanel mainPanel = createAndConfigureMainPanel();
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));

		cancelButton = StyledButtonFactory.createButton("Cancel", ButtonStyles.SECONDARY);
		confirmButton = StyledButtonFactory.createButton("Confirm Order", ButtonStyles.PRIMARY);

		quantityToOrderSpinner.addChangeListener(this::handleSpinnerChange);
		cancelButton.addActionListener(this::handleCancel);
		confirmButton.addActionListener(this::handleConfirm);

		buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

		buttonPanel.add(cancelButton);
		buttonPanel.add(confirmButton);

		add(headerPanel, "grow");
		add(mainPanel, "grow");
		add(buttonPanel, "grow");

		fetchSupplierAndWsp();
		updateOrderDisplay();

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setSize(450, 500);
	}

	private void confirmOrderAction() {
		int quantityToOrder = (Integer) quantityToOrderSpinner.getValue();
		if (quantityToOrder <= 0) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, "Please enter a valid quantity to order.", "Invalid Quantity",
						JOptionPane.WARNING_MESSAGE);
			});
			return;
		}

		if (quantityToOrder <= 0) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, "Please enter a valid quantity to order.", "Invalid Quantity",
						JOptionPane.WARNING_MESSAGE);
			});

			return;
		}

		disableButtons();

		String updateStockSql = "UPDATE item_stocks SET quantity = ? WHERE _item_stock_id = ? AND is_deleted = FALSE";
		String insertRestockSql = "INSERT INTO item_restocks (_item_stock_id, quantity_before, quantity_after, quantity_added) VALUES (?, ?, ?, ?)";
		int quantityAfter = quantityToOrder + currentQuantityBeforeRestock;

		try (var conn = MySqlFactoryDao.createConnection();
				var pstmtStock = conn.prepareStatement(updateStockSql);
				var pstmtRestock = conn.prepareStatement(insertRestockSql);) {
			conn.setAutoCommit(false);

			try {
				pstmtStock.setInt(1, quantityAfter);
				pstmtStock.setInt(2, itemStockIdToRestock);
				pstmtStock.executeUpdate();

				pstmtRestock.setInt(1, itemStockIdToRestock);
				pstmtRestock.setInt(2, quantityToOrder);
				pstmtRestock.setInt(3, quantityAfter);
				pstmtRestock.setInt(4, currentQuantityBeforeRestock);
				pstmtRestock.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}

			conn.commit();

			SwingUtilities.invokeLater(() -> {
				showNotificationPopup();

				onUpdate.run();

				dispose();
			});
		} catch (SQLException e) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, "Error processing restock: " + e.getMessage(), "Database Error",
						JOptionPane.ERROR_MESSAGE);
			});
		} finally {
			SwingUtilities.invokeLater(() -> {
				enableButtons();
			});
		}
	}

	private JPanel createAndConfigureMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

		// Supplier Section
		JPanel supplierSection = new JPanel(new BorderLayout(10, 5));
		supplierSection.setOpaque(false);
		supplierSection.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0), "Supplier",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.BOLD, 12), Color.DARK_GRAY), // Styled
																														// title
				new EmptyBorder(5, 0, 10, 0) // Padding below title
		));
		supplierLabelValue = LabelFactory.createBoldLabel("Fetching supplier...", 16); // Initialize class field

		JPanel supplierDisplayPanel = new JPanel(new BorderLayout());
		supplierDisplayPanel.setBorder(
				BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5, 8, 5, 8)));
		supplierDisplayPanel.add(supplierLabelValue, BorderLayout.CENTER);
		JLabel arrowLabel = LabelFactory.createLabel("▼", 16, Color.GRAY);
		supplierDisplayPanel.add(arrowLabel, BorderLayout.EAST);
		supplierSection.add(supplierDisplayPanel, BorderLayout.CENTER);
		mainPanel.add(supplierSection);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Unit Price Display (WSP for 1 unit)
		unitPriceLabelValue = new JLabel(NumberUtils.formatter.format(wholesalePrice));
		JPanel unitPriceSection = createOrderLineDisplayPanel("Product (Unit Price)", new JLabel("x1"),
				unitPriceLabelValue);
		mainPanel.add(unitPriceSection);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		// Enter Quantity Section
		JPanel quantityInputSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		quantityInputSection.setOpaque(false);
		JLabel enterQtyLabel = LabelFactory.createBoldLabel("Enter quantity:", 14);

		quantityInputSection.add(enterQtyLabel);

		quantityToOrderSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));

		quantityToOrderSpinner.setPreferredSize(new Dimension(80, 28));

		quantityInputSection.add(quantityToOrderSpinner);
		mainPanel.add(quantityInputSection);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		mainPanel.add(new DashedLineSeparator());
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		// Order Line Display (based on entered quantity)
		orderQuantityLabelValue = LabelFactory.createBoldLabel("x1", 12); // Initialize class field
		orderPriceLabelValue = LabelFactory.createBoldLabel("₱ --.--", 12); // Initialize class field
		mainPanel.add(createOrderLineDisplayPanel("Product (Order)", orderQuantityLabelValue, orderPriceLabelValue));
		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		// Total Section
		JPanel totalSection = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalSection.setOpaque(false);
		JLabel totalTextLabel = LabelFactory.createLabel("Total:", 18);
		totalSection.add(totalTextLabel);
		totalOrderPriceLabelValue = LabelFactory.createBoldLabel(NumberUtils.formatter.format(BigDecimal.ZERO), 24);

		totalSection.add(totalOrderPriceLabelValue);
		mainPanel.add(totalSection);

		return mainPanel;
	}

	private JPanel createOrderLineDisplayPanel(String productName, JLabel quantityLabel, JLabel priceLabel) {
		JPanel panel = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 5, 2, 5);
		gbc.anchor = GridBagConstraints.WEST;

		// Product
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.7;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JTextField productField = new JTextField(productName);
		productField.setEditable(false);
		productField.setBorder(null);
		productField.setOpaque(false);
		panel.add(productField, gbc);

		// Quantity
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(quantityLabel, gbc);

		// Price
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(priceLabel, gbc);

		return panel;
	}

	private void disableButtons() {
		confirmButton.setEnabled(false);
		cancelButton.setEnabled(false);
	}

	private void enableButtons() {
		confirmButton.setEnabled(true);
		cancelButton.setEnabled(true);
	}

	private void fetchSupplierAndWsp() {
		// This query assumes an item is primarily linked to ONE supplier via
		// suppliers_items
		// If an item can have multiple suppliers, you'd need a way to pick the
		// default/preferred one.
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
					var supplierName = rs.getString("supplier_name");
					var wspPhp = rs.getBigDecimal("wsp_php");

					SwingUtilities.invokeLater(() -> {
						supplierLabelValue.setText(supplierName);
						wholesalePrice = wspPhp;
						if (wholesalePrice == null) {
							wholesalePrice = BigDecimal.ZERO;
						}
						unitPriceLabelValue.setText(NumberUtils.formatter.format(wholesalePrice));
					});
				} else {
					SwingUtilities.invokeLater(() -> {
						supplierLabelValue.setText("N/A (No supplier link)");
						wholesalePrice = BigDecimal.ZERO; // No WSP if no supplier
						unitPriceLabelValue.setText(NumberUtils.formatter.format(wholesalePrice));
					});
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error fetching supplier and WSP", e);

			SwingUtilities.invokeLater(() -> {
				supplierLabelValue.setText("Error loading supplier");
				wholesalePrice = BigDecimal.ZERO;
				unitPriceLabelValue.setText(NumberUtils.formatter.format(wholesalePrice));
				JOptionPane.showMessageDialog(this, "Error fetching supplier details: " + e.getMessage(),
						"Database Error", JOptionPane.ERROR_MESSAGE);
			});
		}

		SwingUtilities.invokeLater(() -> {
			updateOrderDisplay();
		});
	}

	private void handleCancel(ActionEvent ev) {
		dispose();
	}

	private void handleConfirm(ActionEvent ev) {
		confirmOrderAction();
	}

	private void handleSpinnerChange(ChangeEvent e) {
		updateOrderDisplay();
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
			checkIcon = AssetManager.getOrLoadIcon("icons/checkmark_icon.svg"); // Adjust path and size
		} catch (Exception e) {
			e.printStackTrace();
		}

		JLabel iconLabel; // Declare first
		if (checkIcon != null) {
			iconLabel = new JLabel(checkIcon); // Constructor for Icon
		} else {
			iconLabel = new JLabel("✔"); // Constructor for String
			iconLabel.setFont(new Font("Montserrat", Font.BOLD, 48)); // Make text checkmark big if icon fails
		}
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setBorder(new EmptyBorder(20, 0, 10, 0));

		JLabel messageLabel = new JLabel("Supplier has successfully been notified!");
		messageLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

		notificationDialog.add(iconLabel, BorderLayout.CENTER);
		notificationDialog.add(messageLabel, BorderLayout.SOUTH);

		SwingUtilities.invokeLater(() -> {
			notificationDialog.setVisible(true);
		});
	}

	private void updateOrderDisplay() {
		int quantityToOrder = 0;
		try {
			quantityToOrder = (Integer) quantityToOrderSpinner.getValue();
		} catch (Exception e) {
			/* Spinner might not be fully initialized yet */ return;
		}

		if (quantityToOrder < 0) {
			quantityToOrder = 0;
		}

		orderQuantityLabelValue.setText("x" + quantityToOrder);
		BigDecimal lineTotal = wholesalePrice.multiply(BigDecimal.valueOf(quantityToOrder));
		orderPriceLabelValue.setText(NumberUtils.formatter.format(lineTotal));
		totalOrderPriceLabelValue.setText(NumberUtils.formatter.format(lineTotal)); // Assuming only one item type per
																					// restock
		// dialog
	}
}
