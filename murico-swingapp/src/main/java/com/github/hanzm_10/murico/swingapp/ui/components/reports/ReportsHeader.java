package com.github.hanzm_10.murico.swingapp.ui.components.reports;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.GradientRoundedPanel;

import net.miginfocom.swing.MigLayout;

public final class ReportsHeader {
	private JPanel wrapper;
	private GradientRoundedPanel container;

	private ButtonSceneNavigatorListener navListener = new ButtonSceneNavigatorListener(new AtomicBoolean(false));

	private JButton salesBtn;
	private JButton inventoryBtn;
	private ButtonGroup btnGroup;

	private final Map<String, AbstractButton> sceneButtonMap;

	public ReportsHeader() {
		createComponents();
		attachComponents();

		sceneButtonMap = Map.of("sales reports", salesBtn, "inventory reports", inventoryBtn);
	}

	private void attachComponents() {
		btnGroup.add(salesBtn);
		btnGroup.add(inventoryBtn);

		container.add(salesBtn, "grow");
		container.add(inventoryBtn, "grow");

		wrapper.add(container, "grow");
	}

	public void attachListeners() {
		SceneNavigator.getInstance().subscribe(this::handleNavigation);

		salesBtn.addActionListener(navListener);
		inventoryBtn.addActionListener(navListener);
	}

	private void createComponents() {
		wrapper = new JPanel();
		wrapper.setLayout(new MigLayout("", "[grow]", "[grow]"));

		container = new GradientRoundedPanel(Styles.SECONDARY_COLOR, Styles.SECONDARY_COLOR, 24);
		container.setLayout(new MigLayout("", "[::96px,grow][::124px,grow]", "[grow, center]"));

		btnGroup = new ButtonGroup();

		salesBtn = StyledButtonFactory.createButtonButToggleStyle("Sales");
		inventoryBtn = StyledButtonFactory.createButtonButToggleStyle("Inventory");

		salesBtn.setActionCommand("home/reports/sales reports");
		inventoryBtn.setActionCommand("home/reports/inventory reports");

		salesBtn.putClientProperty(FlatClientProperties.STYLE, "foreground:#ffffff");
		inventoryBtn.putClientProperty(FlatClientProperties.STYLE, "foreground:#ffffff");
	}

	public void destroy() {
		destroyListeners();
	}

	public void destroyListeners() {
		SceneNavigator.getInstance().unsubscribe(this::handleNavigation);

		salesBtn.removeActionListener(navListener);
		inventoryBtn.removeActionListener(navListener);
	}

	public JPanel getContainer() {
		return wrapper;
	}

	private void handleNavigation(String currFullSceneName) {
		var names = currFullSceneName.split(ParsedSceneName.SEPARATOR);

		String lastName = names[names.length - 1];
		AbstractButton exactMatch = sceneButtonMap.get(lastName);

		if (exactMatch != null) {
			btnGroup.setSelected(exactMatch.getModel(), true);
		}
	}
}
