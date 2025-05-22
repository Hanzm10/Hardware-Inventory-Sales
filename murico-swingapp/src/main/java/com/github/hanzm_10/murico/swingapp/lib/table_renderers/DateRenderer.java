package com.github.hanzm_10.murico.swingapp.lib.table_renderers;

import java.awt.Component;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateRenderer extends DefaultTableCellRenderer {
	public DateRenderer() {
		setHorizontalAlignment(CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		value = ((Timestamp) value).toLocalDateTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
