package com.github.hanzm_10.murico.swingapp.scenes.home; // Renamed package

import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.contacts.components.ContactsTable;
import com.github.hanzm_10.murico.swingapp.scenes.home.contacts.components.Header;
import com.github.hanzm_10.murico.swingapp.scenes.home.contacts.components.dialogs.EditUserDialog;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.dialogs.ErrorDialogFactory;

import net.miginfocom.swing.MigLayout;

public class ContactScene implements Scene {

	private JPanel view;

	private Header header;
	private ContactsTable contactsTable;

	private EditUserDialog editUserDialog;

	private ExecutorService executorService;

	@Override
	public String getSceneName() {
		return "Users Management";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void handleEditCommand() {
		if (!SessionManager.getInstance().isAdmin()) {
			SwingUtilities.invokeLater(() -> {
				ErrorDialogFactory.showErrorDialog(view, "Permission Denied",
						"You do not have permission to edit users.");
			});

			return;
		}

		var selectedRow = contactsTable.getTable().getSelectedRow();

		if (selectedRow == -1) {
			SwingUtilities.invokeLater(() -> {
				ErrorDialogFactory.showErrorDialog(view, "No user selected", "Please select a user to edit.");
			});

			return;
		}

		var realColumnIndex = contactsTable.getTable().convertColumnIndexToModel(ContactsTable.COL_CONTACT_ID);
		var idOfSelectedRow = contactsTable.getTable().getValueAt(selectedRow, realColumnIndex);

		if (SessionManager.getInstance().getLoggedInUser()._userId() == (int) idOfSelectedRow) {
			SwingUtilities.invokeLater(() -> {
				ErrorDialogFactory.showErrorDialog(view, "Permission Denied", "You cannot edit your own roles.");
			});

			return;
		}

		var rolesOfSelectedRow = contactsTable.getTable().getValueAt(selectedRow,
				contactsTable.getTable().convertColumnIndexToModel(ContactsTable.COL_CONTACT_ROLE));

		if (rolesOfSelectedRow != null && ((String) rolesOfSelectedRow).contains("Admin")) {
			SwingUtilities.invokeLater(() -> {
				ErrorDialogFactory.showErrorDialog(view, "Permission Denied",
						"You cannot edit the roles of an admin user.");
			});

			return;
		}

		var owner = SwingUtilities.getWindowAncestor(view);

		if (editUserDialog == null) {
			editUserDialog = new EditUserDialog(owner, contactsTable.getTable(), this::onEditUserDialogClosed);
		}

		editUserDialog.setLocationRelativeTo(owner);
		editUserDialog.setRowToBeEdited(selectedRow);
		editUserDialog.setVisible(true);
	}

	private void handleHeaderButtonClick(ActionEvent e) {
		String command = e.getActionCommand();

		switch (command) {
		case Header.EDIT_COMMAND:
			handleEditCommand();
			break;
		default:
			break;
		}
	}

	private void handleInitialContactTableLoad() {
		contactsTable.performBackgroundTask();

		SwingUtilities.invokeLater(() -> {
			header.setBtnListener(this::handleHeaderButtonClick);
		});
	}

	@Override
	public void onBeforeShow() {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
		}

		executorService = Executors.newFixedThreadPool(1);

		executorService.submit(this::handleInitialContactTableLoad);
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("insets 0, flowy", "[grow]", "[top]"));

		header = new Header();
		contactsTable = new ContactsTable();

		view.add(header.getView(), "growx");
		view.add(contactsTable.getView(), "grow");

		header.initializeComponents();
	}

	@Override
	public boolean onDestroy() {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdownNow();
		}

		header.destroy();
		contactsTable.destroy();

		return true;
	}

	private void onEditUserDialogClosed(EditUserDialog.EditUserDialogResult result) {
		var table = contactsTable.getTable();
		var realRoleColumnIndex = table.convertColumnIndexToView(ContactsTable.COL_CONTACT_ROLE);

		table.getModel().setValueAt(result.roles(), result.row(), realRoleColumnIndex);
	}

}