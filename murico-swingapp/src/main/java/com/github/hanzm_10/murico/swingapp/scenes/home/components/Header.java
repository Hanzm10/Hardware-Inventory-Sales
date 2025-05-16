package com.github.hanzm_10.murico.swingapp.scenes.home.components;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.ParsedSceneName;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.ImagePanel;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class Header {
	private static Logger LOGGER = MuricoLogger.getLogger(Header.class);
	private JPanel container;

	private ImagePanel logoIcon;
	private JLabel headerText;

	private String[] sceneNames;

	public Header() {
		container = new JPanel();
		container.setLayout(new MigLayout("insets 0"));

		loadImages();
		initComponents();
		attachComponents();

		SceneNavigator.getInstance().subscribe(this::handleNavigation);

		sceneNames = new String[] { "profile", "dashboard", "order menu", "reports", "inventory", "contacts",
				"settings" };
	}

	private void attachComponents() {
		if (logoIcon != null) {
			container.add(logoIcon, "width 72!, height 72!");
		}

		container.add(Box.createVerticalGlue(), "pushx, grow");
		container.add(headerText);

	}

	public void destroy() {
		SceneNavigator.getInstance().unsubscribe(this::handleNavigation);
	}

	public JPanel getContainer() {
		return container;
	}

	private void handleNavigation(String currFullSceneName) {
		var names = currFullSceneName.split(ParsedSceneName.SEPARATOR);
		var end = names[names.length - 1];

		for (var n : sceneNames) {
			if (n.equals(end)) {
				headerText.setText(end.toUpperCase(Locale.ENGLISH));
				return;
			}
		}

		for (int i = 0; i < names.length; ++i) {
			for (int j = 0; j < sceneNames.length; ++j) {
				if (names[i].equals(sceneNames[j])) {
					headerText.setText(sceneNames[j].toUpperCase(Locale.ENGLISH));
					return;
				}
			}
		}
	}

	private void initComponents() {

		headerText = LabelFactory.createBoldItalicLabel(36, Styles.PRIMARY_COLOR);
	}

	private void loadImages() {
		try {
			logoIcon = new ImagePanel(AssetManager.getOrLoadImage("images/logo.png"));
		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Failed to load images for header", e);
		}
	}
}
