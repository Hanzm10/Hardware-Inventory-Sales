package com.github.hanzm_10.murico.swingapp.lib.table_renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.OverlayLayout;
import javax.swing.table.TableCellRenderer;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

public class ProgressLevelRenderer extends JPanel implements TableCellRenderer {

	public static record StockInfo(@NotNull int getItemId, @NotNull int currentProgressLevel,
			@NotNull int minimumProgressLevel, @NotNull String unitOfMeasure) {

		public static final int MAX_BOUND_PROGRESS_LEVEL = 100;

		public static boolean isValid(Object progressLevel) {
			return (progressLevel != null && progressLevel instanceof StockInfo)
					&& ((StockInfo) progressLevel).currentProgressLevel >= 0
					&& ((StockInfo) progressLevel).minimumProgressLevel >= 0;
		}
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(ProgressLevelRenderer.class);

	private final JLabel label;
	private final JProgressBar progressBar;

	public ProgressLevelRenderer() {
		super();
		setOpaque(false);
		setLayout(new OverlayLayout(this));

		setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

		label = LabelFactory.createBoldLabel("", 9);
		progressBar = new JProgressBar(0, StockInfo.MAX_BOUND_PROGRESS_LEVEL);

		label.setAlignmentX(0.5f);
		label.setAlignmentY(0.5f);

		add(label);

		var panel = new JPanel(new BorderLayout());

		panel.setOpaque(false);

		panel.add(progressBar, BorderLayout.CENTER);

		add(panel);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setOpaque(true);
			setBackground(table.getSelectionBackground());
			label.setForeground(table.getSelectionForeground());
			// For JProgressBar, selection color is usually not applied to the bar itself,
			// but the panel background will cover it.
		} else {
			setOpaque(false);
			setBackground(table.getBackground());
			label.setForeground(table.getForeground());
		}

		if (!StockInfo.isValid(value)) {
			LOGGER.warning("Invalid progress level value: " + value);
			return this;
		}

		StockInfo progressLevel = (StockInfo) value;

		var percentage = ((float) progressLevel.currentProgressLevel - (float) progressLevel.minimumProgressLevel)
				/ (Math.max(StockInfo.MAX_BOUND_PROGRESS_LEVEL, progressLevel.minimumProgressLevel)
						- progressLevel.minimumProgressLevel);
		var txt = progressLevel.currentProgressLevel + " " + progressLevel.unitOfMeasure;
		Color barColor;

		if (percentage <= 0.1f) {
			txt += " Low";
			barColor = Styles.DANGER_COLOR;
		} else if (percentage <= 0.2f) {
			txt += " Mild";
			barColor = Styles.WARNING_COLOR;
		} else if (percentage >= 0.9f) {
			txt += " High";
			barColor = Styles.SUCCESS_COLOR;
		} else { // Between (minQty + 10) and 89
			txt += " Mid";
			barColor = Styles.INFO_COLOR;
		}

		label.setText(txt);
		progressBar.setValue((int) (percentage * 100.0f));
		progressBar.setForeground(barColor);

		return this;
	}

}
