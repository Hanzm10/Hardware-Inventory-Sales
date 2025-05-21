package com.github.hanzm_10.murico.swingapp.lib.table_renderers;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.InventoryBreakdown;

public class InventoryRemarksRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	public InventoryRemarksRenderer() {
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setFont(getFont().deriveFont(Font.BOLD));

		if (value instanceof InventoryBreakdown.InventoryBreakdownRemarks valEnum) {
			switch (valEnum) {
			case CRITICAL -> setForeground(Styles.DANGER_COLOR);
			case LOW -> setForeground(Styles.WARNING_COLOR);
			case NORMAL -> setForeground(Styles.INFO_COLOR);
			case HIGH -> setForeground(Styles.SUCCESS_COLOR);
			}

			value = InventoryBreakdown.InventoryBreakdownRemarks.toString(valEnum);
		}

		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
