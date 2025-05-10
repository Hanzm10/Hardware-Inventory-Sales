package com.github.hanzm_10.murico.swingapp.ui.components;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.GradientRoundedPanel;

import net.miginfocom.swing.MigLayout;

public class Sidebar {
	private static Logger LOGGER = MuricoLogger.getLogger(Sidebar.class);
	private GradientRoundedPanel container;

	private ButtonSceneNavigatorListener navListener;

	private JToggleButton profileBtn;
	private JToggleButton dashboardBtn;
	private JToggleButton salesBtn;
	private JToggleButton inventoryBtn;
	private JToggleButton ordersBtn;
	private JToggleButton usersBtn;
	private JToggleButton settingsBtn;
	private ButtonGroup btnGroup;

	public Sidebar() {
		container = new GradientRoundedPanel(Styles.SECONDARY_COLOR, Styles.PRIMARY_COLOR, 22);
		navListener = new ButtonSceneNavigatorListener(null);

		container.setLayout(new MigLayout("insets 10", "[48px::50px,grow]",
				"[48px::50px,grow][48px::50px,grow][48px::50px,grow][48px::50px,grow][48px::50px,grow][48px::50px,grow]push[48px::50px,grow]"));

		initBtns();
		initBtnGroup();
		initIcons();
		attachBtns();
		attachListeners();
	}

	private void attachBtns() {
		container.add(profileBtn, "cell 0 0,grow");
		container.add(dashboardBtn, "cell 0 1,grow");
		container.add(salesBtn, "cell 0 2,grow");
		container.add(inventoryBtn, "cell 0 3,grow");
		container.add(ordersBtn, "cell 0 4,grow");
		container.add(usersBtn, "cell 0 5,grow");
		container.add(settingsBtn, "cell 0 6,grow");
	}

	private void attachListeners() {
		profileBtn.addActionListener(navListener);
		dashboardBtn.addActionListener(navListener);
		salesBtn.addActionListener(navListener);
		inventoryBtn.addActionListener(navListener);
		ordersBtn.addActionListener(navListener);
		usersBtn.addActionListener(navListener);
		settingsBtn.addActionListener(navListener);

		profileBtn.setActionCommand("home/profile");
		profileBtn.setActionCommand("home/dashboard");
		profileBtn.setActionCommand("home/sales");
		profileBtn.setActionCommand("home/inventory");
		profileBtn.setActionCommand("home/orders");
		profileBtn.setActionCommand("home/users");
		profileBtn.setActionCommand("home/settings");
	}

	public void destroy() {

	}

	public GradientRoundedPanel getContainer() {
		return container;
	}

	private void initBtnGroup() {
		btnGroup = new ButtonGroup();
		btnGroup.add(profileBtn);
		btnGroup.add(dashboardBtn);
		btnGroup.add(salesBtn);
		btnGroup.add(inventoryBtn);
		btnGroup.add(ordersBtn);
		btnGroup.add(usersBtn);
		btnGroup.add(settingsBtn);
	}

	private void initBtns() {
		profileBtn = StyledButtonFactory.createJToggleButton();
		dashboardBtn = StyledButtonFactory.createJToggleButton();
		salesBtn = StyledButtonFactory.createJToggleButton();
		inventoryBtn = StyledButtonFactory.createJToggleButton();
		ordersBtn = StyledButtonFactory.createJToggleButton();
		usersBtn = StyledButtonFactory.createJToggleButton();
		settingsBtn = StyledButtonFactory.createJToggleButton();

		profileBtn.setToolTipText("Go to your profile");
		dashboardBtn.setToolTipText("Go to dashboard");
		salesBtn.setToolTipText("Go to sales");
		inventoryBtn.setToolTipText("Go to inventory");
		ordersBtn.setToolTipText("Go to orders");
		usersBtn.setToolTipText("See the users of this application");
		settingsBtn.setToolTipText("Go to settings");

	}

	private void initIcons() {
		try {
			ordersBtn.setIcon(AssetManager.getOrLoadIcon("icons/truck.svg"));
			dashboardBtn.setIcon(AssetManager.getOrLoadIcon("icons/home.svg"));
			salesBtn.setIcon(AssetManager.getOrLoadIcon("icons/presentation-chart-line.svg"));
			profileBtn.setIcon(AssetManager.getOrLoadIcon("icons/user-circle.svg"));
			usersBtn.setIcon(AssetManager.getOrLoadIcon("icons/notepad-text.svg"));
			inventoryBtn.setIcon(AssetManager.getOrLoadIcon("icons/package.svg"));
			settingsBtn.setIcon(AssetManager.getOrLoadIcon("icons/cog.svg"));
		} catch (URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch icons for sidebar", e);
		}
	}
}
