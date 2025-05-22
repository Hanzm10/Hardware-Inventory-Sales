package com.github.hanzm_10.murico.swingapp.scenes.home.contacts.components.dialogs;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.role.Role;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.scenes.home.contacts.components.ContactsTable;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.dialogs.ErrorDialogFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.dialogs.SuccessDialog;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class EditUserDialog extends JDialog {

	public record EditUserDialogResult(int row, String roles) {
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(EditUserDialog.class);

	private Window owner;

	private WindowAdapter windowListener;
	private ComponentAdapter componentListener;

	private AtomicInteger rowToBeEdited = new AtomicInteger(-1);

	private JPanel headerPanel;
	private JLabel title;
	private JLabel subTitle;

	private JPanel formPanel;
	private JScrollPane scrollPane;
	private JLabel roleLabel;
	private JPanel rolePanel;
	private JCheckBox[] roleCheckBoxes;

	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton saveButton;

	private JTable table;
	private ExecutorService executorService;

	private Consumer<EditUserDialogResult> onSave;

	private AtomicReference<Role[]> rolesArr = new AtomicReference<>(new Role[0]);

	public EditUserDialog(@NotNull Window owner, @NotNull JTable table,
			@NotNull Consumer<EditUserDialogResult> onSave) {
		super(owner, "Edit User", ModalityType.APPLICATION_MODAL);

		this.owner = owner;

		this.windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (executorService != null && !executorService.isShutdown()) {
					executorService.shutdownNow();
				}

				setRowToBeEdited(-1);

				dispose();
			}
		};

		this.componentListener = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				if (executorService != null && !executorService.isShutdown()) {
					executorService.shutdownNow();
				}

				executorService = Executors.newSingleThreadExecutor();

				updateDisplay();
			}
		};

		this.onSave = onSave;
		this.table = table;

		setLayout(new MigLayout("insets 16, flowy, gap 2 16", "[grow]", "[grow]"));

		createComponents();
		attachComponents();

		pack();
		setSize(425, 350);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(windowListener);
		addComponentListener(componentListener);
	}

	private void attachComponents() {
		headerPanel.add(title);
		headerPanel.add(subTitle);

		formPanel.add(roleLabel);
		formPanel.add(rolePanel, "growx");

		buttonPanel.add(cancelButton);
		buttonPanel.add(saveButton);

		add(headerPanel, "growx");
		add(scrollPane, "grow");
		add(buttonPanel, "growx");
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));

		buttonPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getForeground()),
						BorderFactory.createEmptyBorder(8, 0, 0, 0)));

		saveButton = StyledButtonFactory.createButton("Save", ButtonStyles.PRIMARY);
		cancelButton = StyledButtonFactory.createButton("Cancel", ButtonStyles.SECONDARY);

		saveButton.setToolTipText("Save changes");
		cancelButton.setToolTipText("Cancel");

		saveButton.addActionListener(this::handleSave);
		cancelButton.addActionListener(this::handleCancel);
	}

	private void createCheckboxes() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			rolesArr.set(factory.getRoleDao().getRoles());
			var roles = rolesArr.get();

			roleCheckBoxes = new JCheckBox[roles.length];

			rolePanel = new JPanel(new MigLayout("insets 0, wrap 2", "[grow]", "[top]"));

			rolePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
			roleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

			for (int i = 0; i < roles.length; i++) {
				roleCheckBoxes[i] = new JCheckBox(roles[i].name());
				roleCheckBoxes[i].setSelected(false);

				if (roles[i].name().equals("admin")) {
					roleCheckBoxes[i].setFocusable(false);
					roleCheckBoxes[i].setEnabled(false);
				}

				rolePanel.add(roleCheckBoxes[i]);
			}
		} catch (SQLException | IOException e) {
			LOGGER.log(Level.SEVERE, "Error creating checkboxes", e);
		}
	}

	private void createComponents() {
		headerPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]"));
		title = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 24);
		subTitle = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml(""), 16);

		formPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]"));
		scrollPane = new JScrollPane(formPanel);
		roleLabel = LabelFactory.createBoldLabel("Role*", 12, Color.GRAY);

		createCheckboxes();
		createButtonPanel();
	}

	public void destroy() {
		removeWindowListener(windowListener);
		removeComponentListener(componentListener);
	}

	private String getDisplayName() {
		var selectedRow = rowToBeEdited.get();

		if (selectedRow < 0) {
			return "";
		}

		var realColumn = table.convertColumnIndexToModel(ContactsTable.COL_CONTACT_DISPLAY_NAME);
		var name = table.getValueAt(selectedRow, realColumn).toString();

		return name;
	}

	public String getRoles() {
		var selectedRow = rowToBeEdited.get();

		if (selectedRow < 0) {
			return "";
		}

		var realColumn = table.convertColumnIndexToModel(ContactsTable.COL_CONTACT_ROLE);
		var roles = table.getValueAt(selectedRow, realColumn).toString();

		return roles;
	}

	private int getUserId() {
		var selectedRow = rowToBeEdited.get();

		if (selectedRow < 0) {
			return -1;
		}

		var realColumn = table.convertColumnIndexToModel(ContactsTable.COL_CONTACT_ID);

		var stringUserId = table.getValueAt(selectedRow, realColumn).toString();
		var userId = Integer.parseInt(stringUserId);

		return userId;
	}

	private void handleCancel(ActionEvent ev) {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private void handleSave(ActionEvent ev) {
		var selectedRoles = new ArrayList<String>();

		for (JCheckBox checkBox : roleCheckBoxes) {
			if (checkBox.isSelected()) {
				selectedRoles.add(checkBox.getText());
			}
		}

		if (isInputSameAsPrevious(selectedRoles.toArray(new String[selectedRoles.size()]))) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

			return;
		}

		var selectedRolesObject = Arrays.stream(rolesArr.get()).filter(role -> selectedRoles.contains(role.name()))
				.toArray(Role[]::new);

		try {
			var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

			factory.getRoleDao().updateUserRoles(getUserId(), selectedRolesObject);

			SwingUtilities.invokeLater(() -> {
				new SuccessDialog(this, "The roles of " + getDisplayName() + " has been updated successfully!")
						.setVisible(true);
				onSave.accept(new EditUserDialogResult(rowToBeEdited.get(), String.join(",", selectedRoles)));
				dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			});
		} catch (SQLException | IOException e) {
			SwingUtilities.invokeLater(() -> {
				ErrorDialogFactory.showErrorDialog(owner, "Error loading database", "Database Error");
			});

			LOGGER.log(Level.SEVERE, "Error updating user roles", e);
		}
	}

	private boolean isInputSameAsPrevious(String[] selectedRoles) {
		var selectedRow = rowToBeEdited.get();

		if (selectedRow < 0) {
			return false;
		}

		var realColumn = table.convertColumnIndexToModel(ContactsTable.COL_CONTACT_ROLE);
		var roles = table.getValueAt(selectedRow, realColumn).toString();

		return roles.equals(String.join(",", selectedRoles));
	}

	public void setRowToBeEdited(@Range(from = 0, to = Integer.MAX_VALUE) int newRow) {
		this.rowToBeEdited.set(newRow);
	}

	private void updateDisplay() {
		title.setText(HtmlUtils.wrapInHtml("Edit User"));
		subTitle.setText(HtmlUtils.wrapInHtml("Editing user " + getDisplayName()));

		var roles = getRoles();

		if (roles != null && !roles.isEmpty()) {
			for (int i = 0; i < roleCheckBoxes.length; i++) {
				roleCheckBoxes[i].setSelected(false);
			}

			String[] roleNames = roles.split(",");

			for (String roleName : roleNames) {
				for (int i = 0; i < roleCheckBoxes.length; i++) {
					if (roleCheckBoxes[i].getText().equals(roleName)) {
						roleCheckBoxes[i].setSelected(true);
					}
				}
			}
		}
	}
}
