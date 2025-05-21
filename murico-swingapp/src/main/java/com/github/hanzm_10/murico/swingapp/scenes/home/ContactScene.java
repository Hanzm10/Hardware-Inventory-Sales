package com.github.hanzm_10.murico.swingapp.scenes.home; // Renamed package

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserMetadata;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.contactScene.UserDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.contactScene.UserManagement;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel; // Assuming this exists

import net.miginfocom.swing.MigLayout;

public class ContactScene implements Scene {
	private static Set<Integer> tableEditedRows = new HashSet<>();
	private JPanel view;
	private JTable usersTable;
	private DefaultTableModel usersTableModel;

	private JButton addUserButton, editUserButton, deleteUserButton, refreshButton, saveTableEditsButton;
	private UserManagement userManagement;

	private void addUser(ActionEvent e) {
		if (!SessionManager.getInstance().isAdmin()) {
			JOptionPane.showMessageDialog(view, "You do not have permission to add users.", "Permission Denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		UserDialog dialog = new UserDialog(SwingUtilities.getWindowAncestor(view), "Add New User", null);
		dialog.setVisible(true);

		if (dialog.isSaved()) {
			loadUsersData();
		}
	}

	private void deleteUser(ActionEvent e) {
		if (!SessionManager.getInstance().isAdmin()) {
			JOptionPane.showMessageDialog(view, "You do not have permission to delete users.", "Permission Denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int selectedRow = usersTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Please select a user to delete.", "No Selection",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int userId = (int) usersTableModel.getValueAt(usersTable.convertRowIndexToModel(selectedRow), 0);
		String userName = (String) usersTableModel.getValueAt(usersTable.convertRowIndexToModel(selectedRow), 1);

		if (userId == SessionManager.getInstance().getLoggedInUser()._userId()) {
			JOptionPane.showMessageDialog(view, "You cannot delete your own account.", "Permission Denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirmation = JOptionPane.showConfirmDialog(view,
				"Are you sure you want to delete user: " + userName + " (ID: " + userId + ")?", "Confirm Deletion",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmation == JOptionPane.YES_OPTION) {
			if (userManagement.deleteUser(userId)) {
				JOptionPane.showMessageDialog(view, "User " + userName + " deleted successfully.",
						"Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
				loadUsersData(); // Refresh table
			} else {
				JOptionPane.showMessageDialog(view, "Failed to delete user " + userName + ".", "Deletion Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void editUser(ActionEvent e) {
		if (!SessionManager.getInstance().isAdmin()) {
			JOptionPane.showMessageDialog(view, "You do not have permission to edit users.", "Permission Denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int selectedRow = usersTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Please select a user to edit.", "No Selection",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int userId = (int) usersTableModel.getValueAt(usersTable.convertRowIndexToModel(selectedRow), 0);
		UserMetadata userToEdit = null;
		try {
			List<UserMetadata> currentUsers = userManagement.getAllUsersForDisplay(); // Inefficient, ideally get by ID
			for (UserMetadata u : currentUsers) {
				if (u._userId() == userId) {
					userToEdit = u;
					break;
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view, "Error fetching user details: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (userToEdit == null) {
			JOptionPane.showMessageDialog(view, "Could not find user details for ID: " + userId, "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (userToEdit.roles() != null && userToEdit.roles().contains("admin")) {
			JOptionPane.showMessageDialog(view, "You cannot edit an admin user.", "Permission Denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		UserDialog dialog = new UserDialog(SwingUtilities.getWindowAncestor(view), "Edit User", userToEdit);
		dialog.setVisible(true);

		if (dialog.isSaved()) {
			loadUsersData(); // Refresh table
		}
	}

	@Override
	public String getSceneName() {
		return "Users Management";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? view = new RoundedPanel(16) : view; // Lazy initialization
	}

	private void initializeUI() throws URISyntaxException {
		// --- Action Button Panel ---
		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		actionPanel.setBackground(Styles.SECONDARY_COLOR); // Match parent

		addUserButton = new JButton("Add User");
		addUserButton.setIcon(AssetManager.getOrLoadIcon("icons/add2.svg")); // Placeholder icon
		addUserButton.addActionListener(this::addUser);
		actionPanel.add(addUserButton);

		editUserButton = new JButton("Edit User");
		editUserButton.setIcon(AssetManager.getOrLoadIcon("icons/edit2.svg")); // Placeholder
		editUserButton.addActionListener(this::editUser);
		actionPanel.add(editUserButton);

		deleteUserButton = new JButton("Delete User");
		deleteUserButton.setIcon(AssetManager.getOrLoadIcon("icons/delete.svg")); // Placeholder
		deleteUserButton.addActionListener(this::deleteUser);
		actionPanel.add(deleteUserButton);

		refreshButton = new JButton("Refresh");
		refreshButton.setIcon(AssetManager.getOrLoadIcon("icons/refresh.svg")); // Placeholder icon
		refreshButton.addActionListener(_ -> loadUsersData());
		actionPanel.add(refreshButton);

		view.add(actionPanel, "dock north, gapbottom 10");

		// --- Users Table ---
		String[] columnNamesAdmin = { "ID", "Display Name", "Email", "Role(s)", "Verified" };
		String[] columnNamesNonAdmin = { "ID", "Display Name", "Role(s)", "Verified" };

		usersTableModel = new DefaultTableModel(
				SessionManager.getInstance().isAdmin() ? columnNamesAdmin : columnNamesNonAdmin, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// Allow editing only 'Role(s)' column by admin directly in table
				return SessionManager.getInstance().isAdmin() && column == 3;
			}
		};
		usersTable = new JTable(usersTableModel);
		setupTableAppearance();

		if (SessionManager.getInstance().isAdmin()) {
			// Add listener for direct table edits (roles)
			usersTableModel.addTableModelListener(e -> {
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) { // Role column for admin
					tableEditedRows.add(e.getFirstRow());
					saveTableEditsButton.setEnabled(!tableEditedRows.isEmpty());
				}
			});
		}

		JScrollPane scrollPane = new JScrollPane(usersTable);
		scrollPane.getViewport().setBackground(Color.WHITE); // Ensure viewport bg is white
		view.add(scrollPane, "cell 0 1, grow");

		// --- Save Table Edits Button Panel (only for admins if direct table edit is
		// enabled) ---
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.setBackground(Styles.SECONDARY_COLOR);

		saveTableEditsButton = new JButton("Save Role Changes in Table");
		saveTableEditsButton.addActionListener(this::saveTableEdits);
		saveTableEditsButton.setEnabled(false); // Initially disabled

		if (SessionManager.getInstance().isAdmin()) { // Only add this button if admin can edit table directly
			bottomPanel.add(saveTableEditsButton);
		}
		view.add(bottomPanel, "cell 0 2, dock south");

		updateButtonStates(); // Set initial button enabled/disabled state
	}

	private void loadUsersData() {
		usersTableModel.setRowCount(0); // Clear existing data
		tableEditedRows.clear();
		if (saveTableEditsButton != null) {
			saveTableEditsButton.setEnabled(false);
		}

		try {
			List<UserMetadata> users = userManagement.getAllUsersForDisplay();
			for (UserMetadata user : users) {
				if (SessionManager.getInstance().isAdmin()) {
					usersTableModel.addRow(new Object[] { user._userId(), user.displayName(), user.email(),
							user.roles(), user.verificationStatus() });
				} else {
					usersTableModel.addRow(new Object[] { user._userId(), user.displayName(), user.roles(),
							user.verificationStatus() });
				}
			}
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(view, "Error loading users: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
		updateButtonStates();
	}

	@Override
	public void onCreate() {
		view.setBackground(Styles.SECONDARY_COLOR); // Your style
		view.setLayout(new MigLayout("fill, insets 15", "[grow]", "[][grow][]"));
		userManagement = new UserManagement();

		try {
			initializeUI();
			loadUsersData();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(view, "Error initializing UI: " + e.getMessage(), "Initialization Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public boolean onDestroy() {
		view = null;
		return true;
	}

	@Override
	public void onHide() {
		System.out.println(getSceneName() + ": onHide");
	}

	@Override
	public void onShow() {
		updateButtonStates();
	}

	private void onTableRowSelected(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			updateButtonStates();
		}
	}

	private void saveTableEdits(ActionEvent e) {
		if (!SessionManager.getInstance().isAdmin() || tableEditedRows.isEmpty()) {
			return;
		}
		if (userManagement.saveRoleChanges(usersTableModel, tableEditedRows)) {
			JOptionPane.showMessageDialog(view, "Role changes from table saved successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			// Data is already updated in the model, tableEditedRows is cleared in
			// saveRoleChanges
			saveTableEditsButton.setEnabled(false);
			loadUsersData(); // To ensure consistency and re-fetch actual roles string
		} else {
			JOptionPane.showMessageDialog(view, "Failed to save role changes from table.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void setupTableAppearance() {
		usersTable.setRowHeight(25);
		usersTable.getTableHeader().setFont(Styles.TABLE_HEADER_FONT); // Your font
		usersTable.setFont(Styles.TABLE_CELL_FONT); // Your font
		usersTable.setFillsViewportHeight(true); // Fills empty space if table is smaller
		usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		usersTable.getSelectionModel().addListSelectionListener(this::onTableRowSelected);

		// Optional: Cell renderers for alignment or specific formatting
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		usersTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Center ID
		if (SessionManager.getInstance().isAdmin()) {
			usersTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Center Status
		} else {
			usersTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Center Status
		}

		// Set column widths (example)
		TableColumnModel tcm = usersTable.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(50); // ID
		tcm.getColumn(0).setMaxWidth(80);
		tcm.getColumn(1).setPreferredWidth(200); // Display Name
		if (SessionManager.getInstance().isAdmin()) {
			tcm.getColumn(2).setPreferredWidth(250); // Email
			tcm.getColumn(3).setPreferredWidth(150); // Role
			tcm.getColumn(4).setPreferredWidth(100); // Status
		} else {
			tcm.getColumn(2).setPreferredWidth(150); // Role
			tcm.getColumn(3).setPreferredWidth(100); // Status
		}
	}

	private void updateButtonStates() {
		boolean rowSelected = usersTable.getSelectedRow() != -1;
		addUserButton.setEnabled(SessionManager.getInstance().isAdmin());
		editUserButton.setEnabled(SessionManager.getInstance().isAdmin() && rowSelected);
		deleteUserButton.setEnabled(SessionManager.getInstance().isAdmin() && rowSelected);
		if (saveTableEditsButton != null) { // Might not exist if not admin
			saveTableEditsButton.setEnabled(SessionManager.getInstance().isAdmin() && !tableEditedRows.isEmpty());
		}
	}
}