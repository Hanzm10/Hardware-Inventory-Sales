package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.combobox_renderers.PlaceholderRenderer;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class InventoryFilterDialog extends JDialog {
	private static record InventoryFilterComboBoxItem(@NotNull String value, @NotNull String display) {
		@Override
		public String toString() {
			return display;
		}
	}

	public static record FilterResult(@NotNull String category, @NotNull String supplier) {
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(InventoryFilterDialog.class);

	private JPanel formPanel;

	private JLabel categoryLabel;
	private JComboBox<InventoryFilterComboBoxItem> categoryFilterCombo;

	private JLabel supplierLabel;
	private JComboBox<InventoryFilterComboBoxItem> supplierFilterCombo;

	private JScrollPane scrollPane;

	private JPanel buttonPanel;
	private JButton applyButton;
	private JButton resetButton;

	private Consumer<FilterResult> onApply;

	public InventoryFilterDialog(Window owner, Consumer<FilterResult> onApply) {
		super(owner, "Filter Inventory", ModalityType.APPLICATION_MODAL);

		this.onApply = onApply;

		setLayout(new MigLayout("insets 16, flowy", "[grow]", "[grow,bottom]"));

		createComponents();
		attachComponents();

		pack();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void attachComponents() {
		formPanel.add(categoryLabel, "growx");
		formPanel.add(categoryFilterCombo, "growx");

		formPanel.add(Box.createVerticalStrut(2), "shrink");

		formPanel.add(supplierLabel, "growx");
		formPanel.add(supplierFilterCombo, "growx");

		buttonPanel.add(resetButton);
		buttonPanel.add(applyButton);

		add(scrollPane, "growx");
		add(buttonPanel, "growx");
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));

		buttonPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getForeground()),
						BorderFactory.createEmptyBorder(16, 0, 0, 0)));

		resetButton = StyledButtonFactory.createButton("Reset", ButtonStyles.SECONDARY);
		applyButton = StyledButtonFactory.createButton("Apply filters", ButtonStyles.PRIMARY);

		applyButton.setToolTipText("Apply the current selected filters");
		resetButton.setToolTipText("Reset the filters back to none");

		applyButton.addActionListener(this::handleApplyButton);
		resetButton.addActionListener(this::handleResetButton);
	}

	private void createComponents() {
		createFormPanel();
		createButtonPanel();
	}

	private void createFormPanel() {
		formPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[top]"));

		categoryLabel = LabelFactory.createBoldLabel("Filter by category", 12, Color.GRAY);
		categoryFilterCombo = new JComboBox<InventoryFilterDialog.InventoryFilterComboBoxItem>();
		categoryFilterCombo.setFont(categoryFilterCombo.getFont().deriveFont(14f));

		supplierLabel = LabelFactory.createBoldLabel("Filter by supplier", 12, Color.GRAY);
		supplierFilterCombo = new JComboBox<InventoryFilterDialog.InventoryFilterComboBoxItem>();
		supplierFilterCombo.setFont(supplierFilterCombo.getFont().deriveFont(14f));

		scrollPane = new JScrollPane(formPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		categoryFilterCombo.setRenderer(new PlaceholderRenderer(categoryFilterCombo));
		supplierFilterCombo.setRenderer(new PlaceholderRenderer(supplierFilterCombo));

		categoryFilterCombo.addItem(new InventoryFilterComboBoxItem("", "Select a category"));
		supplierFilterCombo.addItem(new InventoryFilterComboBoxItem("", "Select a supplier"));

		populateCategoryComboBox();
		populateSupplierComboBox();
	}

	public void destroy() {
		applyButton.removeActionListener(this::handleApplyButton);
		resetButton.removeActionListener(this::handleResetButton);
	}

	private void handleApplyButton(ActionEvent ev) {
		onApply.accept(new FilterResult(((InventoryFilterComboBoxItem) categoryFilterCombo.getSelectedItem()).value(),
				((InventoryFilterComboBoxItem) supplierFilterCombo.getSelectedItem()).value()));

		dispose();
	}

	private void handleResetButton(ActionEvent ev) {
		categoryFilterCombo.setSelectedIndex(0);
		supplierFilterCombo.setSelectedIndex(0);

		onApply.accept(new FilterResult(((InventoryFilterComboBoxItem) categoryFilterCombo.getSelectedItem()).value(),
				((InventoryFilterComboBoxItem) supplierFilterCombo.getSelectedItem()).value()));

		dispose();
	}

	private void populateCategoryComboBox() {
		try {
			var categories = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getCategoryDao()
					.getAllCategories();

			SwingUtilities.invokeLater(() -> {
				for (var category : categories) {
					this.categoryFilterCombo.addItem(new InventoryFilterComboBoxItem(category.name(), category.name()));
				}
			});
		} catch (SQLException | IOException e) {
			LOGGER.severe("Error fetching categories: " + e.getMessage());

			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Database Error",
						JOptionPane.ERROR_MESSAGE);
			});
		}
	}

	private void populateSupplierComboBox() {
		try {
			var suppliers = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getSupplierDao()
					.getAllSuppliers();

			SwingUtilities.invokeLater(() -> {
				for (var supplier : suppliers) {
					this.supplierFilterCombo.addItem(new InventoryFilterComboBoxItem(supplier.name(), supplier.name()));
				}
			});
		} catch (SQLException | IOException e) {
			LOGGER.severe("Error fetching packagings: " + e.getMessage());

			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Database Error",
						JOptionPane.ERROR_MESSAGE);
			});
		}
	}
}
