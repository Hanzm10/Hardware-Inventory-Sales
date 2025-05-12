package com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class CurrencyRenderer extends DefaultTableCellRenderer {
    // Use the same format as CheckoutPanel for consistency
    private static final DecimalFormat FORMAT = new DecimalFormat("₱ #,##0.00");

    public CurrencyRenderer() {
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Format the value before passing to superclass
        if (value instanceof BigDecimal) {
            value = FORMAT.format(value);
        } else if (value instanceof Number) {
             value = FORMAT.format(((Number) value).doubleValue());
        }
        // Let the superclass handle selection colors, alignment etc.
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}