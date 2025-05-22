package com.github.hanzm_10.murico.swingapp.scenes.home.contacts.components;

import java.awt.Font;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.comparators.NumberWithSymbolsComparator;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserContact;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.lib.table_models.NonEditableTableModel;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.IdRenderer;

import net.miginfocom.swing.MigLayout;

public class ContactsTable implements SceneComponent {

	public static final int COL_CONTACT_ID = 0;
	public static final int COL_CONTACT_DISPLAY_NAME = 1;
	public static final int COL_CONTACT_FIRST_NAME = 2;
	public static final int COL_CONTACT_LAST_NAME = 3;
	public static final int COL_CONTACT_ROLE = 4;

	private JPanel view;
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane;
	private TableRowSorter<TableModel> rowSorter;

	private AtomicReference<UserContact[]> userContacts = new AtomicReference<>(new UserContact[0]);
	private AtomicBoolean initialized = new AtomicBoolean(false);

	@Override
	public void destroy() {
		if (view != null) {
			view.removeAll();
			view = null;
		}

		if (table != null) {
			table.setModel(new DefaultTableModel());
			table = null;
		}

		if (scrollPane != null) {
			scrollPane.setViewportView(null);
			scrollPane = null;
		}

		userContacts.set(new UserContact[0]);
		initialized.set(false);
	}

	public JTable getTable() {
		return table;
	}

	public TableModel getTableModel() {
		return tableModel;
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

		view.setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));

		tableModel = new NonEditableTableModel();
		tableModel.setColumnCount(UserContact.getColumnNames().length);
		tableModel.setRowCount(0);
		tableModel.setColumnIdentifiers(UserContact.getColumnNames());

		table = new JTable(tableModel);
		table.setFont(table.getFont().deriveFont(Font.BOLD));
		table.setGridColor(Styles.TERTIARY_COLOR);
		table.setShowGrid(true);
		table.setRowHeight(40);
		table.setBackground(view.getBackground());

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		var header = table.getTableHeader();

		header.setBackground(Styles.SECONDARY_COLOR);
		header.setForeground(Styles.SECONDARY_FOREGROUND_COLOR);

		rowSorter = new TableRowSorter<TableModel>(tableModel);
		rowSorter.setComparator(COL_CONTACT_ID, new NumberWithSymbolsComparator());

		table.setRowSorter(rowSorter);

		var columnModel = table.getColumnModel();

		columnModel.getColumn(COL_CONTACT_ID).setPreferredWidth(50);
		columnModel.getColumn(COL_CONTACT_DISPLAY_NAME).setPreferredWidth(200);
		columnModel.getColumn(COL_CONTACT_FIRST_NAME).setPreferredWidth(100);
		columnModel.getColumn(COL_CONTACT_LAST_NAME).setPreferredWidth(100);
		columnModel.getColumn(COL_CONTACT_ROLE).setPreferredWidth(100);

		columnModel.getColumn(COL_CONTACT_ID).setCellRenderer(new IdRenderer());

		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		view.add(scrollPane, "grow");

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
			userContacts.set(factory.getUserDao().getUserContacts());
			SwingUtilities.invokeLater(this::updateTableModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateTableModel() {
		if (!initialized.get()) {
			initializeComponents();
		}

		tableModel.setRowCount(0);

		for (UserContact userContact : userContacts.get()) {
			var role = userContact.roles() == null ? "N/A" : userContact.roles();

			tableModel.addRow(new Object[] { userContact._userId(), userContact.displayName(),
					userContact.firstName() == null ? "" : userContact.firstName(),
					userContact.lastName() == null ? "" : userContact.lastName(), role });
		}

		view.revalidate();
	}

}
