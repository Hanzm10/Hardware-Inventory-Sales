package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.dialogs;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.InventoryReportTable.DateRange;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class DateRangePickerDialog extends JDialog {
	private final @NotNull Window owner;

	private WindowAdapter windowListener;
	private ComponentAdapter componentListener;

	private JPanel headerPanel;
	private JLabel title;
	private JLabel subTitle;

	private JPanel formPanel;
	private JScrollPane scrollPane;

	private JLabel dateRangeLabel;
	private JComboBox<DateRange> dateRangeComboBox;
	private JPanel buttonPanel;
	private JButton applyButton;

	private Consumer<DateRange> onDateRangeSelected;

	public DateRangePickerDialog(@NotNull final Window owner, @NotNull final Consumer<DateRange> onDateRangeSelected) {
		super(owner, "Pick a date range", Dialog.ModalityType.APPLICATION_MODAL);

		this.owner = owner;
		this.onDateRangeSelected = onDateRangeSelected;

		this.windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		};
		this.componentListener = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				super.componentShown(e);
			}
		};

		setLayout(new MigLayout("insets 16, flowy, gap 2 16", "[grow]", "[grow,bottom]"));

		createComponents();
		attachComponents();

		pack();
		setSize(430, 325);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(windowListener);
		addComponentListener(componentListener);
	}

	private void attachComponents() {
		headerPanel.add(title);
		headerPanel.add(subTitle);

		formPanel.add(dateRangeLabel, "growx");
		formPanel.add(dateRangeComboBox, "growx");

		buttonPanel.add(applyButton);

		add(headerPanel, "grow");
		add(scrollPane, "grow");
		add(buttonPanel, "grow");
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));

		buttonPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getForeground()),
						BorderFactory.createEmptyBorder(8, 0, 0, 0)));

		applyButton = StyledButtonFactory.createButton("Apply", ButtonStyles.PRIMARY);

		applyButton.setToolTipText("Apply the selected date range");

		applyButton.addActionListener(this::handleApplpy);
	}

	private void createComponents() {
		createHeaderPanel();
		createFormPanel();
		createButtonPanel();
	}

	private void createFormPanel() {
		formPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]"));

		dateRangeLabel = LabelFactory.createBoldLabel("Pick a date range", 12, Color.GRAY);
		dateRangeComboBox = new JComboBox<>(DateRange.values());
		dateRangeComboBox.setSelectedItem(DateRange.ALL_TIME);
		dateRangeComboBox.setFont(dateRangeComboBox.getFont().deriveFont(14f));

		scrollPane = new JScrollPane(formPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	}

	private void createHeaderPanel() {
		headerPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]4[top]"));

		title = LabelFactory.createBoldLabel("Pick a date range", 24);
		subTitle = LabelFactory.createLabel("Select a date range for inventory changes' breakdown", 14);
	}

	public void destroy() {
		removeWindowListener(windowListener);
		removeComponentListener(componentListener);

		if (applyButton != null) {
			applyButton.removeActionListener(this::handleApplpy);
		}
	}

	private void handleApplpy(ActionEvent ev) {
		onDateRangeSelected.accept((DateRange) dateRangeComboBox.getSelectedItem());

		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
