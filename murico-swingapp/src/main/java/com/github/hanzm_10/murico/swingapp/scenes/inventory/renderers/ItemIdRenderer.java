package com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class ItemIdRenderer extends DefaultTableCellRenderer {

    public ItemIdRenderer() {
        // Item ID usually looks better left-aligned unless purely numeric
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // The model should provide the value formatted as "#12345"
        // Superclass handles text and selection
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}