package com.github.hanzm_10.murico.swingapp.lib.table_renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.github.hanzm_10.murico.swingapp.lib.utils.NumberUtils;

public class CurrencyRenderer extends DefaultTableCellRenderer {
	public CurrencyRenderer() {
		setHorizontalAlignment(CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value == null) {
			return super.getTableCellRendererComponent(table, "0", isSelected, hasFocus, row, column);
		}

		value = "₱" + NumberUtils.formatter.format(value);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
