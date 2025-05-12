package com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers;

import java.awt.Component;
import javax.swing.Icon; // For potential icons
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
// Import icons if you have them: import javax.swing.ImageIcon;

public class ProductNameRenderer extends DefaultTableCellRenderer {
    // TODO: Load icons for different product types/categories
    // private Icon defaultIcon = new ImageIcon(getClass().getResource("/icons/default_product.png"));
    // private Icon fastenerIcon = new ImageIcon(getClass().getResource("/icons/fastener.png"));

    public ProductNameRenderer() {
        setHorizontalAlignment(SwingConstants.LEFT);
        // setIcon(defaultIcon); // Set a default icon
        setIconTextGap(10); // Space between icon and text
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Let superclass handle selection colors etc.
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // --- Placeholder for Icon Logic ---
        // try {
        //     String category = (String) table.getValueAt(row, InventoryScene.COL_CATEGORY);
        //     if ("Fasteners".equals(category)) {
        //         setIcon(fastenerIcon);
        //     } else {
        //        setIcon(defaultIcon);
        //     }
        // } catch (Exception e) {
        //     setIcon(defaultIcon); // Fallback on error
        // }
        // ---------------------------------

        setText(value != null ? value.toString() : ""); // Ensure text is set
        return this;
    }
}