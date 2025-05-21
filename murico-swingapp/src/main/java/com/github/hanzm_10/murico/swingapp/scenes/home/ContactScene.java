package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import java.awt.Cursor;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.contactScene.Contact;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
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
	private boolean uiInitialized = false;



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
		showTable();
	}

	@Override
	public void onCreate() {
		if(!uiInitialized) {
			try {
				initializeUsersUI();
				uiInitialized = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	@Override
	public boolean onDestroy() {
		System.out.println(getSceneName() + ": onDestroy");
		uiInitialized = false;
		return true;
	}
	@Override
	public void onHide() {
		System.out.println(getSceneName() + ": onHide");
		
	}
	
	@Override
	public void onShow() {
		System.out.println(getSceneName() + ": onShow");
	
		
	}
	public void showTable() {
		Contact contact = new Contact();
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();
		var role = loggedInUser.roles();
		
		view.setBackground((Styles.SECONDARY_COLOR));
		view.setLayout(new MigLayout("fill", "[grow]", "[650][grow]"));
		
		JPanel tablePanel = new RoundedPanel(20);
		tablePanel.setBackground((Color.WHITE));
		tablePanel.setLayout(new MigLayout("fill", "[grow]", "[grow]"));
		view.add(tablePanel, "cell 0 0,grow");
		
		
		JPanel buttonPnl = new JPanel();
		buttonPnl.setLayout(new MigLayout("insets 0, fillx", "[grow]"));
		buttonPnl.setBackground(Styles.SECONDARY_COLOR);
		view.add(buttonPnl, "cell 0 1,grow");
		
		saveBtn = new JButton("Save Changes");
		saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buttonPnl.add(saveBtn, "align center");
		if (role.equalsIgnoreCase("admin")) {
			String[] columnNames = { "User ID", "Username", "Email Address", "Role", "Verification Status" };
			usersTableModel = new DefaultTableModel(columnNames, 0) {

				@Override
				public boolean isCellEditable(int row, int column) {
					return column == 3;
				}
			};

			usersTable = new JTable(usersTableModel);
			contact.displayUsersTableForAdmin(usersTableModel, editedRows);
			usersScrollpane = new JScrollPane(usersTable);
			tablePanel.add(usersScrollpane, "cell 0 0, grow");
			saveBtn.setEnabled(true);
		} else {
			String[] columnNames = { "User ID", "Username", "Role", "Verification Status" };
			usersTableModel = new DefaultTableModel(columnNames, 0) {};

			usersTable = new JTable(usersTableModel);
			contact.displayUsersTableForNotAdmin(usersTableModel, editedRows);
			usersScrollpane = new JScrollPane(usersTable);
			tablePanel.add(usersScrollpane, "cell 0 0, grow");
			saveBtn.setEnabled(false);			
		}
		
		saveBtn.addActionListener(e -> {
			contact.saveChanges(usersTableModel, editedRows);
			JOptionPane.showMessageDialog(view, "Changes saved successfully!");
		});
		
		if (view != null) {
            view.revalidate(); 
            view.repaint();    
        }

	}

}

// 		
