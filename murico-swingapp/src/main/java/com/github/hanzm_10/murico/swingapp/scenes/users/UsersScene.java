package com.github.hanzm_10.murico.swingapp.scenes.users;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;


public class UsersScene extends JPanel implements Scene{
	
	private JTable usersTable;
	private DefaultTableModel usersTableModel;
	private JScrollPane usersScrollpane;
	private JButton saveUsersBtn;
	private boolean uiInitialized = false;
	private JPanel usersPnl;
	static Set<Integer> editedRows = new HashSet<>();
	
	public UsersScene() {
		setLayout(new BorderLayout(0,0));
		System.out.println("UsersScene instance created.");
	}
	
	@Override
	public String getSceneName() {
		// TODO Auto-generated method stub
		return "users";
	}

	@Override
	public JPanel getSceneView() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void onCreate() {
		if(!uiInitialized) {
			initializeUsersUI();
			uiInitialized = true;
		}
		
	}
	@Override
	public void onShow() {
		System.out.println(getName() + ": onShow");
        // Ensure UI is built before loading data
        if (!uiInitialized) {
            onCreate(); // Call onCreate if not already initialized
        }
        
	}
	
	
	@Override
	public void onHide() {
	        System.out.println(getName() + ": onHide");
	        // Pause activities if needed
	    }
	 @Override
	    public boolean onDestroy() {
		 usersTable = null;
		 usersTableModel = null;
		 usersScrollpane = null;
		 removeAll();
		 uiInitialized = false;
		return true;
	 }
	 
	private void initializeUsersUI() {
		usersPnl = new JPanel();
		usersPnl.setBackground(Color.white);
		this.add(usersPnl, BorderLayout.CENTER); 
		
		String[] columnNames = {"User ID", "Username","Email Address","Role"};
		usersTableModel = new DefaultTableModel(columnNames, 0) {
		
		@Override 
		public boolean isCellEditable(int row, int column) {
			return column == 3;
			}
		};
		
		usersTable = new JTable(usersTableModel);
		displayUsersTable(usersTableModel);
		
		usersScrollpane = new JScrollPane(usersTable);
		usersScrollpane.setBounds(38,176,973,516);
		usersPnl.add(usersScrollpane);
		
		saveUsersBtn = new JButton("Save Changes");
		//saveUsersBtn.setLocation(393, 735);
		saveUsersBtn.setSize(237, 43);
		this.add(saveUsersBtn, BorderLayout.SOUTH);
		
		
	}
	
	private void displayUsersTable(DefaultTableModel tableModel) {
		//var query = MySqlQueryLoader.getInstance().get("get_users_table", "users", SqlQueryType.SELECT);
		try{
			var conn = MySqlFactoryDao.createConnection();
			var query = MySqlQueryLoader.getInstance().get("get_users_table", "users", SqlQueryType.SELECT);
				
			var statement = conn.prepareStatement(query);
			var resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				int id = resultSet.getInt("_user_id");
				String username = resultSet.getString("user_display_name");
				String emailAdd = resultSet.getString("user_email");
                String role = resultSet.getString("user_role");

                tableModel.addRow(new Object[]{id, username, emailAdd, role});
            }
			resultSet.close();
			statement.close();
			conn.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}catch(IOException e1) {
			e1.printStackTrace();	}
		tableModel.addTableModelListener(e -> {
			if(e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
				editedRows.add(e.getFirstRow());
			}
		});
	}
	
	public static void saveUser(DefaultTableModel model) {
	    try {
	        String updateQuery = MySqlQueryLoader.getInstance().get("update_users_table", "users", SqlQueryType.UPDATE);
	        PreparedStatement updateStmt = MySqlFactoryDao.createConnection().prepareStatement(updateQuery);
	        String userEmailAdd = null;

	        if (editedRows.isEmpty()) {
	            //Register.showMessage("No changes to save.");
	            return;
	        }

	        for (int row : editedRows) {
	            int id = (int) model.getValueAt(row, 0);
	            String newRole = (String) model.getValueAt(row, 3);
	            userEmailAdd = (String) model.getValueAt(row, 2);
	            
	            if (userEmailAdd != null) {
		            //EmailSender.emailSender(userEmailAdd);
		        }

	            updateStmt.setString(1, newRole);
	            updateStmt.setInt(2, id);
	            updateStmt.addBatch();
	        }

	        updateStmt.executeBatch();
	        updateStmt.close();

	        editedRows.clear();
	        System.out.println("Changes saved successfully");
	        //Register.showMessage("Changes saved successfully\nThe Email has been sent to the respective users.");
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        System.out.println("Error saving changes");
	        //Register.showMessage("Error saving changes");
	    }catch(IOException er) {
	    	er.printStackTrace();
	    }
	}

}

