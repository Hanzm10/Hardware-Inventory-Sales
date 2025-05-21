package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.combobox_renderers.PlaceholderRenderer;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.ProgressLevelRenderer.ProgressLevel;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryTable;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.DashedLineSeparator;
import com.github.hanzm_10.murico.swingapp.ui.components.dialogs.SuccessDialog;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class RestockItemDialog extends JDialog {

	public record RestockSupplierComboBoxItem(@NotNull String supplierName, @NotNull BigDecimal wspPhp) {
		@Override
		public final String toString() {
			return supplierName;
		}
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(RestockItemDialog.class);

	private @NotNull final Window owner;
	private @NotNull final JTable table;
	private @NotNull final WindowAdapter windowListener;
	private @NotNull final ComponentAdapter componentListener;

	private JPanel headerPanel;
	private JLabel title;
	private JLabel subTitle;
	private JLabel description;

	private JPanel formPanel;
	private JLabel supplierLabel;
	private JComboBox<RestockSupplierComboBoxItem> supplier;
	private JLabel supplierErrorMsg;

	private JLabel quantityLabel;
	private JSpinner quantity;
	private JLabel quantityErrorMsg;

	private JPanel orderDetailsPanel;
	private JLabel wspLabel;
	private JLabel totalLabel;

	private JScrollPane scrollPane;

	private JPanel buttonPanel;
	private JButton confirmButton;
	private JButton cancelButton;

	private AtomicInteger rowToBeRestocked = new AtomicInteger(-1);

	private Thread fetchThread;
	private Thread updateThread;

	private @NotNull Runnable onUpdate;

	public RestockItemDialog(@NotNull final Window owner, @NotNull final JTable table,
			@NotNull final Runnable onUpdate) {
		super(owner, "Restock Item", Dialog.ModalityType.APPLICATION_MODAL);

		this.owner = owner;
		this.table = table;
		this.onUpdate = onUpdate;
		this.windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (fetchThread != null && fetchThread.isAlive()) {
					fetchThread.interrupt();
					ConnectionManager.cancel(fetchThread);
				}

				if (updateThread != null && updateThread.isAlive()) {
					updateThread.interrupt();
					ConnectionManager.cancel(updateThread);
				}

				clearErrorMessages();
				clearFields();
				enableButtons();

				validate();

				dispose();
			}
		};
		this.componentListener = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				if (fetchThread != null && fetchThread.isAlive()) {
					fetchThread.interrupt();
					ConnectionManager.cancel(fetchThread);
				}

				fetchThread = new Thread(() -> {
					populateSupplierComboBox();
				});

				fetchThread.start();
				updateDisplay();
			}
		};

		setLayout(new MigLayout("insets 16, flowy, gap 2 16", "[grow]", "[grow]"));

		createComponents();
		attachComponents();

		pack();
		setSize(520, 460);

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(windowListener);
		addComponentListener(componentListener);
	}

	private void attachComponents() {
		headerPanel.add(title, "grow");
		headerPanel.add(subTitle, "grow");
		headerPanel.add(description, "grow");

		formPanel.add(supplierLabel, "grow");
		formPanel.add(supplier, "grow");
		formPanel.add(supplierErrorMsg, "grow");

		formPanel.add(Box.createVerticalStrut(4), "growx");

		formPanel.add(quantityLabel, "grow");
		formPanel.add(quantity, "grow");
		formPanel.add(quantityErrorMsg, "grow");

		formPanel.add(Box.createVerticalStrut(4), "growx");

		formPanel.add(new DashedLineSeparator(), "growx");

		formPanel.add(orderDetailsPanel, "grow");
		orderDetailsPanel.add(wspLabel, "alignx left");
		orderDetailsPanel.add(totalLabel, "alignx right");

		buttonPanel.add(cancelButton, "grow");
		buttonPanel.add(confirmButton, "grow");

		add(headerPanel, "grow");
		add(scrollPane, "grow");
		add(buttonPanel, "grow");
	}

	private void clearErrorMessages() {
		supplierErrorMsg.setText("");
		quantityErrorMsg.setText("");
	}

	private void clearFields() {
		quantity.setValue(1);
		supplier.setSelectedIndex(0);
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
		buttonPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getForeground()),
						BorderFactory.createEmptyBorder(8, 0, 0, 0)));

		confirmButton = StyledButtonFactory.createButton("Confirm", ButtonStyles.PRIMARY);
		cancelButton = StyledButtonFactory.createButton("Cancel", ButtonStyles.SECONDARY);

		confirmButton.setToolTipText("Confirm item restock");
		cancelButton.setToolTipText("Cancel operation");

		confirmButton.addActionListener(this::handleConfirm);
		cancelButton.addActionListener(this::handleCancel);
	}

	private void createComponents() {
		createHeaderPanel();
		createFormPanel();
		createButtonPanel();
	}

	private void createFormPanel() {
		formPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[grow]"));

		supplierLabel = LabelFactory.createBoldLabel("Order from supplier*", 12, Color.GRAY);
		supplier = new JComboBox<>();
		supplier.setFont(supplier.getFont().deriveFont(14f));
		supplierErrorMsg = LabelFactory.createErrorLabel("", 9);

		quantityLabel = LabelFactory.createBoldLabel("Restock quantity", 12, Color.GRAY);
		quantity = new JSpinner(new SpinnerNumberModel(1, 1, 1_000_000, 1));
		quantity.setFont(quantity.getFont().deriveFont(14f));
		quantityErrorMsg = LabelFactory.createErrorLabel("", 9);

		orderDetailsPanel = new JPanel(new MigLayout("insets 0, fillx", "[grow]push[grow]"));

		wspLabel = LabelFactory.createBoldItalicLabel("WSP: N/A", 14, Color.GRAY);
		totalLabel = LabelFactory.createBoldLabel("Total: N/A", 18);

		scrollPane = new JScrollPane(formPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		supplier.setRenderer(new PlaceholderRenderer<RestockSupplierComboBoxItem>(supplier));
		supplier.addItem(new RestockSupplierComboBoxItem("Select supplier", BigDecimal.ZERO));

		supplier.addActionListener(this::handleSupplierChange);
		quantity.addChangeListener(this::handleQuantityChange);
	}

	private void createHeaderPanel() {
		headerPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]4[top]2[top]"));
		title = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 24);
		subTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 16);
		description = LabelFactory.createBoldItalicLabel(HtmlUtils.wrapInHtml(""), 16, Color.GRAY);
	}

	public void destroy() {
		removeWindowListener(windowListener);
		removeComponentListener(componentListener);

		if (fetchThread != null && fetchThread.isAlive()) {
			fetchThread.interrupt();
			ConnectionManager.cancel(fetchThread);
		}

		if (updateThread != null && updateThread.isAlive()) {
			updateThread.interrupt();
			ConnectionManager.cancel(updateThread);
		}

		confirmButton.removeActionListener(this::handleConfirm);
		cancelButton.removeActionListener(this::handleCancel);

		supplier.removeActionListener(this::handleSupplierChange);
		quantity.removeChangeListener(this::handleQuantityChange);
	}

	private void disableButtons() {
		confirmButton.setEnabled(false);
		cancelButton.setEnabled(false);
	}

	private void enableButtons() {
		confirmButton.setEnabled(true);
		cancelButton.setEnabled(true);
	}

	public int getItemId() {
		var realItemIdColumn = table.convertColumnIndexToView(InventoryTable.COL_ITEM_ID);
		var val = table.getValueAt(rowToBeRestocked.get(), realItemIdColumn);

		if (val instanceof Integer) {
			return (int) val;
		}

		var stringVal = val.toString();

		if (stringVal.startsWith("#")) {
			return Integer.parseInt(stringVal.split("#")[1]);
		}

		return Integer.parseInt(stringVal);
	}

	private String getItemName() {
		var realItemNameColumn = table.convertColumnIndexToView(InventoryTable.COL_ITEM_NAME);

		return table.getValueAt(rowToBeRestocked.get(), realItemNameColumn).toString();
	}

	private int getItemQuantity() {
		var realItemQuantityColumn = table.convertColumnIndexToView(InventoryTable.COL_STOCK_QUANTITY);
		var val = table.getValueAt(rowToBeRestocked.get(), realItemQuantityColumn);

		if (val instanceof ProgressLevel) {
			return ((ProgressLevel) val).currentProgressLevel();
		}

		return Integer.parseInt(val.toString());
	}

	public int getStockId() {
		var realStockIdColumn = table.convertColumnIndexToView(InventoryTable.COL_ITEM_STOCK_ID);
		var val = table.getValueAt(rowToBeRestocked.get(), realStockIdColumn);

		if (val instanceof Integer) {
			return (int) val;
		}

		var stringVal = val.toString();

		if (stringVal.startsWith("#")) {
			return Integer.parseInt(stringVal.split("#")[1]);
		}

		return Integer.parseInt(stringVal);
	}

	private void handleCancel(ActionEvent ev) {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private void handleConfirm(ActionEvent ev) {
		SwingUtilities.invokeLater(this::clearErrorMessages);

		if (!isValidInput()) {
			return;
		}

		var val = (int) quantity.getValue();
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		SwingUtilities.invokeLater(this::disableButtons);

		updateThread = new Thread(() -> {
			try {
				var selectedItem = (RestockSupplierComboBoxItem) supplier.getSelectedItem();

				if (selectedItem == null) {
					return;
				}

				factory.getItemDao().restockItem(getStockId(), val, getItemQuantity());

				SwingUtilities.invokeLater(() -> {
					new SuccessDialog(this, "Item restocked successfully").setVisible(true);
					onUpdate.run();
					dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				});
			} catch (SQLException | IOException e) {
				LOGGER.log(Level.SEVERE, "Error restocking item", e);

				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this, "Error restocking item: " + e.getMessage(), "Database Error",
							JOptionPane.ERROR_MESSAGE);
					enableButtons();
				});
			} finally {
				SwingUtilities.invokeLater(this::enableButtons);
			}
		});

		updateThread.start();
	}

	private void handleQuantityChange(ChangeEvent ev) {
		var selectedItem = (RestockSupplierComboBoxItem) supplier.getSelectedItem();

		if (selectedItem != null) {
			totalLabel.setText(
					"Total: ₱" + selectedItem.wspPhp().multiply(new BigDecimal(quantity.getValue().toString())));
		}

		if (quantity.getValue() == null) {
			quantity.setValue(1);
		}

		if (quantity.getValue() instanceof Integer) {
			if ((int) quantity.getValue() < 1) {
				quantity.setValue(1);
			}
		}

		if (quantity.getValue() instanceof Double) {
			if ((double) quantity.getValue() < 1.0) {
				quantity.setValue(1.0);
			}
		}

		validate();
	}

	private void handleSupplierChange(ActionEvent ev) {
		var selectedItem = (RestockSupplierComboBoxItem) supplier.getSelectedItem();

		if (selectedItem != null) {
			if (selectedItem.wspPhp() == null) {
				wspLabel.setText("WSP: N/A");
				totalLabel.setText("Total: N/A");
				return;
			}

			wspLabel.setText("WSP: ₱" + selectedItem.wspPhp());
			totalLabel.setText(
					"Total: ₱" + selectedItem.wspPhp().multiply(new BigDecimal(quantity.getValue().toString())));
		}
	}

	private boolean isValidInput() {
		var isValid = true;

		if (supplier.getSelectedItem() == null) {
			SwingUtilities.invokeLater(() -> {
				supplierErrorMsg.setText(HtmlUtils.wrapInHtml("Please select a supplier"));
			});

			isValid = false;
		} else if (supplier.getSelectedItem() instanceof RestockSupplierComboBoxItem) {
			var selectedItem = (RestockSupplierComboBoxItem) supplier.getSelectedItem();

			if (selectedItem.wspPhp() == null) {
				SwingUtilities.invokeLater(() -> {
					supplierErrorMsg.setText(HtmlUtils.wrapInHtml("Please select a valid supplier"));
				});

				isValid = false;
			}
		}

		if (quantity.getValue() == null) {
			SwingUtilities.invokeLater(() -> {
				quantityErrorMsg.setText(HtmlUtils.wrapInHtml("Please enter a quantity"));
			});

			isValid = false;
		} else if (quantity.getValue() instanceof Integer) {
			if ((int) quantity.getValue() < 1) {
				SwingUtilities.invokeLater(() -> {
					quantityErrorMsg.setText(HtmlUtils.wrapInHtml("Quantity must be greater than 0"));
				});

				isValid = false;
			}
		} else {
			SwingUtilities.invokeLater(() -> {
				quantityErrorMsg.setText(HtmlUtils.wrapInHtml("Quantity must be an integer"));
			});

			isValid = false;
		}

		return isValid;
	}

	private void populateSupplierComboBox() {
		try {
			var supplierWsps = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getSupplierDao()
					.getAllSuppliersForItem(getItemId());

			SwingUtilities.invokeLater(() -> {
				var selectedItem = supplier.getSelectedItem();
				this.supplier.removeAllItems();
				this.supplier.addItem(new RestockSupplierComboBoxItem("Select supplier", null));

				for (var supplierWsp : supplierWsps) {
					this.supplier
							.addItem(new RestockSupplierComboBoxItem(supplierWsp.supplierName(), supplierWsp.wspPhp()));
				}

				if (selectedItem != null) {
					this.supplier.setSelectedItem(selectedItem);
				}
			});
		} catch (SQLException | IOException e) {
			LOGGER.log(Level.SEVERE, "Error fetching suppliers", e);

			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Database Error",
						JOptionPane.ERROR_MESSAGE);
			});
		}
	}

	public void setRowToBeRestocked(@Range(from = 0, to = Integer.MAX_VALUE) final int rowToBeRestocked) {
		this.rowToBeRestocked.set(rowToBeRestocked);
	}

	private void updateDisplay() {
		title.setText(HtmlUtils.wrapInHtml("Restock " + getItemName()));
		subTitle.setText(HtmlUtils.wrapInHtml("Stock ID: " + getStockId()));
		description.setText(HtmlUtils.wrapInHtml("Current stock: " + getItemQuantity() + " unit(s)"));

		validate();
	}
}
