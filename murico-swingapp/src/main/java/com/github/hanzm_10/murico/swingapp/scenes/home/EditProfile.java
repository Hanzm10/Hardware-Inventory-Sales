package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextPlaceholder;

public class EditProfile implements Scene {

	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPanel view;
	private boolean uiInitialized = false;
	private JLabel profilepic_1;
	private JLabel profileLbl_1;
	private JButton btnSave;
	private JButton cancelBtn;

	@Override
	public String getSceneName() {
		return "editprofile";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void initializeEditProfileUI() throws IOException, InterruptedException {
		view.setBackground(Color.WHITE);
		// view.setLayout(MigLayout());

		JLabel pfpName = new JLabel();
		pfpName.setBounds(420, 302, 365, 79);
		pfpName.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pfpName.setForeground(Color.WHITE);
		pfpName.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		// pfpName.setText(username.toUpperCase());
		pfpName.setFont(new Font("Montserrat", Font.BOLD, 64));
		pfpName.setOpaque(false);
		view.add(pfpName);

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		model.addElement("Select gender");
		for (String r : new String[] { "Male", "Female", "To be assigned" }) {
			model.addElement(r);
		}

		// 2) Create your combo from that model:
		JComboBox<String> combo = new JComboBox<>(model);
		combo.setBounds(432, 524, 257, 52);
		// pfp.editRole(combo);
		view.add(combo);

		textField = new JTextField();
		textField.setBounds(432, 410, 257, 52);
		view.add(textField);
		textField.setColumns(10);
		new TextPlaceholder("First Name", textField);
		// LoginWindows.defText(textField, "First Name");

		textField_1 = new JTextField();
		textField_1.setBounds(432, 476, 257, 52);
		view.add(textField_1);
		textField_1.setColumns(10);
		new TextPlaceholder("Lastname", textField_1);
		textField_2 = new JTextField();
		textField_2.setBounds(432, 568, 257, 52);
		view.add(textField_2);
		textField_2.setColumns(10);
		// LoginWindows.defText(textField_1, "Last Name");
		new TextPlaceholder("Email Address", textField_2);
		// LoginWindows.defText(textField_2, "Email Address");

		btnSave = new JButton("Save");
		btnSave.setBounds(620, 671, 75, 29);
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		view.add(btnSave);

		cancelBtn = new JButton("Cancel");
		cancelBtn.setBounds(432, 671, 86, 29);
		cancelBtn.setBackground(Color.WHITE);
		cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		view.add(cancelBtn);

		profilepic_1 = new JLabel("");
		profilepic_1.setBounds(445, 38, 245, 246);
		profilepic_1.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profilepic.png")));
		profilepic_1.setBackground(new Color(33, 64, 107));
		view.add(profilepic_1);

		profileLbl_1 = new JLabel("");
		profileLbl_1.setBounds(16, 200, 1173, 671);
		profileLbl_1.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profileRectangle.png")));
		view.add(profileLbl_1);

		JLabel ProfileTxt_1 = new JLabel();
		ProfileTxt_1.setBounds(844, 16, 220, 79);
		ProfileTxt_1.setText("Profile");
		ProfileTxt_1.setForeground(new Color(40, 62, 104));
		ProfileTxt_1.setFont(new Font("Montserrat", Font.BOLD | Font.ITALIC, 64));
		view.add(ProfileTxt_1);

	}

	@Override
	public void onCreate() {
		System.out.println(getSceneName() + ": onCreate");
		try {
			initializeEditProfileUI();
			uiInitialized = true;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onDestroy() {
		uiInitialized = false;
		return true;

	}

	@Override
	public void onHide() {
		System.out.println(getSceneName() + ": onHide");
		// Pause activities if needed
	}

	@Override
	public void onShow() {
		System.out.println(getSceneName() + ": onShow");
		if (!uiInitialized) {
			onCreate();
		}
	}
}