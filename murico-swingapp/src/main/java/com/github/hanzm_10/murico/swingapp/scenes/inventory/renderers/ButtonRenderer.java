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
package com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers;

// Import your AssetManager
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;

public class ButtonRenderer extends JButton implements TableCellRenderer {

	public ButtonRenderer() {
		setOpaque(true);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setFocusPainted(false);

		// Define desired size for the icon within the button
		int iconWidth = 16;
		int iconHeight = 16;

		try {
			// Assuming AssetManager.getOrLoadIcon is updated to take width and height
			ImageIcon icon = AssetManager.getOrLoadIcon("/icons/edit_button.svg");
			if (icon != null) {
				setIcon(icon);
			} else {
				System.err.println("AssetManager: Edit button icon not found for table button! Using text fallback.");
				setText("...");
			}
		} catch (Exception e) {
			System.err.println("Error loading Edit button icon via AssetManager for table button: " + e.getMessage());
			e.printStackTrace();
			setText("...");
		}

		// Set preferred size for the button itself, can be slightly larger than icon
		// for padding
		setPreferredSize(new Dimension(iconWidth - 8, iconHeight - 8));
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		setToolTipText("Edit Item Details");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// Background handling for selection or focus (optional for icon buttons)
		// if (isSelected) {
		// setBackground(table.getSelectionBackground());
		// } else {
		// setBackground(UIManager.getColor("Button.background")); // Or
		// table.getBackground()
		// }
		// if (hasFocus) {
		// setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		// } else {
		// setBorder(null);
		// }
		return this;
	}
}
