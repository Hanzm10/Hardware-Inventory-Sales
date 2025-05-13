package com.github.hanzm_10.murico.swingapp.ui.components;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.GradientRoundedPanel;

import net.miginfocom.swing.MigLayout;

public class Sidebar {
	private static Logger LOGGER = MuricoLogger.getLogger(Sidebar.class);
	private GradientRoundedPanel container;
	private ButtonSceneNavigatorListener navListener;

	private JButton profileBtn;
	private JButton dashboardBtn;
	private JButton reportsBtn;
	private JButton inventoryBtn;
	private JButton orderMenuBtn;
	private JButton contactsBtn;
	private JButton settingsBtn;
	private ButtonGroup btnGroup;

	private final Map<String, AbstractButton> sceneButtonMap;

	public Sidebar() {
		container = new GradientRoundedPanel(Styles.SECONDARY_COLOR, Styles.PRIMARY_COLOR, 24);
		navListener = new ButtonSceneNavigatorListener(new AtomicBoolean(false));

		container.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
		container.setLayout(new MigLayout("insets 16", "[50px::50px,grow]",
				"[50px::50px,grow][50px::50px,grow][50px::50px,grow][50px::50px,grow]72px:push[50px::50px,grow]"));

		initBtns();
		initBtnGroup();
		initIcons();
		attachBtns();
		attachListeners();

		SceneNavigator.getInstance().subscribe(this::handleNavigation);
		sceneButtonMap = Map.of("profile", profileBtn, "dashboard", dashboardBtn, "reports", reportsBtn, "inventory",
				inventoryBtn, "order menu", orderMenuBtn, "contacts", contactsBtn, "settings", settingsBtn);
	}

	private void attachBtns() {
		container.add(profileBtn, "cell 0 0,grow");
		// container.add(dashboardBtn, "cell 0 1,grow");
		container.add(reportsBtn, "cell 0 1,grow");
		container.add(inventoryBtn, "cell 0 2,grow");
		container.add(orderMenuBtn, "cell 0 3,grow");
		// container.add(contactsBtn, "cell 0 5,grow");
		container.add(settingsBtn, "cell 0 4, grow");
	}

	private void attachListeners() {
		profileBtn.addActionListener(navListener);
		dashboardBtn.addActionListener(navListener);
		reportsBtn.addActionListener(navListener);
		inventoryBtn.addActionListener(navListener);
		orderMenuBtn.addActionListener(navListener);
		contactsBtn.addActionListener(navListener);
		settingsBtn.addActionListener(navListener);

		profileBtn.setActionCommand("home/profile");
		dashboardBtn.setActionCommand("home/dashboard");
		reportsBtn.setActionCommand("home/reports");
		inventoryBtn.setActionCommand("home/inventory");
		orderMenuBtn.setActionCommand("home/order menu");
		contactsBtn.setActionCommand("home/contacts");
		settingsBtn.setActionCommand("home/settings");
	}

	private void checkIfContains(String currFullSceneName) {
		var entry = sceneButtonMap.entrySet();

		for (var n : currFullSceneName.split(ParsedSceneName.SEPARATOR)) {
			for (var e : entry) {
				if (n.equals(e.getKey())) {
					var btn = e.getValue();

					btnGroup.setSelected(btn.getModel(), true);
					break;
				}
			}
		}
	}

	public void destroy() {
		SceneNavigator.getInstance().unsubscribe(this::handleNavigation);
		profileBtn.removeActionListener(navListener);
		dashboardBtn.removeActionListener(navListener);
		reportsBtn.removeActionListener(navListener);
		inventoryBtn.removeActionListener(navListener);
		orderMenuBtn.removeActionListener(navListener);
		contactsBtn.removeActionListener(navListener);
		settingsBtn.removeActionListener(navListener);
	}

	public JPanel getContainer() {
		return container;
	}

	private void handleNavigation(String currFullSceneName) {
		var names = currFullSceneName.split(ParsedSceneName.SEPARATOR);
		String lastName = names[names.length - 1];
		AbstractButton exactMatch = sceneButtonMap.get(lastName);

		if (exactMatch != null) {
			btnGroup.setSelected(exactMatch.getModel(), true);
		} else {
			checkIfContains(currFullSceneName);
		}
	}

	private void initBtnGroup() {
		btnGroup = new ButtonGroup();
		btnGroup.add(profileBtn);
		btnGroup.add(dashboardBtn);
		btnGroup.add(reportsBtn);
		btnGroup.add(inventoryBtn);
		btnGroup.add(orderMenuBtn);
		btnGroup.add(contactsBtn);
		btnGroup.add(settingsBtn);
	}

	private void initBtns() {
		profileBtn = StyledButtonFactory.createButtonButToggleStyle();
		dashboardBtn = StyledButtonFactory.createButtonButToggleStyle();
		reportsBtn = StyledButtonFactory.createButtonButToggleStyle();
		inventoryBtn = StyledButtonFactory.createButtonButToggleStyle();
		orderMenuBtn = StyledButtonFactory.createButtonButToggleStyle();
		contactsBtn = StyledButtonFactory.createButtonButToggleStyle();
		settingsBtn = StyledButtonFactory.createButtonButToggleStyle();

		profileBtn.setToolTipText("Go to your profile");
		dashboardBtn.setToolTipText("Go to dashboard");
		reportsBtn.setToolTipText("Go to reports");
		inventoryBtn.setToolTipText("Go to inventory");
		orderMenuBtn.setToolTipText("Go to order menu");
		contactsBtn.setToolTipText("Go to contacts");
		settingsBtn.setToolTipText("Go to settings");

	}

	private void initIcons() {
		try {
			orderMenuBtn.setIcon(AssetManager.getOrLoadIcon("icons/truck.svg"));
			dashboardBtn.setIcon(AssetManager.getOrLoadIcon("icons/home.svg"));
			reportsBtn.setIcon(AssetManager.getOrLoadIcon("icons/presentation-chart-line.svg"));
			profileBtn.setIcon(AssetManager.getOrLoadIcon("icons/user-circle.svg"));
			contactsBtn.setIcon(AssetManager.getOrLoadIcon("icons/notepad-text.svg"));
			inventoryBtn.setIcon(AssetManager.getOrLoadIcon("icons/package.svg"));
			settingsBtn.setIcon(AssetManager.getOrLoadIcon("icons/cog.svg"));
		} catch (URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "Failed to fetch icons for sidebar", e);
		}
	}
}
