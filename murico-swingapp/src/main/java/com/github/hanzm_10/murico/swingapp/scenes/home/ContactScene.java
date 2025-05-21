package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Image;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserMetadata;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;

import net.miginfocom.swing.MigLayout;

public class ContactScene implements Scene {
	static Set<Integer> editedRows = new HashSet<>();
	private JPanel view;

	protected JPanel leftComponent;
	protected JPanel nameRows;
	protected JLabel conLabel;
	
	private JButton saveBtn;

	protected Image logoImage;

	private JTable usersTable;

	private DefaultTableModel usersTableModel;

	private JScrollPane usersScrollpane;

	private void displayUsersTable(DefaultTableModel tableModel) {
		UserMetadata[] users;
		try {
			users = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getUserDao().getAllUsers();

			for (var user : users) {
				Object[] rowData = { user._userId(), user.displayName(), user.email(), user.roles(),
						user.verificationStatus() };
				tableModel.addRow(rowData);
			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String getSceneName() {
		// TODO Auto-generated method stub
		return "contacts";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new RoundedPanel(20)) : view;
	}

	private void initializeUsersUI() {

		view.setBackground((Styles.SECONDARY_COLOR));
		view.setLayout(new MigLayout("fill", "[grow]", "[grow][grow]"));

		String[] columnNames = { "User ID", "Username", "Email Address", "Role", "Verification Status" };
		usersTableModel = new DefaultTableModel(columnNames, 0) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 3;
			}
		};

		usersTable = new JTable(usersTableModel);
		displayUsersTable(usersTableModel);

		usersScrollpane = new JScrollPane(usersTable);
		usersScrollpane.setBounds(38, 176, 973, 516);
		view.add(usersScrollpane, "cell 0 0,grow");
		
		saveBtn = new JButton("Save Changes");
		view.add(saveBtn, "cell 0 1, grow ,alignx center");
		
	}

	@Override
	public void onCreate() {
		System.out.println(getSceneName() + ": onCreate");
		initializeUsersUI();

	}

}
