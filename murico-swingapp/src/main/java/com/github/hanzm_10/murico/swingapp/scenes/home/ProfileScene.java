package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.util.logging.Logger;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.SceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.manager.impl.StaticSceneManager;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SubSceneSupport;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.EditProfileScene;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.ReadProfileScene;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;

import net.miginfocom.swing.MigLayout;

public class ProfileScene implements Scene, SubSceneSupport {
	private static final Logger LOGGER = MuricoLogger.getLogger(ProfileScene.class);
	private JPanel view;

	private SceneManager sceneManager;

	private void createSceneManager() {
		sceneManager = new StaticSceneManager();

		sceneManager.registerScene("read_profile", () -> new ReadProfileScene(), HomeScene.GUARD);
		sceneManager.registerScene("edit_profile", () -> new EditProfileScene(), () -> {
			if (!HomeScene.GUARD.canAccess()) {
				return false;
			}

			return SessionManager.getInstance().getLoggedInUser().roles().contains("admin");
		});
	}

	@Override
	public SceneManager getSceneManager() {
		if (sceneManager == null) {
			createSceneManager();
		}

		return sceneManager;
	}

	@Override
	public String getSceneName() {
		return "profile";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;

	}

	/*
	 * private void initializeProfileUI() throws IOException, InterruptedException {
	 * Profile profile = new Profile(); view.setBackground(Color.WHITE);
	 *
	 * view.setLayout(new MigLayout("fill", "[][413.00][][]",
	 * "[][66.00][88.00][]"));
	 *
	 * displayRoleLbl = new JLabel(); displayRoleLbl.setForeground(Color.WHITE);
	 * String username =
	 * SessionManager.getInstance().getLoggedInUser().displayName();
	 * displayRoleLbl.setText(profile.getRoleByUsername(username));
	 * displayRoleLbl.setFont(new Font("Montserrat", Font.BOLD, 20));
	 * displayRoleLbl.setOpaque(false); view.add(displayRoleLbl);
	 * view.add(displayRoleLbl, "cell 2 3,alignx center,aligny top");
	 *
	 * roleLbl = new JLabel(""); image =
	 * AssetManager.getOrLoadImage("images/roleBoarderLabel.png");
	 * roleLbl.setIcon(new ImageIcon(image));
	 * roleLbl.setAlignmentX(Component.CENTER_ALIGNMENT); roleLbl.setBounds(497,
	 * 425, 274, 56); view.add(roleLbl, "cell 2 3,alignx center,aligny top");
	 *
	 * profilepicLbl = new JLabel(""); profilepicLbl.setBackground(new Color(33, 64,
	 * 107)); profilepicLbl.setIcon(new
	 * ImageIcon(AssetManager.getOrLoadImage("images/profilepic.png")));
	 *
	 * profilepicLbl.setBounds(497, 64, 233, 229); view.add(profilepicLbl,
	 * "cell 2 0, alignx center, aligny center");
	 *
	 * displaynameLbl = new JLabel();
	 * displaynameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
	 * displaynameLbl.setForeground(Color.WHITE);
	 * displaynameLbl.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	 * displaynameLbl.setText(username.toUpperCase()); displaynameLbl.setFont(new
	 * Font("Montserrat", Font.BOLD, 64)); displaynameLbl.setOpaque(false);
	 * view.add(displaynameLbl, "cell 2 2,alignx center,aligny top");
	 *
	 * profileLogoLbl = new JLabel(""); profileLogoLbl.setIcon(new
	 * ImageIcon(AssetManager.getOrLoadImage("images/profileRectangle.png")));
	 * view.add(profileLogoLbl, "cell 1 1 4 4,alignx center,aligny top"); //
	 * profilePnl.setLayout(null);
	 *
	 * editProfBtn = new JButton("");
	 * editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	 * editProfBtn.setBorderPainted(false); editProfBtn.setIcon(new
	 * ImageIcon(AssetManager.getOrLoadImage("images/editProf.png")));
	 * view.add(editProfBtn, "cell 3 0,alignx right,growy");
	 *
	 * editProfBtn.addActionListener(new ActionListener() {
	 *
	 * @Override public void actionPerformed(ActionEvent e) { boolean isAdmin =
	 * profile.isAdmin(username); if (isAdmin) { EditProfile scene = new
	 * EditProfile(); scene.onCreate(); scene.onShow();
	 *
	 * } else { System.out.print("error"); } } }); }
	 */

	@Override
	public void navigateTo(@NotNull String subSceneName) {
		sceneManager.navigateTo(subSceneName);
	}

	@Override
	public void navigateToDefault() {
		SceneNavigator.getInstance().navigateTo(
				"home" + ParsedSceneName.SEPARATOR + getSceneName() + ParsedSceneName.SEPARATOR + "read_profile");
	}

	@Override
	public void onCreate() {
		view.setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
		view.add(sceneManager.getRootContainer(), "grow");
	}

	@Override
	public boolean onDestroy() {
		return true;
	}

}
