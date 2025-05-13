/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

// Import icons if you have them: import javax.swing.ImageIcon;

public class ProductNameRenderer extends DefaultTableCellRenderer {
	// TODO: Load icons for different product types/categories
	// private Icon defaultIcon = new
	// ImageIcon(getClass().getResource("/icons/default_product.png"));
	// private Icon fastenerIcon = new
	// ImageIcon(getClass().getResource("/icons/fastener.png"));

	public ProductNameRenderer() {
		setHorizontalAlignment(SwingConstants.LEFT);
		// setIcon(defaultIcon); // Set a default icon
		setIconTextGap(10); // Space between icon and text
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// Let superclass handle selection colors etc.
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// --- Placeholder for Icon Logic ---
		// try {
		// String category = (String) table.getValueAt(row,
		// InventoryScene.COL_CATEGORY);
		// if ("Fasteners".equals(category)) {
		// setIcon(fastenerIcon);
		// } else {
		// setIcon(defaultIcon);
		// }
		// } catch (Exception e) {
		// setIcon(defaultIcon); // Fallback on error
		// }
		// ---------------------------------

		setText(value != null ? value.toString() : ""); // Ensure text is set
		return this;
	}
}
