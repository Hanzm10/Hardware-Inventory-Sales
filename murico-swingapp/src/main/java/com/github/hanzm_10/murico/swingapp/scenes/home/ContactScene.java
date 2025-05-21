package com.github.hanzm_10.murico.swingapp.scenes.home; // Renamed package

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserMetadata;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.contactScene.UserDialog;
import com.github.hanzm_10.murico.swingapp.scenes.home.contactScene.UserManagement;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel; // Assuming this exists

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactScene implements Scene {
    private JPanel view;
    private JTable usersTable;
    private DefaultTableModel usersTableModel;
    private JButton addUserButton, editUserButton, deleteUserButton, refreshButton, saveTableEditsButton;

    private UserManagement userManagement;
    private static Set<Integer> tableEditedRows = new HashSet<>(); // For direct table role edits
    private boolean isAdmin = false;
    private boolean uiInitialized = false;


    @Override
    public String getSceneName() {
        return "Users Management";
    }

    @Override
    public JPanel getSceneView() {
        if (view == null) {
            view = new RoundedPanel(10); // Or new JPanel()
            view.setBackground(Styles.SECONDARY_COLOR); // Your style
            view.setLayout(new MigLayout("fill, insets 15", "[grow]", "[][grow][]"));
            userManagement = new UserManagement();
            // Determine admin status once
            var loggedInUser = SessionManager.getInstance().getLoggedInUser();
            if (loggedInUser != null && loggedInUser.roles() != null) {
                // Assuming roles() gives a string, check if "admin" is one of them
                isAdmin = Arrays.stream(loggedInUser.roles().toLowerCase().split(","))
                                .anyMatch(role -> role.trim().equals("admin"));
            }
        }
        return view;
    }

    @Override
    public void onCreate() {
         if (!uiInitialized) {
            try {
				initializeUI();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            loadUsersData();
            uiInitialized = true;
        }
    }
    
    @Override
    public void onShow() {
        System.out.println(getSceneName() + ": onShow");
        // Refresh data if needed, or if coming back from a dialog
        if (uiInitialized) { // Only refresh if UI is already built
           // loadUsersData(); // Could be too aggressive, depends on UX
           updateButtonStates(); // Ensure buttons are correct after potential role changes
        }
    }

    @Override
    public boolean onDestroy() {
        System.out.println(getSceneName() + ": onDestroy");
        uiInitialized = false; // Reset for next creation
        view = null; // Allow panel to be garbage collected and recreated
        return true;
    }
    
    @Override
    public void onHide() {
        System.out.println(getSceneName() + ": onHide");
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
        refreshButton.addActionListener(e -> loadUsersData());
        actionPanel.add(refreshButton);


        view.add(actionPanel, "dock north, gapbottom 10");

        // --- Users Table ---
        String[] columnNamesAdmin = {"ID", "Display Name", "Email", "Role(s)", "Verified"};
        String[] columnNamesNonAdmin = {"ID", "Display Name", "Role(s)", "Verified"};

        usersTableModel = new DefaultTableModel(isAdmin ? columnNamesAdmin : columnNamesNonAdmin, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow editing only 'Role(s)' column by admin directly in table
                return isAdmin && column == 3;
            }
        };
        usersTable = new JTable(usersTableModel);
        setupTableAppearance();

        if (isAdmin) {
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

        // --- Save Table Edits Button Panel (only for admins if direct table edit is enabled) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Styles.SECONDARY_COLOR);

        saveTableEditsButton = new JButton("Save Role Changes in Table");
        saveTableEditsButton.addActionListener(this::saveTableEdits);
        saveTableEditsButton.setEnabled(false); // Initially disabled
        
        if(isAdmin) { // Only add this button if admin can edit table directly
            bottomPanel.add(saveTableEditsButton);
        }
        view.add(bottomPanel, "cell 0 2, dock south");

        updateButtonStates(); // Set initial button enabled/disabled state
    }

    private void setupTableAppearance() {
        usersTable.setRowHeight(25);
        usersTable.getTableHeader().setFont(Styles.TABLE_HEADER_FONT); // Your font
        usersTable.setFont(Styles.TABLE_CELL_FONT);         // Your font
        usersTable.setFillsViewportHeight(true); // Fills empty space if table is smaller
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.getSelectionModel().addListSelectionListener(this::onTableRowSelected);

        // Optional: Cell renderers for alignment or specific formatting
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        usersTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Center ID
         if (isAdmin) {
            usersTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Center Status
        } else {
            usersTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Center Status
        }

        // Set column widths (example)
        TableColumnModel tcm = usersTable.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(50); // ID
        tcm.getColumn(0).setMaxWidth(80);
        tcm.getColumn(1).setPreferredWidth(200); // Display Name
        if (isAdmin) {
            tcm.getColumn(2).setPreferredWidth(250); // Email
            tcm.getColumn(3).setPreferredWidth(150); // Role
            tcm.getColumn(4).setPreferredWidth(100); // Status
        } else {
            tcm.getColumn(2).setPreferredWidth(150); // Role
            tcm.getColumn(3).setPreferredWidth(100); // Status
        }
    }


    private void loadUsersData() {
        usersTableModel.setRowCount(0); // Clear existing data
        tableEditedRows.clear();
        if (saveTableEditsButton != null) saveTableEditsButton.setEnabled(false);

        try {
            List<UserMetadata> users = userManagement.getAllUsersForDisplay();
            for (UserMetadata user : users) {
                if (isAdmin) {
                    usersTableModel.addRow(new Object[]{
                            user._userId(),
                            user.displayName(),
                            user.email(),
                            user.roles(), // This might be a comma-separated string if multiple roles
                            user.verificationStatus()
                    });
                } else {
                    usersTableModel.addRow(new Object[]{
                            user._userId(),
                            user.displayName(),
                            user.roles(),
                            user.verificationStatus()
                    });
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error loading users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean rowSelected = usersTable.getSelectedRow() != -1;
        addUserButton.setEnabled(isAdmin);
        editUserButton.setEnabled(isAdmin && rowSelected);
        deleteUserButton.setEnabled(isAdmin && rowSelected);
        if (saveTableEditsButton != null) { // Might not exist if not admin
             saveTableEditsButton.setEnabled(isAdmin && !tableEditedRows.isEmpty());
        }
    }

    private void onTableRowSelected(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            updateButtonStates();
        }
    }

    private void addUser(ActionEvent e) {
        if (!isAdmin) return;
        UserDialog dialog = new UserDialog((Frame) SwingUtilities.getWindowAncestor(view), "Add New User", true, null, isAdmin);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadUsersData(); // Refresh table
        }
    }

    private void editUser(ActionEvent e) {
        if (!isAdmin) return;
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a user to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Retrieve UserMetadata for the selected row.
        // This requires you to have the full UserMetadata object or fetch it by ID.
        // For simplicity, let's re-fetch. A better way is to store UserMetadata objects with the table model or in a list.
        int userId = (int) usersTableModel.getValueAt(usersTable.convertRowIndexToModel(selectedRow), 0);
        UserMetadata userToEdit = null;
        try {
            // You might need a getUserById method in UserManagement
            // For now, we'll find it from the list we would have loaded
            List<UserMetadata> currentUsers = userManagement.getAllUsersForDisplay(); // Inefficient, ideally get by ID
            for(UserMetadata u : currentUsers) {
                if(u._userId() == userId) {
                    userToEdit = u;
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error fetching user details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userToEdit == null) {
             JOptionPane.showMessageDialog(view, "Could not find user details for ID: " + userId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        UserDialog dialog = new UserDialog((Frame) SwingUtilities.getWindowAncestor(view), "Edit User", true, userToEdit, isAdmin);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadUsersData(); // Refresh table
        }
    }

    private void deleteUser(ActionEvent e) {
        if (!isAdmin) return;
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a user to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) usersTableModel.getValueAt(usersTable.convertRowIndexToModel(selectedRow), 0);
        String userName = (String) usersTableModel.getValueAt(usersTable.convertRowIndexToModel(selectedRow), 1);

        int confirmation = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete user: " + userName + " (ID: " + userId + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            if (userManagement.deleteUser(userId)) {
                JOptionPane.showMessageDialog(view, "User " + userName + " deleted successfully.", "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
                loadUsersData(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(view, "Failed to delete user " + userName + ".", "Deletion Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveTableEdits(ActionEvent e) {
        if (!isAdmin || tableEditedRows.isEmpty()) return;
        if (userManagement.saveRoleChanges(usersTableModel, tableEditedRows)) {
            JOptionPane.showMessageDialog(view, "Role changes from table saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Data is already updated in the model, tableEditedRows is cleared in saveRoleChanges
            saveTableEditsButton.setEnabled(false);
             loadUsersData(); // To ensure consistency and re-fetch actual roles string
        } else {
            JOptionPane.showMessageDialog(view, "Failed to save role changes from table.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}