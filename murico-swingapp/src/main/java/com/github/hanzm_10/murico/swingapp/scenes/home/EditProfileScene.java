package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextPlaceholder;

import net.miginfocom.swing.MigLayout;

public class EditProfileScene implements Scene {

	public record GenderCombo(String value, String display) {
		@Override
		public final String toString() {
			return display;
		}
	}

	private JTextField firstnameTF;
	private JTextField lastnameTF;
	private JPanel view;
	private boolean uiInitialized = false;
	private JButton btnSave;
	private JButton cancelBtn;
	private Integer userID;
	private String firstName;

	private String lastName;

	@Override
	public String getSceneName() {
		return "edit";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new RoundedPanel(20)) : view;
	}

	private void initializeEditProfileUI() {
		Profile pfp = new Profile();
		view.setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));
		view.setBackground(Styles.SECONDARY_COLOR);

		JPanel profilePnl = new JPanel(
				new MigLayout("wrap, insets 20 50 20 50", "[grow,center]", "[]20[]20[]20[]20[]30[][]"));
		profilePnl.setBackground(new Color(33, 64, 107));
		view.add(profilePnl, "cell 0 0 , growx,growy, aligny center, alignx center");

		JLabel pfpName = new JLabel();
		pfpName.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pfpName.setForeground(Color.WHITE);
		pfpName.setFont(new Font("Montserrat", Font.BOLD, 64));
		profilePnl.add(pfpName, "cell 0 0,alignx center");

		DefaultComboBoxModel<GenderCombo> model = new DefaultComboBoxModel<>();
		model.addElement(new GenderCombo(null, "Select a gender"));
		model.addElement(new GenderCombo("male", "Male"));
		model.addElement(new GenderCombo("female", "Female"));
		model.addElement(new GenderCombo("non-binary", "Non-Binary"));

		firstnameTF = new JTextField();
		profilePnl.add(firstnameTF, "cell 0 1,growx,width 257!,alignx center");
		firstnameTF.setColumns(10);

		lastnameTF = new JTextField();
		profilePnl.add(lastnameTF, "cell 0 2,growx,width 257!,alignx center");
		lastnameTF.setColumns(10);

		JComboBox<GenderCombo> combo = new JComboBox<>(model);
		profilePnl.add(combo, "cell 0 3,growx,width 257!,alignx center");

		JPanel buttonPanel = new JPanel(new MigLayout("insets 0, fillx", "[grow][grow]"));
		buttonPanel.setOpaque(false);

		btnSave = new JButton("Save");
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buttonPanel.add(btnSave, "align right");

		cancelBtn = new JButton("Cancel");
		cancelBtn.setBackground(Color.WHITE);
		cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buttonPanel.add(cancelBtn, "align left");

		profilePnl.add(buttonPanel, "cell 0 5, alignx center");

		btnSave.addActionListener(_ -> {
			String displayName = SessionManager.getInstance().getLoggedInUser().displayName();
			String newGender = ((GenderCombo) combo.getSelectedItem()).value();

			userID = pfp.getUserIdByDisplayName(displayName);

			if (userID != null) {
				pfp.profile(userID, firstnameTF.getText(), lastnameTF.getText(), newGender);
				var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

				try {
					SessionManager.getInstance()
							.updateUserMetadata(factory.getUserDao().getUserMetadataByDisplayName(displayName));
				} catch (IllegalArgumentException | IllegalStateException | IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(view, "User not found: " + displayName);
			}

			JOptionPane.showMessageDialog(view, "Changes saved successfully: " + displayName);
			SceneNavigator.getInstance().navigateTo("home/profile/readonly");

		});

		cancelBtn.addActionListener(e -> SceneNavigator.getInstance().navigateTo("home/profile/readonly"));
	}

	@Override
	public void onCreate() {
		System.out.println(getSceneName() + ": onCreate");
		initializeEditProfileUI();
		uiInitialized = true;
	}

	@Override
	public boolean onDestroy() {
		uiInitialized = false;
		return true;
	}

	@Override
	public void onHide() {
		System.out.println(getSceneName() + ": onHide");
	}

	@Override
	public void onShow() {
		Profile profile = new Profile();
		System.out.println(getSceneName() + ": onShow");
		var loggedInUser = SessionManager.getInstance().getLoggedInUser();
		var displayName = loggedInUser.displayName();
		firstName = profile.getFirstname(displayName);
		lastName = profile.getLastname(displayName);

		if (firstName != null) {
			new TextPlaceholder(lastName, lastnameTF);

			new TextPlaceholder(firstName, firstnameTF);
		} else {
			new TextPlaceholder("Last Name", lastnameTF);
			new TextPlaceholder("First Name", firstnameTF);
		}

		if (!uiInitialized) {
			onCreate();
		}
	}
}
