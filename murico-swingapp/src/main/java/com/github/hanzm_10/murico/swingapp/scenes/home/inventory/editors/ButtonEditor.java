package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent; // Import for MouseEvent
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.github.hanzm_10.murico.swingapp.scenes.home.InventorySceneNew;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.InventoryTable;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	// Action commands for menu items
	public static final String ACTION_EDIT = "ACTION_EDIT_ITEM";
	public static final String ACTION_RESTOCK = "ACTION_RESTOCK_ITEM";
	public static final String ACTION_DELETE = "ACTION_DELETE_ITEM";
	private final JButton ellipsisButton;
	private final InventorySceneNew parentScene;

	private int currentRowInTable; // View row index
	private int currentModelRow; // Model row index
	private JTable currentTableRef; // Reference to the table

	public ButtonEditor(InventorySceneNew parentScene, TableCellRenderer renderer) {
		this.parentScene = parentScene;
		this.ellipsisButton = new JButton();

		// Configure the ellipsisButton based on the renderer's appearance
		if (renderer instanceof JButton) {
			JButton rendererButton = (JButton) renderer;
			this.ellipsisButton.setIcon(rendererButton.getIcon());
			this.ellipsisButton.setPreferredSize(rendererButton.getPreferredSize());
			this.ellipsisButton.setToolTipText(rendererButton.getToolTipText());
			this.ellipsisButton.setHorizontalAlignment(rendererButton.getHorizontalAlignment());
			this.ellipsisButton.setVerticalAlignment(rendererButton.getVerticalAlignment());
		} else {
			this.ellipsisButton.setText("..."); // Fallback
		}

		this.ellipsisButton.setBorderPainted(false);
		this.ellipsisButton.setContentAreaFilled(false);
		this.ellipsisButton.setFocusPainted(false);
		this.ellipsisButton.setOpaque(false);

		// ActionListener for the ellipsis button itself (to show the popup)
		this.ellipsisButton.addActionListener(e -> {
			// This ensures that the cell editing is stopped *before* the popup menu logic
			// fires.
			// Sometimes, without this, the popup might not get focus correctly or might
			// interfere with the table's selection model.
			SwingUtilities.invokeLater(() -> {
				stopCellEditing(); // Programmatically stop editing
				showActionPopupMenu(ellipsisButton);
			});
		});
	}

	// ActionListener for JMenuItems within the JPopupMenu
	@Override
	public void actionPerformed(ActionEvent e) {
		// Cell editing is already stopped by the ellipsis button's action.
		String command = e.getActionCommand();

		if (currentModelRow < 0 || currentTableRef == null) {
			System.err.println("ButtonEditor: No valid row selected for action (modelRow or tableRef is invalid).");
			return;
		}

		// Convert modelRow back to viewRow IF the methods in InventoryScene expect
		// viewRow
		// However, it's often more robust to pass modelRow to InventoryScene and let IT
		// handle
		// conversions if it needs to interact with view-specific aspects.
		// For now, assuming methods in InventoryScene can take modelRow or handle
		// conversion.
		// The openEditItemDialog in InventoryScene was already taking viewRow.

		int viewRowForScene = currentTableRef.convertRowIndexToView(currentModelRow);

		if (ACTION_EDIT.equals(command)) {
			parentScene.openEditItemDialog(viewRowForScene);
		} else if (ACTION_RESTOCK.equals(command)) {
			parentScene.openRestockDialog(currentModelRow); // Pass modelRow
		} else if (ACTION_DELETE.equals(command)) {
			parentScene.handleDeleteItem(currentModelRow); // Pass modelRow
		}
	}

	@Override
	public Object getCellEditorValue() {
		return "";
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.currentTableRef = table;
		this.currentRowInTable = row; // view row
		this.currentModelRow = table.convertRowIndexToModel(row); // model row

		// If the button should look selected
		// if (isSelected) {
		// ellipsisButton.setBackground(table.getSelectionBackground());
		// } else {
		// ellipsisButton.setBackground(UIManager.getColor("Button.background"));
		// }
		return ellipsisButton;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			// Activate editor on a single click to make the button responsive
			return ((MouseEvent) anEvent).getClickCount() >= 1;
		}
		return super.isCellEditable(anEvent); // Fallback for other event types
	}

	private void showActionPopupMenu(Component invoker) {
		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem editItem = new JMenuItem("Edit Details"); // Changed text slightly
		editItem.setActionCommand(ACTION_EDIT);
		editItem.addActionListener(this); // 'this' ButtonEditor handles menu item actions

		JMenuItem restockItem = new JMenuItem("Restock Item");
		restockItem.setActionCommand(ACTION_RESTOCK);
		restockItem.addActionListener(this);

		JMenuItem deleteItem = new JMenuItem("Archive"); // Clarified action
		deleteItem.setActionCommand(ACTION_DELETE);
		deleteItem.addActionListener(this);
		// Example:
		// deleteItem.setIcon(AssetManager.getOrLoadIcon("/icons/delete_icon.svg", 16,
		// 16));

		popupMenu.add(editItem);
		popupMenu.add(restockItem);
		popupMenu.add(new JSeparator());
		popupMenu.add(deleteItem);

		// Determine position carefully if the table scrolls
		if (currentTableRef != null) {
			java.awt.Rectangle cellRect = currentTableRef.getCellRect(currentRowInTable,
					currentTableRef.convertColumnIndexToView(InventoryTable.COL_ACTION), false);
			popupMenu.show(currentTableRef, cellRect.x, cellRect.y + cellRect.height);
		} else {
			popupMenu.show(invoker, 0, invoker.getHeight()); // Fallback position
		}
	}

	@Override
	public boolean stopCellEditing() {
		// This method is called when editing is to be stopped.
		// It should fire the "editing stopped" event.
		super.fireEditingStopped();
		return true; // Return true to indicate editing was successfully stopped.
	}
}