package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.ItemStock;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryHeader;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryTable;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.AddItemDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.DeleteItemsDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.EditItemDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.EditItemDialog.UpdateResult;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.InventoryFilterDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.InventoryFilterDialog.FilterResult;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.RestockItemDialog;

import net.miginfocom.swing.MigLayout;

public class InventoryScene implements Scene {

	private JPanel view;

	private InventoryTable inventoryTable;
	private InventoryHeader inventoryHeader;

	private AddItemDialog addItemDialog;
	private InventoryFilterDialog filterDialog;
	private EditItemDialog editItemDialog;
	private DeleteItemsDialog deleteItemsDialog;
	private RestockItemDialog restockItemDialog;

	private ExecutorService executor;

	private void addRowToTable(ItemStock rowData) {
		inventoryTable.prependItemStock(rowData);
	}

	private void attachComponents() {
		view.setLayout(new MigLayout("insets 0, flowy", "[grow]", "[]16[grow]"));

		view.add(inventoryHeader.getView(), "growx");
		view.add(inventoryTable.getView(), "grow");

		inventoryHeader.initializeComponents();
	}

	private void createComponents() {
		inventoryHeader = new InventoryHeader();
		inventoryTable = new InventoryTable();
	}

	@Override
	public String getSceneName() {
		return "inventory";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void handleAddCommand() {
		SwingUtilities.invokeLater(() -> {
			var owner = SwingUtilities.getWindowAncestor(view);

			if (addItemDialog == null) {
				addItemDialog = new AddItemDialog(owner, this::addRowToTable);
			}

			addItemDialog.setLocationRelativeTo(owner);
			addItemDialog.setVisible(true);
		});
	}

	private void handleDeleteCommand() {
		var selectedRows = inventoryTable.getSelectedRows();

		if (selectedRows.length == 0) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
						"Please choose a row to be deleted.", "Unsupported Operation", JOptionPane.ERROR_MESSAGE);
			});

			return;
		}

		SwingUtilities.invokeLater(() -> {
			var owner = SwingUtilities.getWindowAncestor(view);

			if (deleteItemsDialog == null) {
				deleteItemsDialog = new DeleteItemsDialog(owner, inventoryTable.getTable(), this::removeDeletedItems);
			}

			deleteItemsDialog.setLocationRelativeTo(owner);
			deleteItemsDialog.setRowsToDelete(selectedRows);
			deleteItemsDialog.setVisible(true);
		});
	}

	private void handleEditCommand() {
		var selectedRows = inventoryTable.getSelectedRows();

		if (selectedRows.length == 0) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
						"Please choose a row to be edited.", "Unsupported Operation", JOptionPane.ERROR_MESSAGE);
			});

			return;
		} else if (selectedRows.length > 1) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
						"Editing multiple items is not yet supported.", "Unsupported Operation",
						JOptionPane.ERROR_MESSAGE);
			});

			return;
		}

		SwingUtilities.invokeLater(() -> {
			var owner = SwingUtilities.getWindowAncestor(view);

			if (editItemDialog == null) {
				editItemDialog = new EditItemDialog(owner, inventoryTable.getTable(), this::onItemUpdate);
			}

			editItemDialog.setLocationRelativeTo(owner);
			editItemDialog.setRowtoBeEdited(selectedRows[0]);
			editItemDialog.setVisible(true);
		});
	}

	private void handleFilter(FilterResult filterResult) {
		var tableFilter = inventoryTable.getTableSearchListener().getTableFilter();
		var realCategoryIndex = inventoryTable.getTable().convertColumnIndexToView(InventoryTable.COL_CATEGORY_TYPE);
		var realSupplierIndex = inventoryTable.getTable().convertColumnIndexToView(InventoryTable.COL_SUPPLIER_NAME);

		tableFilter.setColumnFilter(realCategoryIndex, filterResult.category());
		tableFilter.setColumnFilter(realSupplierIndex, filterResult.supplier());
	}

	private void handleFilterCommand() {
		SwingUtilities.invokeLater(() -> {
			var owner = SwingUtilities.getWindowAncestor(view);

			if (filterDialog == null) {
				filterDialog = new InventoryFilterDialog(SwingUtilities.getWindowAncestor(view), this::handleFilter);
			}

			filterDialog.setLocationRelativeTo(owner);
			filterDialog.setVisible(true);
		});
	}

	// A hacky way to initialize the listeners
	// since the components are only initialized
	// once the table has gotten its data on first render.
	private void handleInitialInventoryTableThread() {
		inventoryTable.performBackgroundTask();

		// we do this because the components of inventory table will only be initialized
		// inside
		// the EDT. Besides, we're manipulating the UI as well by attaching listeners,
		// so this is
		// a good practice.
		SwingUtilities.invokeLater(() -> {
			// doesnt necessarily need to be here, but might as well to centralize where
			// listeners are added
			inventoryHeader.setBtnListener(this::listenToHeaderButtonEvents);
			inventoryHeader.setSearchListener(inventoryTable.getTableSearchListener());
		});
	}

	private void handleRestockCommand() {
		var selectedRows = inventoryTable.getSelectedRows();

		if (selectedRows.length == 0) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
						"Please choose a row to be restocked.", "Unsupported Operation", JOptionPane.ERROR_MESSAGE);
			});

			return;
		} else if (selectedRows.length > 1) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
						"Restocking multiple items is not yet supported.", "Unsupported Operation",
						JOptionPane.ERROR_MESSAGE);
			});

			return;
		}

		SwingUtilities.invokeLater(() -> {
			var owner = SwingUtilities.getWindowAncestor(view);

			if (restockItemDialog == null) {
				restockItemDialog = new RestockItemDialog(owner, inventoryTable.getTable(), this::refreshTableData);
			}

			restockItemDialog.setLocationRelativeTo(owner);
			restockItemDialog.setRowToBeRestocked(selectedRows[0]);
			restockItemDialog.setVisible(true);
		});
	}

	private void listenToHeaderButtonEvents(ActionEvent ev) {
		var command = ev.getActionCommand();

		switch (command) {
		case InventoryHeader.ADD_COMMAND:
			handleAddCommand();
			break;
		case InventoryHeader.FILTER_COMMAND:
			handleFilterCommand();
			break;
		case InventoryHeader.DELETE_COMMAND:
			handleDeleteCommand();
			break;
		case InventoryHeader.EDIT_COMMAND:
			handleEditCommand();
			break;
		case InventoryHeader.RESTOCK_COMMAND:
			handleRestockCommand();
			break;
		}
	}

	@Override
	public void onBeforeShow() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}

		executor = Executors.newFixedThreadPool(1);

		executor.submit(this::handleInitialInventoryTableThread);
	}

	@Override
	public void onCreate() {
		createComponents();
		attachComponents();
	}

	@Override
	public boolean onDestroy() {
		terminateThreads();

		if (inventoryTable != null) {
			inventoryTable.destroy();
		}

		inventoryHeader.destroy();
		inventoryTable.destroy();

		if (addItemDialog != null) {
			addItemDialog.destroy();
		}

		if (filterDialog != null) {
			filterDialog.destroy();
		}

		if (editItemDialog != null) {
			editItemDialog.destroy();
		}

		if (deleteItemsDialog != null) {
			deleteItemsDialog.destroy();
		}

		if (restockItemDialog != null) {
			restockItemDialog.destroy();
		}

		return true;
	}

	@Override
	public void onHide() {
		terminateThreads();
	}

	// to optimize the performance of the table, we only update the
	// columns that are updated
	private void onItemUpdate(UpdateResult result) {
		var model = inventoryTable.getTableModel();
		var realUnitPriceCol = inventoryTable.getTable().convertColumnIndexToView(InventoryTable.COL_UNIT_PRICE);
		var realMinQtyCol = inventoryTable.getTable().convertColumnIndexToView(InventoryTable.COL_MINIMUM_QUANTITY);

		model.setValueAt(result.sellingPrice(), result.row(), realUnitPriceCol);
		model.setValueAt(result.minQty(), result.row(), realMinQtyCol);
	}

	public void refreshTableData() {
		executor.submit(inventoryTable::performBackgroundTask);
	}

	/**
	 * To only remove the rows that are deleted from the table model, and not do a
	 * whole table refresh
	 *
	 * @param deletedRows
	 */
	public void removeDeletedItems(int[] deletedRows) {
		var model = (DefaultTableModel) inventoryTable.getTableModel();

		for (int i = deletedRows.length - 1; i >= 0; i--) {
			var realRow = inventoryTable.getTable().convertRowIndexToModel(deletedRows[i]);

			model.removeRow(realRow);
		}
	}

	private void terminateThreads() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}
	}

}