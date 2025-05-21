package com.github.hanzm_10.murico.swingapp.scenes.home.contactScene;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserMetadata;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.service.email_sender.EmailSender;

public class Contact {
	public void displayUsersTableForAdmin(DefaultTableModel tableModel, Set<Integer> editedRows) {
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
		
		tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                editedRows.add(e.getFirstRow());
            }
        });

	}
	
	public void displayUsersTableForNotAdmin(DefaultTableModel tableModel, Set<Integer> editedRows) {
		UserMetadata[] users;
		try {
			users = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getUserDao().getAllUsers();

			for (var user : users) {
				Object[] rowData = { user._userId(), user.displayName(), user.roles(),
						user.verificationStatus() };
				tableModel.addRow(rowData);
			}
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void saveChanges (DefaultTableModel model, Set<Integer> editedRows) {
		System.out.println("Saving changes...");
		try {
	        var conn = MySqlFactoryDao.createConnection();
	        var updateQuery = MySqlQueryLoader.getInstance().get("update_userScenes_table", "accounts", MySqlQueryLoader.SqlQueryType.UPDATE);
	        var updateStmt = conn.prepareStatement(updateQuery);
	        String userEmailAdd = null;

	        if (editedRows.isEmpty()) {
	        	return;
	        }

	        for (int row : editedRows) {
	            int id = (int) model.getValueAt(row, 0);
	            Integer roleID = null;
	            String newRole = (String) model.getValueAt(row, 3);
	            System.out.println("New role: " + newRole);
	            if (newRole.equalsIgnoreCase("admin")) {
	            	roleID = 1;
	            } else if (newRole.equalsIgnoreCase("clerk")) {
	            	roleID = 2;
	            } else if (newRole.equalsIgnoreCase("purchasing officer")) {
	            	roleID = 3;
	            } else if (newRole.equalsIgnoreCase("logistics")) {
	            	roleID = 4;
	            }
	            else {
	            	roleID = null;
	            }
	            System.out.println("Role ID: " + roleID);
	            
	            userEmailAdd = (String) model.getValueAt(row, 2);
	            
	            if (userEmailAdd != null) {
		            EmailSender.emailSender(userEmailAdd);
		            System.out.println("Email sent to: " + userEmailAdd);
		        }

	            updateStmt.setInt(1, roleID);
	            updateStmt.setInt(2, id);
	            updateStmt.addBatch();
	        }

	        updateStmt.executeBatch();
	        updateStmt.close();

	        editedRows.clear();
	       
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	      
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}