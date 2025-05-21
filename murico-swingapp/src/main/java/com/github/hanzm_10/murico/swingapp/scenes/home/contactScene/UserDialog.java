package com.github.hanzm_10.murico.swingapp.scenes.home.contactScene;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Arrays;

import com.github.hanzm_10.murico.swingapp.constants.Styles; // Assuming you have this
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserGender;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserMetadata;


public class UserDialog extends JDialog {
    private JTextField displayNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JLabel passwordLabel, confirmPasswordLabel;

    private UserMetadata currentUser; // For editing
    private boolean saved = false;

    private final boolean isAdmin;

    public UserDialog(Frame owner, String title, boolean modal, UserMetadata userToEdit, boolean isAdmin) {
        super(owner, title, modal);
        this.currentUser = userToEdit;
        this.isAdmin = isAdmin;
        initComponents();
        populateFields();
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap 2, fillx", "[right]rel[grow,fill]", "[]10[]10[]10[]10[]20[]"));
        getContentPane().setBackground(Styles.SECONDARY_COLOR); // Use your styles

        add(new JLabel("Display Name:"));
        displayNameField = new JTextField(20);
        add(displayNameField, "growx");

        add(new JLabel("Email:"));
        emailField = new JTextField(20);
        add(emailField, "growx");

        passwordLabel = new JLabel("Password:");
        add(passwordLabel);
        passwordField = new JPasswordField(20);
        add(passwordField, "growx");

        confirmPasswordLabel = new JLabel("Confirm Password:");
        add(confirmPasswordLabel);
        confirmPasswordField = new JPasswordField(20);
        add(confirmPasswordField, "growx");

        if (isAdmin) {
            add(new JLabel("Role:"));
            roleComboBox = new JComboBox<>(UserManagement.getAllRoleNames());
            add(roleComboBox, "growx");
        } else if (currentUser != null) { // Non-admin editing their own profile (limited)
             add(new JLabel("Role:"));
             JTextField roleDisplayField = new JTextField(currentUser.roles()); // Assuming roles() is a string
             roleDisplayField.setEditable(false);
             add(roleDisplayField, "growx");
        }


        // Hide password fields if editing (unless it's a specific "change password" dialog)
        if (currentUser != null) {
            passwordLabel.setVisible(false);
            passwordField.setVisible(false);
            confirmPasswordLabel.setVisible(false);
            confirmPasswordField.setVisible(false);
             // Adjust layout constraints if fields are hidden to prevent large gaps
            // This might require more complex MigLayout or removing/adding components dynamically
        }


        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::performSave);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Styles.SECONDARY_COLOR);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, "span 2, align center");
    }

    private void populateFields() {
        if (currentUser != null) {
            displayNameField.setText(currentUser.displayName());
            emailField.setText(currentUser.email());
            if (isAdmin && roleComboBox != null) {
                // Assuming currentUser.roles() returns a primary role string.
                // If it returns multiple, you need to decide which one to select or change UI.
                String currentRole = currentUser.roles().split(",")[0].trim(); // Get first role
                roleComboBox.setSelectedItem(UserManagement.getRoleNameFromId(UserManagement.getRoleIdFromName(currentRole)));
            }
        }
    }

    private void performSave(ActionEvent e) {
        // Basic Validation
        String displayName = displayNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String selectedRole = isAdmin && roleComboBox != null ? (String) roleComboBox.getSelectedItem() : (currentUser != null ? currentUser.roles() : null);


        if (displayName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Display Name and Email cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // Simple email regex
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserManagement manager = new UserManagement();
        boolean success;

        if (currentUser == null) { // Adding new user
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty for new user.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selectedRole == null && isAdmin) { // Admin must select a role for new user
                 JOptionPane.showMessageDialog(this, "A role must be selected for the new user.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            success = manager.addUser(displayName, email, password, selectedRole);
        } else { // Editing existing user
            // Password change is not handled here for simplicity.
            // If roleComboBox is null, it means non-admin is editing, so role isn't passed or is original.
            success = manager.updateUser(currentUser._userId(), displayName, email, selectedRole);
        }

        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this, "User " + (currentUser == null ? "added" : "updated") + " successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to " + (currentUser == null ? "add" : "update") + " user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public UserMetadata getUpdatedUserDetails() {
        if (!saved) {
            return null;
        }

        // Values from the dialog fields
        String newDisplayName = displayNameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newRoleStr = isAdmin && roleComboBox != null ? (String) roleComboBox.getSelectedItem() : (currentUser != null ? currentUser.roles() : "Clerk"); // Default if needed

        if (currentUser != null) { // Editing an existing user
            // Reuse existing values for fields not edited in this dialog
            return new UserMetadata(
                    currentUser._userId(),
                    currentUser._createdAt(), // Keep original createdAt
                    new Timestamp(System.currentTimeMillis()), // Set updatedAt to now
                    newDisplayName,
                    currentUser.displayImage(), // Assuming display image is not changed in this dialog
                    currentUser.gender(),       // Keep original gender
                    currentUser.firstName(),    // Keep original firstName
                    currentUser.lastName(),     // Keep original lastName
                    currentUser.biography(),    // Keep original biography
                    newRoleStr,                 // Updated role
                    newEmail,
                    currentUser.verificationStatus(), // Keep original verification status (boolean)
                    currentUser.verifiedAt()          // Keep original verifiedAt
            );
        } else { // Adding a new user
            // Provide defaults for fields not set in this dialog
            // The addUser method in UserManagement will handle actual DB insertion values.
            // This constructed object here is more for the scene's potential immediate use if any.
            Timestamp now = new Timestamp(System.currentTimeMillis());
            return new UserMetadata(
                    0, // User ID will be generated by the database
                    now, // _createdAt
                    now, // updatedAt
                    newDisplayName,
                    "profile_picture/default.png", // Default displayImage
                    UserGender.OTHER, // Default gender (You need a UserGender enum: MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY)
                    "", // Default firstName (or make it a field in dialog)
                    "", // Default lastName (or make it a field in dialog)
                    "", // Default biography
                    newRoleStr,
                    newEmail,
                    false, // Default verificationStatus for a new user (could be true if auto-verified)
                    null   // verifiedAt for a new unverified user (or now if auto-verified)
            );
        }
    }
}