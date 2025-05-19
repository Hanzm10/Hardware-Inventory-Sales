package com.github.hanzm_10.murico.swingapp.ui.components.dialogs;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class SuccessDialog extends JDialog {

	private static final Logger LOGGER = MuricoLogger.getLogger(SuccessDialog.class);

	private ImageIcon checkIcon;
	private JLabel iconLabel;
	private JLabel messageLabel;
	private String message;

	public SuccessDialog() {
		this(null, "Success");
	}

	public SuccessDialog(@NotNull JDialog owner) {
		this(owner, "Success");
	}

	public SuccessDialog(@NotNull JDialog owner, @NotNull String message) {
		super(owner, "Success", true);

		this.message = message;

		setLayout(new MigLayout("insets 16, gap 8", "[grow]"));

		createComponents();
		attachComponents();

		pack();
		setLocationRelativeTo(owner);
	}

	public SuccessDialog(@NotNull String message) {
		this(null, message);
	}

	private void attachComponents() {
		add(iconLabel, "grow");
		add(messageLabel, "grow");
	}

	private void createComponents() {
		try {
			checkIcon = AssetManager.getOrLoadIcon("icons/circle-check-big.svg");
			iconLabel = new JLabel(checkIcon);
		} catch (URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "Failed to load icon", e);
			iconLabel = LabelFactory.createLabel("✔", 16, Styles.SUCCESS_COLOR);
		}

		messageLabel = LabelFactory.createBoldLabel(message, 16, Styles.SUCCESS_COLOR);
	}
}
