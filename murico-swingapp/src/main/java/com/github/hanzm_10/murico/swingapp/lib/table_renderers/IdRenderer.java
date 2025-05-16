package com.github.hanzm_10.murico.swingapp.lib.table_renderers;

import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;

public class IdRenderer extends DefaultTableCellRenderer {
	public IdRenderer() {
		setHorizontalAlignment(CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		value = "#" + value;
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
