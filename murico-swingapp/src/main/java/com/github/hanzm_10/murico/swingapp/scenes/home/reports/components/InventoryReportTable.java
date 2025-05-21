package com.github.hanzm_10.murico.swingapp.scenes.home.reports.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.constants.Styles;
import com.github.hanzm_10.murico.swingapp.lib.comparators.NumberWithSymbolsComparator;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.InventoryBreakdown;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.SceneComponent;
import com.github.hanzm_10.murico.swingapp.lib.table_models.NonEditableTableModel;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.IdRenderer;
import com.github.hanzm_10.murico.swingapp.lib.table_renderers.InventoryRemarksRenderer;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.scenes.home.reports.components.dialogs.DateRangePickerDialog;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import net.miginfocom.swing.MigLayout;

public class InventoryReportTable implements SceneComponent {

	public static enum DateRange {
		WEEKLY, MONTHLY, YEARLY, ALL_TIME;

		public static DateRange fromString(String str) {
			return switch (str.toUpperCase()) {
			case "WEEKLY" -> WEEKLY;
			case "MONTHLY" -> MONTHLY;
			case "YEARLY" -> YEARLY;
			case "ALL_TIME" -> ALL_TIME;
			default -> MONTHLY;
			};
		}

		public String fromEndDate(LocalDate endDate) {
			return switch (this) {
			case WEEKLY -> endDate.minus(7, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
			case MONTHLY -> endDate.minus(1, ChronoUnit.MONTHS).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
			case YEARLY -> endDate.minus(1, ChronoUnit.YEARS).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
			case ALL_TIME -> LocalDate.of(1990, 1, 1).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
			};
		}

		public LocalDate fromEndDateAsDate(LocalDate endDate) {
			return switch (this) {
			case WEEKLY -> endDate.minus(7, ChronoUnit.DAYS);
			case MONTHLY -> endDate.minus(1, ChronoUnit.MONTHS);
			case YEARLY -> endDate.minus(1, ChronoUnit.YEARS);
			case ALL_TIME -> LocalDate.of(1990, 1, 1);
			};
		}

		@Override
		public String toString() {
			return switch (this) {
			case WEEKLY -> "Weekly";
			case MONTHLY -> "Monthly";
			case YEARLY -> "Yearly";
			case ALL_TIME -> "All Time";
			};
		}
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryReportTable.class);
	public static final int COL_ITEM_ID = 0;
	public static final int COL_ITEM_NAME = 1;
	public static final int COL_ITEM_CATEGORY = 2;
	public static final int COL_ITEM_PACKAGING = 3;
	public static final int COL_ITEM_INITIAL_QUANTITY = 4;
	public static final int COL_ITEM_SOLD_QUANTITY = 5;
	public static final int COL_ITEM_RESTOCK_QUANTITY = 6;
	public static final int COL_ITEM_QUANTITY = 7;

	public static final int COL_ITEM_REMARKS = 8;

	private JPanel view;
	private JPanel headerPanel;
	private JLabel title;
	private JPanel datePanel;
	private JLabel dateLabel;
	private DatePicker datePicker;
	private JButton dateRangeDialogOpener;
	private DateRangePickerDialog dateRangeDialog;

	private DefaultTableModel tableModel;
	private JTable table;

	private JScrollPane scrollPane;

	private AtomicReference<DateRange> dateRange = new AtomicReference<>(DateRange.ALL_TIME);
	private AtomicReference<LocalDate> selectedDate = new AtomicReference<>(LocalDate.now());

	private AtomicBoolean initialized = new AtomicBoolean(false);
	private AtomicReference<InventoryBreakdown[]> inventoryBreakdown = new AtomicReference<>(new InventoryBreakdown[0]);

	private void attachComponents() {
		headerPanel.add(title);
		headerPanel.add(datePanel);
		datePanel.add(dateLabel);
		datePanel.add(datePicker);
		datePanel.add(dateRangeDialogOpener);

		view.add(headerPanel, "growx");
		view.add(scrollPane, "grow");
	}

	private void createComponents() {
		view.setLayout(new MigLayout("insets 0, flowy", "[grow]", "[]8[grow]"));

		headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		title = LabelFactory.createBoldLabel(HtmlUtils.wrapInHtml("Inventory Breakdown"), 24, Styles.PRIMARY_COLOR);
		datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));

		initBtns();
		createDatePicker();

		var labelText = "From " + dateRange.get().fromEndDate(selectedDate.get()) + " to "
				+ selectedDate.get().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
		dateLabel = LabelFactory.createBoldItalicLabel(HtmlUtils.wrapInHtml(labelText), 12, Color.GRAY);
		dateLabel.setVerticalAlignment(SwingConstants.BOTTOM);

		tableModel = new NonEditableTableModel();
		table = new JTable(tableModel);

		table.setFont(table.getFont().deriveFont(Font.BOLD));
		table.setShowGrid(true);
		table.setRowHeight(40);
		table.setBackground(view.getBackground());

		scrollPane = new JScrollPane(table);

		var cellRenderer = new DefaultTableCellRenderer();
		var sorter = new TableRowSorter<TableModel>(tableModel);
		var comparator = new NumberWithSymbolsComparator();

		cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		tableModel.setColumnIdentifiers(InventoryBreakdown.getColumnNames());

		for (int i = 0; i < InventoryBreakdown.getColumnNames().length; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
		}

		sorter.setComparator(COL_ITEM_ID, comparator);
		sorter.setComparator(COL_ITEM_INITIAL_QUANTITY, comparator);
		sorter.setComparator(COL_ITEM_SOLD_QUANTITY, comparator);
		sorter.setComparator(COL_ITEM_RESTOCK_QUANTITY, comparator);
		sorter.setComparator(COL_ITEM_QUANTITY, comparator);

		table.setRowSorter(sorter);

		var header = table.getTableHeader();

		header.setBackground(Styles.SECONDARY_COLOR);
		header.setForeground(Styles.SECONDARY_FOREGROUND_COLOR);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		var columnModel = table.getColumnModel();
		columnModel.getColumn(COL_ITEM_ID).setCellRenderer(new IdRenderer());
		columnModel.getColumn(COL_ITEM_REMARKS).setCellRenderer(new InventoryRemarksRenderer());
	}

	private void createDatePicker() {
		datePicker = new DatePicker();

		var dateSettings = datePicker.getSettings();
		datePicker.getComponentToggleCalendarButton().setText("");
		datePicker.getComponentToggleCalendarButton().setBorder(new FlatRoundBorder());

		try {
			datePicker.getComponentToggleCalendarButton().setIcon(AssetManager.getOrLoadIcon("icons/calendar.svg"));
		} catch (URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "Failed to load calendar icon", e);
			datePicker.getComponentToggleCalendarButton().setText("📅");
		}

		datePicker.setDate(selectedDate.get());
		datePicker.setToolTipText("Select date");

		dateSettings.setVisibleDateTextField(false);
		dateSettings.setFormatForDatesBeforeCommonEra("MMM dd, yyyy");
		dateSettings.setAllowKeyboardEditing(false);
		dateSettings.setAllowEmptyDates(false);
		dateSettings.setDateRangeLimits(LocalDate.of(2015, 1, 1), LocalDate.now());
		dateSettings.setVisibleClearButton(false);

		datePicker.addDateChangeListener(this::handleDateChange);
	}

	@Override
	public void destroy() {
		if (datePicker != null) {
			datePicker.removeDateChangeListener(this::handleDateChange);
		}

		if (dateRangeDialogOpener != null) {
			dateRangeDialogOpener.removeActionListener(this::handleDateRangeClick);
		}
	}

	@Override
	public JPanel getView() {
		return view == null ? (view = new JPanel()) : view;
	}

	private void handleDateChange(DateChangeEvent ev) {
		selectedDate.set(datePicker.getDate());

		SwingUtilities.invokeLater(() -> {
			var labelText = "From " + dateRange.get().fromEndDate(selectedDate.get()) + " to "
					+ selectedDate.get().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
			dateLabel.setText(HtmlUtils.wrapInHtml(labelText));
		});

		performBackgroundTask();

		SwingUtilities.invokeLater(() -> {
			updateTable();
			view.validate();
		});
	}

	private void handleDateRangeClick(ActionEvent ev) {
		var owner = SwingUtilities.getWindowAncestor(view);

		if (dateRangeDialog == null) {
			dateRangeDialog = new DateRangePickerDialog(owner, this::handleDateRangeSelected);
		}

		dateRangeDialog.setLocationRelativeTo(owner);
		dateRangeDialog.setVisible(true);
	}

	private void handleDateRangeSelected(DateRange range) {
		dateRange.set(range);
		selectedDate.set(datePicker.getDate());

		SwingUtilities.invokeLater(() -> {
			var labelText = "From " + dateRange.get().fromEndDate(selectedDate.get()) + " to "
					+ selectedDate.get().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
			dateLabel.setText(HtmlUtils.wrapInHtml(labelText));
		});

		performBackgroundTask();
	}

	private void initBtns() {
		dateRangeDialogOpener = StyledButtonFactory.createButton("", ButtonStyles.TRANSPARENT);

		try {
			dateRangeDialogOpener.setIcon(AssetManager.getOrLoadIcon("icons/funnel-sm.svg"));
		} catch (URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "Failed to load funnel icon", e);
			dateRangeDialogOpener.setText("🔽");
		}

		dateRangeDialogOpener.setToolTipText("Select date range");
		dateRangeDialogOpener.setFocusable(false);
		dateRangeDialogOpener.addActionListener(this::handleDateRangeClick);
	}

	@Override
	public void initializeComponents() {
		if (initialized.get()) {
			return;
		}

		createComponents();
		attachComponents();

		initialized.set(true);
	}

	@Override
	public boolean isInitialized() {
		return initialized.get();
	}

	@Override
	public void performBackgroundTask() {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			inventoryBreakdown.set(factory.getItemDao()
					.getInventoryBreakdowns(dateRange.get().fromEndDateAsDate(selectedDate.get()), selectedDate.get()));
			SwingUtilities.invokeLater(this::updateTable);
		} catch (Exception e) {
			LOGGER.warning("Failed to get inventory breakdown: " + e.getMessage());
		}
	}

	private void updateTable() {
		if (!initialized.get()) {
			initializeComponents();
		}

		var data = inventoryBreakdown.get();
		tableModel.setRowCount(0);

		for (var item : data) {
			tableModel.addRow(item.toObjectArray());
		}

		view.validate();
	}

}
