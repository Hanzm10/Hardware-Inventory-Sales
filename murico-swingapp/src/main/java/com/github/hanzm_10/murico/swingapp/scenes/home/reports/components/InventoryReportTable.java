package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;

public class InventoryReportTable implements SceneComponent {

	private JPanel view;

	private JLabel title;
	private DefaultTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;

	@Override
	public void destroy() {
	}

	@Override
	public JPanel getView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void initializeComponents() {

	}

	@Override
	public boolean isInitialized() {
		return false;
	}

}
