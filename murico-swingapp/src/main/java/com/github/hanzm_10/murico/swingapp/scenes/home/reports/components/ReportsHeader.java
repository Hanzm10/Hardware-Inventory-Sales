package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

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
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.listeners.ButtonSceneNavigatorListener;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.RoundedPanel;

import net.miginfocom.swing.MigLayout;

public class ReportsHeader implements SceneComponent {

	private RoundedPanel view;

	private ButtonSceneNavigatorListener navListener = new ButtonSceneNavigatorListener(new AtomicBoolean(false));

	private JButton salesBtn;
	private JButton inventoryBtn;
	private ButtonGroup btnGroup;

	private Map<String, AbstractButton> sceneButtonMap;

	private AtomicBoolean initialized = new AtomicBoolean(false);

	private void attachComponents() {
		view.add(salesBtn, "width 120!, height 36!");
		view.add(inventoryBtn, "width 120!, height 36!");
	}

	private void attachListeners() {
		SceneNavigator.getInstance().subscribe(this::handleNavigation);

		salesBtn.addActionListener(navListener);
		inventoryBtn.addActionListener(navListener);
	}

	private void createComponents() {
		view = new RoundedPanel(24);
		view.setBackground(Styles.SECONDARY_COLOR);

		view.setLayout(new MigLayout("insets 8", "[][]", "[grow]"));

		salesBtn = StyledButtonFactory.createButtonButToggleStyle("Sales");
		inventoryBtn = StyledButtonFactory.createButtonButToggleStyle("Inventory");

		salesBtn.setActionCommand("home/reports/sales reports");
		inventoryBtn.setActionCommand("home/reports/inventory reports");

		salesBtn.setToolTipText("See the sales reports");
		inventoryBtn.setToolTipText("See the inventory reports");

		salesBtn.putClientProperty(FlatClientProperties.STYLE, "foreground:#ffffff");
		inventoryBtn.putClientProperty(FlatClientProperties.STYLE, "foreground:#ffffff");

		btnGroup = new ButtonGroup();

		btnGroup.add(salesBtn);
		btnGroup.add(inventoryBtn);
	}

	@Override
	public void destroy() {
		SceneNavigator.getInstance().unsubscribe(this::handleNavigation);

		salesBtn.removeActionListener(navListener);
		inventoryBtn.removeActionListener(navListener);

		if (view != null) {
			view.removeAll();
			view = null;
		}

		if (btnGroup != null) {
			btnGroup.remove(salesBtn);
			btnGroup.remove(inventoryBtn);
			btnGroup = null;
		}

		if (sceneButtonMap != null) {
			sceneButtonMap = null;
		}

		navListener = null;
		salesBtn = null;
		inventoryBtn = null;

		initialized.set(false);
	}

	@Override
	public JPanel getView() {
		return view == null ? (view = new RoundedPanel(24)) : view;
	}

	private void handleNavigation(String currFullSceneName) {
		var names = currFullSceneName.split(ParsedSceneName.SEPARATOR);

		String lastName = names[names.length - 1];
		AbstractButton exactMatch = sceneButtonMap.get(lastName);

		if (exactMatch != null) {
			btnGroup.setSelected(exactMatch.getModel(), true);
		}
	}

	@Override
	public void initializeComponents() {
		if (initialized.get()) {
			return;
		}

		createComponents();
		attachComponents();
		attachListeners();

		sceneButtonMap = Map.of("sales reports", salesBtn, "inventory reports", inventoryBtn);

		initialized.set(true);
	}

	@Override
	public boolean isInitialized() {
		return initialized.get();
	}
}
