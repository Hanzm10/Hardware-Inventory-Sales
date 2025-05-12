package com.github.hanzm_10.murico.swingapp.scenes.inventory.editors;

import com.github.hanzm_10.murico.swingapp.scenes.inventory.InventoryScene; // Need reference to scene
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private final JButton button;
    private final InventoryScene parentScene; // Reference to call back
    private int currentRow;
    protected static final String EDIT_ACTION = "edit";

    public ButtonEditor(InventoryScene parentScene, TableCellRenderer renderer) {
        this.parentScene = parentScene;
        this.button = new JButton();
        this.button.setOpaque(false); // Match renderer
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false);
        this.button.setFocusPainted(false);

        // Copy appearance from the renderer button
        if (renderer instanceof JButton) {
            this.button.setIcon(((JButton)renderer).getIcon());
            this.button.setText(((JButton)renderer).getText());
            this.button.setPreferredSize(((JButton)renderer).getPreferredSize());
            this.button.setHorizontalAlignment(((JButton)renderer).getHorizontalAlignment());
            this.button.setVerticalAlignment(((JButton)renderer).getVerticalAlignment());
            this.button.setToolTipText(((JButton)renderer).getToolTipText());
        } else {
            this.button.setText("...");
        }

        this.button.addActionListener(this);
        this.button.setActionCommand(EDIT_ACTION);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.currentRow = row;
        // Appearance is set in constructor, selection doesn't usually change editor look much
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return ""; // Action is performed, no value change
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (EDIT_ACTION.equals(e.getActionCommand())) {
            // IMPORTANT: Stop editing before calling the dialog
            fireEditingStopped();
            // Call back to the InventoryScene to open the dialog
            parentScene.openEditItemDialog(currentRow);
        }
    }

    // Needed for AbstractCellEditor
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true; // Allow editor activation
    }

    // Optional: Improve click detection if needed
    // @Override
    // public boolean stopCellEditing() {
    //     return super.stopCellEditing();
    // }

    // @Override
    // protected void fireEditingStopped() {
    //     super.fireEditingStopped();
    // }
}