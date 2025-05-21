package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.comparators.NumberWithSymbolsComparator;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.InventoryBreakdown;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.lib.table_models.NonEditableTableModel;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.IdRenderer;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.InventoryRemarksRenderer;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class InventoryReportTable implements SceneComponent {

	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryReportTable.class);

	public static final int COL_ITEM_ID = 0;
	public static final int COL_ITEM_NAME = 1;
	public static final int COL_ITEM_CATEGORY = 2;
	public static final int COL_ITEM_PACKAGING = 3;
	public static final int COL_ITEM_INITIAL_QUANTITY = 4;
	public static final int COL_ITEM_SOLD_QUANTITY = 5;
	public static final int COL_ITEM_RESTOCK_QUANTITY = 6;
	public static final int COL_ITEM_QUANTITY = 7;
	public static final int COL_ITEM_REMARKS = 8;

	private JPanel view;

	private JLabel title;
	private DefaultTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;

	private AtomicBoolean initialized = new AtomicBoolean(false);
	private AtomicReference<InventoryBreakdown[]> inventoryBreakdown = new AtomicReference<>(new InventoryBreakdown[0]);

	private void attachComponents() {
		view.add(title);
		view.add(scrollPane, "grow");
	}

	private void createComponents() {
		view.setLayout(new MigLayout("insets 0, flowy", "[grow]", "[]8[grow]"));

		title = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Inventory Breakdown"), 24, Styles.PRIMARY_COLOR);

		tableModel = new NonEditableTableModel();
		table = new JTable(tableModel);
		scrollPane = new JScrollPane(table);

		var cellRenderer = new DefaultTableCellRenderer();
		var sorter = new TableRowSorter<TableModel>(tableModel);
		var comparator = new NumberWithSymbolsComparator();

		cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (var columnNames : InventoryBreakdown.getColumnNames()) {
			tableModel.addColumn(columnNames);
		}

		for (int i = 0; i < InventoryBreakdown.getColumnNames().length; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
		}

		sorter.setComparator(COL_ITEM_ID, comparator);
		sorter.setComparator(COL_ITEM_INITIAL_QUANTITY, comparator);
		sorter.setComparator(COL_ITEM_SOLD_QUANTITY, comparator);
		sorter.setComparator(COL_ITEM_RESTOCK_QUANTITY, comparator);
		sorter.setComparator(COL_ITEM_QUANTITY, comparator);

		table.setRowSorter(sorter);

		var header = table.getTableHeader();

		header.setBackground(Styles.SECONDARY_COLOR);
		header.setForeground(Styles.SECONDARY_FOREGROUND_COLOR);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		var columnModel = table.getColumnModel();
		columnModel.getColumn(COL_ITEM_ID).setCellRenderer(new IdRenderer());
		columnModel.getColumn(COL_ITEM_REMARKS).setCellRenderer(new InventoryRemarksRenderer());
	}

	@Override
	public void destroy() {
	}

	@Override
	public JPanel getView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void initializeComponents() {
		if (initialized.get()) {
			return;
		}

		createComponents();
		attachComponents();

		initialized.set(true);
	}

	@Override
	public boolean isInitialized() {
		return initialized.get();
	}

	@Override
	public void performBackgroundTask() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			inventoryBreakdown.set(factory.getItemDao().getInventoryBreakdowns());
			SwingUtilities.invokeLater(this::updateTable);
		} catch (Exception e) {
			LOGGER.warning("Failed to get inventory breakdown: " + e.getMessage());
		}
	}

	private void updateTable() {
		if (!initialized.get()) {
			initializeComponents();
		}

		var data = inventoryBreakdown.get();
		tableModel.setRowCount(0);

		for (var item : data) {
			tableModel.addRow(item.toObjectArray());
		}
	}

}
