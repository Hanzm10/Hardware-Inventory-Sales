package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
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
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.ProgressLevelRenderer.ProgressLevel;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryTable;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.dialogs.SuccessDialog;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class EditItemDialog extends JDialog {

	public static record UpdateResult(int row, BigDecimal sellingPrice, int minQty) {
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(EditItemDialog.class);
	private @NotNull final WindowAdapter windowListener;

	private @NotNull final ComponentAdapter componentListener;
	private @NotNull final Window owner;
	private @NotNull AtomicInteger rowToBeEdited = new AtomicInteger(-1);

	private @NotNull JTable table;
	private JPanel headerPanel;
	private JLabel title;
	private JLabel subTitle;
	private JLabel description;

	private JPanel formPanel;

	private JLabel sellingPriceLabel;
	private JSpinner sellingPrice;
	private JLabel sellingPriceError;

	private JLabel minQtyLabel;
	private JSpinner minQty;
	private JLabel minQtyError;

	private JScrollPane scrollPane;

	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton saveButton;

	private Thread updateThread;

	private Consumer<UpdateResult> onUpdate;

	public EditItemDialog(@NotNull Window owner, JTable table, Consumer<UpdateResult> onUpdate) {
		super(owner, "Edit Item", Dialog.ModalityType.APPLICATION_MODAL);

		this.owner = owner;
		this.table = table;
		this.onUpdate = onUpdate;
		this.windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (updateThread != null && updateThread.isAlive()) {
					updateThread.interrupt();
					ConnectionManager.cancel(updateThread);
				}

				clearErrorMessages();
				clearFields();
				validate();

				rowToBeEdited.set(-1);
				;

				dispose();
			};
		};
		this.componentListener = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				super.componentShown(e);
				updateDisplay();

			}
		};

		setLayout(new MigLayout("insets 16, flowy, gap 2 16", "[grow]", "[grow]"));

		createComponents();
		attachComponents();

		pack();
		setSize(new Dimension(500, 450));

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(windowListener);
		addComponentListener(componentListener);
	}

	private void attachComponents() {
		headerPanel.add(title, "grow");
		headerPanel.add(subTitle, "grow");
		headerPanel.add(description, "grow");

		formPanel.add(sellingPriceLabel, "grow");
		formPanel.add(sellingPrice, "grow");
		formPanel.add(sellingPriceError, "grow");

		formPanel.add(Box.createVerticalStrut(2));

		formPanel.add(minQtyLabel, "grow");
		formPanel.add(minQty, "grow");
		formPanel.add(minQtyError, "grow");

		buttonPanel.add(cancelButton);
		buttonPanel.add(saveButton);

		add(headerPanel, "grow");
		add(scrollPane, "grow");
		add(buttonPanel, "grow");
	}

	private void clearErrorMessages() {
		sellingPriceError.setText("");
		minQtyError.setText("");
	}

	private void clearFields() {
		sellingPrice.setValue(0.00);
		minQty.setValue(0);
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));

		buttonPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getForeground()),
						BorderFactory.createEmptyBorder(8, 0, 0, 0)));

		saveButton = StyledButtonFactory.createButton("Save", ButtonStyles.PRIMARY);
		cancelButton = StyledButtonFactory.createButton("Cancel", ButtonStyles.SECONDARY);

		saveButton.setToolTipText("Save changes");
		cancelButton.setToolTipText("Cancel");

		saveButton.addActionListener(this::handleSave);
		cancelButton.addActionListener(this::handleCancel);
	}

	private void createComponents() {
		createHeaderPanel();
		createFormPanel();
		createButtonPanel();
	}

	private void createFormPanel() {
		formPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]"));

		sellingPriceLabel = LabelFactory.createBoldLabel("Selling Price(₱)*", 12, Color.GRAY);
		sellingPrice = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 10.00));
		sellingPrice.setFont(sellingPrice.getFont().deriveFont(14f));
		sellingPriceError = LabelFactory.createLabel("", 9, Styles.DANGER_COLOR);

		minQtyLabel = LabelFactory.createBoldLabel("Minimum Quantity*", 12, Color.GRAY);
		minQty = new JSpinner(new SpinnerNumberModel(0, 0, 100_000, 1));
		minQty.setFont(minQty.getFont().deriveFont(14f));
		minQtyError = LabelFactory.createLabel("", 9, Styles.DANGER_COLOR);

		scrollPane = new JScrollPane(formPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	}

	private void createHeaderPanel() {
		headerPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]4[top]2[top]"));
		title = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 24);
		subTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 16);
		description = LabelFactory.createBoldItalicLabel(HtmlUtils.wrapInHtml(""), 14, Color.GRAY);
	}

	public void destroy() {
		removeWindowListener(windowListener);
		removeComponentListener(componentListener);

		if (updateThread != null && updateThread.isAlive()) {
			updateThread.interrupt();
			ConnectionManager.cancel(updateThread);
		}

		cancelButton.removeActionListener(this::handleCancel);
		saveButton.removeActionListener(this::handleSave);
	}

	private void disableButtons() {
		saveButton.setEnabled(false);
		cancelButton.setEnabled(false);
	}

	private void enableButtons() {
		saveButton.setEnabled(true);
		cancelButton.setEnabled(true);
	}

	private int getItemMinQty() {
		var realItemMinQtyColumn = table.convertColumnIndexToView(InventoryTable.COL_MINIMUM_QUANTITY);
		var val = table.getValueAt(rowToBeEdited.get(), realItemMinQtyColumn);

		if (val instanceof Integer) {
			return (int) val;
		}

		return Integer.parseInt(val.toString());
	}

	private String getItemName() {
		var realItemNameColumn = table.convertColumnIndexToView(InventoryTable.COL_ITEM_NAME);

		return table.getValueAt(rowToBeEdited.get(), realItemNameColumn).toString();
	}

	private int getItemQuantity() {
		var realItemQuantityColumn = table.convertColumnIndexToView(InventoryTable.COL_STOCK_QUANTITY);
		var val = table.getValueAt(rowToBeEdited.get(), realItemQuantityColumn);

		if (val instanceof ProgressLevel) {
			return ((ProgressLevel) val).currentProgressLevel();
		}

		return Integer.parseInt(val.toString());
	}

	private double getItemUnitPrice() {
		var realItemUnitPriceColumn = table.convertColumnIndexToView(InventoryTable.COL_UNIT_PRICE);
		var val = table.getValueAt(rowToBeEdited.get(), realItemUnitPriceColumn);

		if (val instanceof BigDecimal) {
			return ((BigDecimal) val).doubleValue();
		}

		var stringVal = val.toString();

		if (stringVal.startsWith("₱")) {
			return Double.parseDouble(stringVal.split("₱")[1]);
		}

		return Double.parseDouble(stringVal);
	}

	public int getStockId() {
		var realStockIdColumn = table.convertColumnIndexToView(InventoryTable.COL_ITEM_STOCK_ID);
		var val = table.getValueAt(rowToBeEdited.get(), realStockIdColumn);

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

	private void handleSave(ActionEvent ev) {
		SwingUtilities.invokeLater(this::clearErrorMessages);

		if (!isValidInput()) {
			return;
		}

		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		SwingUtilities.invokeLater(this::disableButtons);

		updateThread = new Thread(() -> {
			try {
				factory.getItemDao().updateItemStock(getStockId(), BigDecimal.valueOf((double) sellingPrice.getValue()),
						(int) minQty.getValue());

				SwingUtilities.invokeLater(() -> {
					new SuccessDialog(this, "Item has been edited!").setVisible(true);
					onUpdate.accept(new UpdateResult(rowToBeEdited.get(),
							BigDecimal.valueOf((double) sellingPrice.getValue()), (int) minQty.getValue()));
					dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				});
			} catch (SQLException | IOException e) {
				LOGGER.log(Level.SEVERE, "Failed to update item stock", e);

				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this, "Error updating item: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				});
			} finally {
				SwingUtilities.invokeLater(this::enableButtons);
			}
		});

		updateThread.start();
	}

	private boolean isValidInput() {
		var sellingPriceValue = BigDecimal.valueOf((double) sellingPrice.getValue());
		var minQtyValue = (int) minQty.getValue();

		if (sellingPriceValue.compareTo(BigDecimal.ZERO) < 0) {
			SwingUtilities.invokeLater(() -> {
				sellingPriceError.setText(HtmlUtils.wrapInHtml("Selling Price cannot be negative"));
			});

			return false;
		}

		if (minQtyValue < 0) {
			SwingUtilities.invokeLater(() -> {
				minQtyError.setText(HtmlUtils.wrapInHtml("Minimum Quantity cannot be negative"));
			});
			return false;
		}

		return true;
	}

	public void setRowtoBeEdited(@Range(from = 0, to = Integer.MAX_VALUE) int newRow) {
		this.rowToBeEdited.set(newRow);
	}

	private void updateDisplay() {
		title.setText(HtmlUtils.wrapInHtml("Edit " + getItemName()));
		subTitle.setText(HtmlUtils.wrapInHtml("Stock ID: " + getStockId()));
		description.setText(HtmlUtils.wrapInHtml("Current stock: " + getItemQuantity() + " unit(s)"));
		minQty.setValue(getItemMinQty());
		sellingPrice.setValue(getItemUnitPrice());

		validate();
	}
}
