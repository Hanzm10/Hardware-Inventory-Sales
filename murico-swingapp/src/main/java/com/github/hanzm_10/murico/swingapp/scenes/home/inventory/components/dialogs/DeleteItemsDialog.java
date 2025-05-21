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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryTable;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.dialogs.SuccessDialog;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class DeleteItemsDialog extends JDialog {
	public static record ItemToBeDeleted(@NotNull int itemId, @NotNull int itemStockId, @NotNull String itemName) {
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(DeleteItemsDialog.class);
	private @NotNull final Window owner;

	private @NotNull final WindowAdapter windowListener;
	private @NotNull final ComponentAdapter componentListener;

	private int[] rowsToDelete;

	private @NotNull JTable table;
	private Consumer<int[]> onDelete;
	private JPanel headerPanel;

	private JLabel title;
	private JLabel subTitle;

	private JScrollPane scrollPane;
	private JPanel contentPanel;
	private JPanel buttonPanel;

	private JButton cancelButton;

	private JButton deleteButton;

	private Thread deleteThread;

	private ItemToBeDeleted[] itemsToBeDeleted;

	public DeleteItemsDialog(@NotNull final Window owner, @NotNull final JTable table,
			@NotNull final Consumer<int[]> onDelete) {
		super(owner, "Delete Item(s)", Dialog.ModalityType.APPLICATION_MODAL);

		this.owner = owner;
		this.table = table;
		this.onDelete = onDelete;

		setLayout(new MigLayout("insets 16, flowy, gap 2 16", "[grow]", "[grow]"));

		createComponents();
		attachComponents();

		this.windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				terminateThread();

				rowsToDelete = null;
				itemsToBeDeleted = null;

				contentPanel.removeAll();
				validate();

				dispose();
			}
		};

		this.componentListener = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				super.componentShown(e);
				updateDisplay();

			}
		};

		pack();
		setSize(new Dimension(400, 300));

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(windowListener);
		addComponentListener(componentListener);
	}

	private void attachComponents() {
		headerPanel.add(title, "grow");
		headerPanel.add(subTitle, "grow");

		updateContents();

		buttonPanel.add(cancelButton);
		buttonPanel.add(deleteButton);

		add(headerPanel, "grow");
		add(scrollPane, "grow");
		add(buttonPanel, "grow");
	}

	private void createComponents() {
		var rowsLen = rowsToDelete == null ? 0 : rowsToDelete.length;

		headerPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]4[top]"));
		title = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Deleting " + rowsLen + " item(s)"), 24);
		subTitle = LabelFactory.createLabel(HtmlUtils.wrapInHtml(
				"This will delete <strong>ALL</strong> items. This operation <b>CANNOT</b> be reversed."), 14);

		contentPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]"));
		scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
		buttonPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getForeground()),
						BorderFactory.createEmptyBorder(8, 0, 0, 0)));

		cancelButton = StyledButtonFactory.createButton("Cancel", ButtonStyles.SECONDARY);
		deleteButton = StyledButtonFactory.createButton("Delete", ButtonStyles.PRIMARY);

		cancelButton.setToolTipText("Cancel delete operation");
		deleteButton.setToolTipText("Confirm delete operation");

		cancelButton.addActionListener(this::handleCancel);
		deleteButton.addActionListener(this::handleDelete);
	}

	public void destroy() {
		removeWindowListener(windowListener);
		removeComponentListener(componentListener);

		cancelButton.removeActionListener(this::handleCancel);
		deleteButton.removeActionListener(this::handleDelete);

		terminateThread();
	}

	private void disableButtons() {
		deleteButton.setEnabled(false);
		deleteButton.setText("Deleting...");
	}

	private void enableButtons() {
		deleteButton.setEnabled(true);
		deleteButton.setText("Delete");
	}

	private int getItemId(int rowIdx) {
		var realItemIdColumn = table.convertColumnIndexToView(InventoryTable.COL_ITEM_ID);
		var val = table.getValueAt(rowIdx, realItemIdColumn);

		if (val instanceof Integer) {
			return (int) val;
		}

		var stringifiedVal = val.toString();

		if (stringifiedVal.startsWith("#")) {
			return Integer.parseInt(stringifiedVal.split("#")[1]);
		}

		return Integer.parseInt(stringifiedVal);
	}

	private String getItemName(int rowIdx) {
		var realItemNameColumn = table.convertColumnIndexToView(InventoryTable.COL_ITEM_NAME);
		var val = table.getValueAt(rowIdx, realItemNameColumn);

		if (val instanceof String) {
			return (String) val;
		}

		return val.toString();
	}

	private int getItemStockId(int rowIdx) {
		var realItemStockIdColumn = table.convertColumnIndexToView(InventoryTable.COL_ITEM_STOCK_ID);
		var val = table.getValueAt(rowIdx, realItemStockIdColumn);

		if (val instanceof Integer) {
			return (int) val;
		}

		var stringifiedVal = val.toString();

		if (stringifiedVal.startsWith("#")) {
			return Integer.parseInt(stringifiedVal.split("#")[1]);
		}

		return Integer.parseInt(stringifiedVal);
	}

	private void handleCancel(ActionEvent ev) {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private void handleDelete(ActionEvent ev) {
		terminateThread();

		SwingUtilities.invokeLater(this::disableButtons);

		deleteThread = new Thread(() -> {
			var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

			try {
				factory.getItemDao().archiveItems(itemsToBeDeleted, null);

				SwingUtilities.invokeLater(() -> {
					new SuccessDialog(this, "Delete operation is successful!").setVisible(true);
					onDelete.accept(rowsToDelete);
					dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				});

			} catch (SQLException | IOException e) {
				LOGGER.log(Level.SEVERE, "Failed to delete items", e);

				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				});
			} finally {
				SwingUtilities.invokeLater(this::enableButtons);
			}
		});

		deleteThread.start();
	}

	public void setRowsToDelete(int[] rowsToDelete) {
		this.rowsToDelete = rowsToDelete;
	}

	private void terminateThread() {
		if (deleteThread != null && deleteThread.isAlive()) {
			deleteThread.interrupt();
			ConnectionManager.cancel(deleteThread);
		}
	}

	private void updateContents() {
		if (rowsToDelete == null) {
			return;
		}

		var itemsToBeDeleted = new ArrayList<ItemToBeDeleted>();

		for (var i = 0; i < rowsToDelete.length; ++i) {
			var itemId = getItemId(rowsToDelete[i]);
			var itemStockId = getItemStockId(rowsToDelete[i]);
			var itemName = getItemName(rowsToDelete[i]);

			itemsToBeDeleted.add(new ItemToBeDeleted(itemId, itemStockId, itemName));

			var panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
			var idLabel = LabelFactory.createLabel("#" + itemStockId, 12, Color.GRAY);
			var nameLabel = LabelFactory.createLabel(itemName, 14);

			panel.add(idLabel);
			panel.add(nameLabel);

			contentPanel.add(panel, "grow");
		}

		this.itemsToBeDeleted = itemsToBeDeleted.toArray(new ItemToBeDeleted[itemsToBeDeleted.size()]);
	}

	private void updateDisplay() {
		var rowLen = rowsToDelete == null ? 0 : rowsToDelete.length;
		title.setText("Deleting " + rowLen + " item(s)");
		updateContents();

		validate();
	}

}
