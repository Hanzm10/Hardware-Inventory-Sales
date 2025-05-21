package com.github.hanzm_10.murico.swingapp.scenes.home.contactScene; // Renamed package

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import com.github.hanzm_10.murico.swingapp.lib.auth.MuricoCrypt;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserMetadata;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.utils.CharUtils;

public class UserManagement {

	public static String[] getAllRoleNames() {
		return new String[] { "Clerk", "Purchasing Officer", "Logistics" };
	}

	// Helper method to map role name to ID
	public static Integer getRoleIdFromName(String roleName) {
		if (roleName == null) {
			return null;
		}
		switch (roleName.trim().toLowerCase()) {
		case "admin":
			return 1;
		case "clerk":
			return 2;
		case "purchasing officer":
			return 3;
		case "logistics":
			return 4;
		default:
			System.err.println("Unknown role name: " + roleName);
			return null;
		}
	}

	// Helper method to map role ID to name (useful for populating JComboBox)
	public static String getRoleNameFromId(Integer roleId) {
		if (roleId == null) {
			return ""; // Or handle as an error
		}
		switch (roleId) {
		case 1:
			return "Admin";
		case 2:
			return "Clerk";
		case 3:
			return "Purchasing Officer";
		case 4:
			return "Logistics";
		default:
			System.err.println("Unknown role ID: " + roleId);
			return "Unknown Role";
		}
	}

	public boolean addUser(String displayName, String email, char[] password, String roleName) {
		var hashedPassword = new MuricoCrypt().hash(password);

		Integer roleId = getRoleIdFromName(roleName);

		if (roleId == null) {
			System.err.println("Invalid role provided for new user: " + roleName);
			return false;
		}

		String insertUserSql = "INSERT INTO users (display_name, display_image) VALUES (?, ?)";
		// Assuming _user_id is auto-increment or you get it back
		String insertAccountSql = "INSERT INTO accounts (_user_id, email, password_hash, password_salt, verification_status, verified_at) VALUES (?, ?, ?, ?, 'verified', CURRENT_TIMESTAMP)";
		// For new users, maybe 'pending' and verified_at = NULL initially. Let's stick
		// to 'verified' like your seed.
		String insertUserRoleSql = "INSERT INTO users_roles (_user_id, _role_id) VALUES (?, ?)";

		Connection conn = null;
		try {
			conn = MySqlFactoryDao.createConnection();
			conn.setAutoCommit(false);

			long userId = -1;

			// Insert into users table
			try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
				userStmt.setString(1, displayName);
				userStmt.setString(2, "profile_picture/default.png"); // Default image
				userStmt.executeUpdate();

				try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						userId = generatedKeys.getLong(1);
					} else {
						throw new SQLException("Creating user failed, no ID obtained.");
					}
				}
			}

			// Insert into accounts table
			try (PreparedStatement accountStmt = conn.prepareStatement(insertAccountSql)) {
				accountStmt.setLong(1, userId);
				accountStmt.setString(2, email);
				accountStmt.setString(3, CharUtils.toBase64(hashedPassword.hashedString()));
				accountStmt.setString(4, hashedPassword.salt().toBase64());
				accountStmt.executeUpdate();
			}

			// Insert into users_roles table
			try (PreparedStatement roleStmt = conn.prepareStatement(insertUserRoleSql)) {
				roleStmt.setLong(1, userId);
				roleStmt.setInt(2, roleId);
				roleStmt.executeUpdate();
			}

			conn.commit();
			System.out.println("User added successfully: " + displayName);
			return true;

		} catch (SQLException ex) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ex.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean deleteUser(int userId) {
		// Order of deletion matters due to foreign key constraints
		// users_roles -> accounts -> users
		String deleteUserRolesSql = "DELETE FROM users_roles WHERE _user_id = ?";
		String deleteAccountSql = "DELETE FROM accounts WHERE _user_id = ?";
		String deleteUserSql = "DELETE FROM users WHERE _user_id = ?";

		Connection conn = null;
		try {
			conn = MySqlFactoryDao.createConnection();
			conn.setAutoCommit(false);

			// Delete from users_roles
			try (PreparedStatement stmt = conn.prepareStatement(deleteUserRolesSql)) {
				stmt.setInt(1, userId);
				stmt.executeUpdate();
			}
			// Delete from accounts
			try (PreparedStatement stmt = conn.prepareStatement(deleteAccountSql)) {
				stmt.setInt(1, userId);
				stmt.executeUpdate();
			}
			// Delete from users
			try (PreparedStatement stmt = conn.prepareStatement(deleteUserSql)) {
				stmt.setInt(1, userId);
				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
					// This means the user didn't exist or was already deleted,
					// but FKs might have prevented earlier steps if it didn't exist in users
					System.out.println("User with ID " + userId + " not found in users table, or already deleted.");
					// Depending on strictness, you might rollback or consider it "successful" if
					// the goal is absence
				}
			}

			conn.commit();
			System.out.println("User deleted successfully: ID " + userId);
			return true;

		} catch (SQLException ex) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ex.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Method to fetch all users for display
	public List<UserMetadata> getAllUsersForDisplay() throws SQLException, IOException {
		List<UserMetadata> userList = new ArrayList<>();
		// Assuming getUserDao().getAllUsers() fetches all necessary data including
		// roles as a string
		UserMetadata[] usersArray = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getUserDao()
				.getAllUsers();
		for (UserMetadata user : usersArray) {
			userList.add(user);
		}
		return userList;
	}

	// Method to save changes made to roles directly in the table (if kept)
	public boolean saveRoleChanges(DefaultTableModel model, Set<Integer> editedRows) {
		if (editedRows.isEmpty()) {
			System.out.println("No role changes to save from table.");
			return true; // No changes, so technically successful
		}
		System.out.println("Saving role changes from table...");

		// IMPORTANT: This query needs to correctly handle users_roles.
		// If a user can have multiple roles, and you're setting ONE primary role:
		// 1. DELETE FROM users_roles WHERE _user_id = ? AND _role_id =
		// <old_role_id_if_known_or_specific_managed_role>
		// 2. INSERT INTO users_roles (_user_id, _role_id) VALUES (?, ?)
		// OR (simpler if this UI only manages one role per user):
		// 1. DELETE FROM users_roles WHERE _user_id = ?
		// 2. INSERT INTO users_roles (_user_id, _role_id) VALUES (?, ?)
		// The below assumes the simpler "delete all existing, insert new" for the
		// managed role.
		// This needs a transaction.

		String deleteUserRolesQuery = "DELETE FROM users_roles WHERE _user_id = ?";
		String insertUserRoleQuery = "INSERT INTO users_roles (_user_id, _role_id) VALUES (?, ?)";

		try (Connection conn = MySqlFactoryDao.createConnection()) {
			conn.setAutoCommit(false); // Start transaction

			try (PreparedStatement deleteStmt = conn.prepareStatement(deleteUserRolesQuery);
					PreparedStatement insertStmt = conn.prepareStatement(insertUserRoleQuery)) {

				for (int row : editedRows) {
					int userId = (int) model.getValueAt(row, 0); // User ID
					String newRoleName = (String) model.getValueAt(row, 3); // Role name from table (admin view)
					// String userEmail = (String) model.getValueAt(row, 2); // Email for
					// notification

					Integer newRoleId = getRoleIdFromName(newRoleName);

					if (newRoleId == null) {
						System.err.println("Invalid role: " + newRoleName + " for user ID: " + userId
								+ ". Skipping update for this row.");
						continue;
					}

					// Delete existing roles for the user
					deleteStmt.setInt(1, userId);
					deleteStmt.addBatch();

					// Insert the new role
					insertStmt.setInt(1, userId);
					insertStmt.setInt(2, newRoleId);
					insertStmt.addBatch();

					// System.out.println("Scheduled role update for User ID: " + userId + " to
					// Role: " + newRoleName);
					// if (userEmail != null && !userEmail.trim().isEmpty()) {
					// EmailSender.emailSender(userEmail);
					// System.out.println("Email notification for role change initiated for: " +
					// userEmail);
					// }
				}

				deleteStmt.executeBatch();
				insertStmt.executeBatch();
				conn.commit();
				editedRows.clear();
				System.out.println("Role changes saved successfully.");
				return true;

			} catch (SQLException ex) {
				conn.rollback(); // Rollback on error
				ex.printStackTrace();
				// Consider throwing a custom exception or returning false with a message
				return false;
			} finally {
				conn.setAutoCommit(true); // Reset auto-commit
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean updateUser(int userId, String displayName, String email, String roleName) {
		Integer roleId = getRoleIdFromName(roleName);
		if (roleId == null && roleName != null && !roleName.isEmpty()) { // Allow role to be null if not changing
			System.err.println("Invalid role provided for user update: " + roleName);
			return false;
		}

		String updateUserSql = "UPDATE users SET display_name = ? WHERE _user_id = ?";
		String updateAccountSql = "UPDATE accounts SET email = ? WHERE _user_id = ?";
		// Role update is more complex if multiple roles are allowed
		String deleteUserRolesSql = "DELETE FROM users_roles WHERE _user_id = ?";
		String insertUserRoleSql = "INSERT INTO users_roles (_user_id, _role_id) VALUES (?, ?)";

		Connection conn = null;
		try {
			conn = MySqlFactoryDao.createConnection();
			conn.setAutoCommit(false);

			// Update users table
			try (PreparedStatement userStmt = conn.prepareStatement(updateUserSql)) {
				userStmt.setString(1, displayName);
				userStmt.setInt(2, userId);
				userStmt.executeUpdate();
			}

			// Update accounts table
			try (PreparedStatement accountStmt = conn.prepareStatement(updateAccountSql)) {
				accountStmt.setString(1, email);
				accountStmt.setInt(2, userId);
				accountStmt.executeUpdate();
			}

			// Update users_roles table (if role is being changed)
			if (roleId != null) {
				try (PreparedStatement deleteRolesStmt = conn.prepareStatement(deleteUserRolesSql)) {
					deleteRolesStmt.setInt(1, userId);
					deleteRolesStmt.executeUpdate();
				}
				try (PreparedStatement insertRoleStmt = conn.prepareStatement(insertUserRoleSql)) {
					insertRoleStmt.setInt(1, userId);
					insertRoleStmt.setInt(2, roleId);
					insertRoleStmt.executeUpdate();
				}
			}

			conn.commit();
			System.out.println("User updated successfully: " + displayName);
			return true;

		} catch (SQLException ex) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ex.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}