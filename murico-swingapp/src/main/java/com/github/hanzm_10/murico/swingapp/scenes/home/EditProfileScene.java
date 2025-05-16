package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextPlaceholder;

import net.miginfocom.swing.MigLayout;

public class EditProfileScene implements Scene {
	
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPanel view;
	private String username;
	private boolean uiInitialized = false;
	private JLabel profilepic_1;
	private JLabel profileLbl_1;
	private JButton btnSave;
	private JButton cancelBtn;
	
	
	@Override
	public String getSceneName() {
		return "edit";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
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



private void initializeEditProfileUI() throws IOException, InterruptedException {
	Profile pfp = new Profile();
	//view.setBackground(new Color(3));
	view.setLayout(new MigLayout("insets 0, fillx", "[grow]","[][grow]"));
	
	/*JPanel profilepicPnl = new JPanel(new MigLayout("wrap, insets 20 50 20 50", "[grow,center]", "[]20[]"));
	view.add(profilepicPnl, "growx, align center, wrap");
	
	profilepic_1 = new JLabel("");
	//profilepic_1.setBounds(445, 38, 245, 246);
	profilepic_1.setIcon(new ImageIcon(AssetManager.getOrLoadImage("images/profilepic.png")));
	profilepic_1.setBackground(new Color(33, 64, 107));
	profilepicPnl.add(profilepic_1, "growx, align center ");
	*/
	
	JPanel profilePnl = new JPanel(new MigLayout("wrap, insets 20 50 20 50", "[grow,center]", "[]20[]20[]20[]20[]30[][]"));
    profilePnl.setBackground(new Color(33, 64, 107));
	view.add(profilePnl, "growx,growy, align center");
	
    JLabel pfpName = new JLabel();
	pfpName.setBounds(420, 302, 365, 79);
	pfpName.setAlignmentX(Component.RIGHT_ALIGNMENT);
	pfpName.setForeground(Color.WHITE);
	pfpName.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	//pfpName.setText(username.toUpperCase());
	pfpName.setFont(new Font("Montserrat", Font.BOLD, 64));
	pfpName.setOpaque(false);
	profilePnl.add(pfpName, "cell 0 0,alignx center");
	
	DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    model.addElement("Select gender");          
    for (String r : new String[]{
        "Male", "Female", "To be assigned"
    }) {
        model.addElement(r);
    }
    
    textField = new JTextField();
    profilePnl.add(textField, "cell 0 1,growx,width 257!,alignx center");
	textField.setColumns(10);
	TextPlaceholder firstnameHoldr = new TextPlaceholder("First Name", textField);
	//LoginWindows.defText(textField, "First Name");
	
	textField_1 = new JTextField();
	textField_1.setBounds(432, 476, 257, 52);
	profilePnl.add(textField_1, "cell 0 2,growx,width 257!,alignx center");
	textField_1.setColumns(10);
	TextPlaceholder lastnameHoldr = new TextPlaceholder("Lastname", textField_1);
	
	
    // 2) Create your combo from that model:
    JComboBox<String> combo = new JComboBox<>(model);
    combo.setBounds(432, 524, 257, 52);
    //pfp.editRole(combo);
	profilePnl.add(combo, "cell 0 3,growx,width 257!,alignx center");
	
	/*textField_2 = new JTextField();
	profilePnl.add(textField_2, "cell 0 4,growx,width 257!,alignx center");
	textField_2.setColumns(10);
	TextPlaceholder emailHoldr = new TextPlaceholder("Email Address", textField_2);
	*/
	
	JPanel buttonPanel = new JPanel(new MigLayout("insets 0, fillx", "[grow][grow]"));
    buttonPanel.setOpaque(false);
	
	btnSave = new JButton("Save");
	btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	buttonPanel.add(btnSave, "align right");
	
	cancelBtn = new JButton("Cancel");
	cancelBtn.setBounds(432, 671, 86, 29);
	cancelBtn.setBackground(Color.WHITE);
	cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	buttonPanel.add(cancelBtn, "align left");
	
	profilePnl.add(buttonPanel, "cell 0 5, alignx center");
	
	btnSave.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String displayName = SessionManager.getInstance().getLoggedInUser().displayName();
			String newGender = (String) combo.getSelectedItem();
		    Integer userId = pfp.getUserIdByDisplayName(displayName);
		    if (userId != null) {
		        	pfp.profile(userId, textField.getText(), textField_1.getText(), newGender);
		    } else {
		        JOptionPane.showMessageDialog(view, "User not found: " + displayName);
		    }
		}
	});
	
	cancelBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SceneNavigator.getInstance().navigateTo("home/profile/readonly");
			
		}
	});
	}
}