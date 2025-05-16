package com.github.hanzm_10.murico.swingapp.lib.combobox_renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

public class PlaceholderRenderer extends DefaultListCellRenderer {
	private JComboBox<?> comboBox;

	public PlaceholderRenderer(JComboBox<?> comboBox) {
		super();
		this.comboBox = comboBox;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (index == 0 || (index == -1 && comboBox.getSelectedIndex() == 0)) {
			c.setForeground(Color.GRAY); // make placeholder gray
		} else {
			c.setForeground(Color.BLACK);
		}
		return c;
	}
}
