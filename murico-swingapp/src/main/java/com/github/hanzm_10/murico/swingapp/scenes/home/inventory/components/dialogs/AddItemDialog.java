package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.swingapp.lib.combobox_renderers.PlaceholderRenderer;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.lib.utils.NumberUtils;
import com.github.hanzm_10.murico.swingapp.scenes.home.InventorySceneNew;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextFieldFactory;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class AddItemDialog extends JDialog {

	private static class ComboBoxItem {
		// ... (same as before) ...
		private final int id;
		private final String name;

		public ComboBoxItem(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static final Logger LOGGER = MuricoLogger.getLogger(AddItemDialog.class);

	private JScrollPane formScrollPane;

	private AtomicBoolean isUpdating = new AtomicBoolean(false);

	private JPanel headerPanel;
	private JLabel title;
	private JLabel subtitle;

	private JPanel formPanel;

	private JLabel itemNameLabel;
	private JTextField itemName;
	private JLabel itemNameError;

	private JLabel itemDescriptionLabel;
	private JTextArea itemDescription;
	private JLabel itemDescriptionError;

	private JLabel categoryLabel;
	private JComboBox<ComboBoxItem> category;
	private JLabel categoryError;

	private JLabel packagingLabel;
	private JComboBox<ComboBoxItem> packaging;
	private JLabel packagingError;

	private JLabel initialQuantityLabel;
	private JSpinner initialQuantity;
	private JLabel initialQuantityError;

	private JLabel minQuantityLabel;
	private JSpinner minQuantity;
	private JLabel minQuantityError;

	private JLabel sellingPriceLabel;
	private JSpinner sellingPrice;
	private JLabel sellingPriceError;

	private JLabel srpLabel;
	private JSpinner srp;
	private JLabel srpError;

	private JLabel supplierLabel;
	private JComboBox<ComboBoxItem> supplier;
	private JLabel supplierNameError;

	private JLabel costPriceLabel;
	private JSpinner costPrice;
	private JLabel costPriceError;

	private JPanel buttonPanel;

	private JButton cancelButton;
	private JButton confirmButton;

	private Thread fetchThread;
	private Thread updateThread;

	private InventorySceneNew parent;

	public AddItemDialog(Window owner, InventorySceneNew parent) {
		super(owner, "Add New Item", Dialog.ModalityType.APPLICATION_MODAL);

		this.parent = parent;
		setLayout(new MigLayout("insets 16, flowy", "[grow]", "[grow]"));

		createComponents();
		attachComponents();

		pack();
		setLocationRelativeTo(owner);

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (fetchThread != null && fetchThread.isAlive()) {
					fetchThread.interrupt();
					ConnectionManager.cancel(fetchThread);
				}

				if (isUpdating.get()) {
					var confirm = JOptionPane.showConfirmDialog(AddItemDialog.this,
							"Are you sure you want to cancel the operation?", "Confirm Cancel",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

					if (confirm != JOptionPane.YES_OPTION) {
						return;
					}
				}

				if (updateThread != null && updateThread.isAlive()) {
					updateThread.interrupt();
					ConnectionManager.cancel(updateThread);
				}

				dispose();
			}
		});
	}

	private void attachComponents() {
		headerPanel.add(title, "grow");
		headerPanel.add(subtitle, "grow");

		formPanel.add(itemNameLabel, "grow");
		formPanel.add(itemName, "grow");
		formPanel.add(itemNameError, "grow");

		formPanel.add(itemDescriptionLabel, "grow");
		formPanel.add(itemDescription, "grow");
		formPanel.add(itemDescriptionError, "grow");

		formPanel.add(categoryLabel, "grow");
		formPanel.add(category, "grow");
		formPanel.add(categoryError, "grow");

		formPanel.add(packagingLabel, "grow");
		formPanel.add(packaging, "grow");
		formPanel.add(packagingError, "grow");

		formPanel.add(initialQuantityLabel, "grow");
		formPanel.add(initialQuantity, "grow");
		formPanel.add(initialQuantityError, "grow");

		formPanel.add(minQuantityLabel, "grow");
		formPanel.add(minQuantity, "grow");
		formPanel.add(minQuantityError, "grow");

		formPanel.add(sellingPriceLabel, "grow");
		formPanel.add(sellingPrice, "grow");
		formPanel.add(sellingPriceError, "grow");

		formPanel.add(srpLabel, "grow");
		formPanel.add(srp, "grow");
		formPanel.add(srpError, "grow");

		formPanel.add(supplierLabel, "grow");
		formPanel.add(supplier, "grow");
		formPanel.add(supplierNameError, "grow");

		formPanel.add(costPriceLabel, "grow");
		formPanel.add(costPrice, "grow");
		formPanel.add(costPriceError, "grow");

		buttonPanel.add(cancelButton);
		buttonPanel.add(confirmButton);

		add(headerPanel, "grow");
		add(formScrollPane, "grow");
		add(buttonPanel, "grow");
	}

	private void clearErrorMessages() {
		itemNameError.setText("");
		itemDescriptionError.setText("");
		categoryError.setText("");
		packagingError.setText("");
		initialQuantityError.setText("");
		minQuantityError.setText("");
		sellingPriceError.setText("");
		srpError.setText("");
		supplierNameError.setText("");
		costPriceError.setText("");
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));

		cancelButton = StyledButtonFactory.createButton("Cancel", ButtonStyles.DANGER);
		confirmButton = StyledButtonFactory.createButton("Save Item", ButtonStyles.PRIMARY);

		cancelButton.addActionListener(this::handleCancelButton);
		confirmButton.addActionListener(this::handleSaveButton);
	}

	private void createComponents() {
		createHeaderPanel();
		createFormPanel();
		createButtonPanel();
	}

	private void createFormPanel() {
		formPanel = new JPanel(new MigLayout("insets 0, flowy", "[grow]", "[grow]"));

		itemNameLabel = LabelFactory.createLabel("Item Name*", 12, Color.GRAY);
		itemName = TextFieldFactory.createTextField("Item Name", 14);
		itemNameError = LabelFactory.createErrorLabel("Item Name is required.", 10);

		itemDescriptionLabel = LabelFactory.createLabel("Description", 12, Color.GRAY);
		itemDescription = TextFieldFactory.createTextArea(14);
		itemDescription.setRows(3);
		itemDescriptionError = LabelFactory.createErrorLabel("Description is required.", 10);

		categoryLabel = LabelFactory.createLabel("Category*", 12, Color.GRAY);
		category = new JComboBox<>();
		categoryError = LabelFactory.createErrorLabel("Category is required.", 10);

		packagingLabel = LabelFactory.createLabel("Packaging*", 12, Color.GRAY);
		packaging = new JComboBox<>();
		packagingError = LabelFactory.createErrorLabel("Packaging is required.", 10);

		initialQuantityLabel = LabelFactory.createLabel("Initial Quantity*", 12, Color.GRAY);
		initialQuantity = new JSpinner(new SpinnerNumberModel(1, 0, 1_000_000, 1));
		initialQuantityError = LabelFactory.createErrorLabel("Initial Quantity is required.", 10);

		minQuantityLabel = LabelFactory.createLabel("Min. Stock Qty*", 12, Color.GRAY);
		minQuantity = new JSpinner(new SpinnerNumberModel(0, 0, 100_000, 1));
		minQuantityError = LabelFactory.createErrorLabel("Min. Stock Qty is required.", 10);

		sellingPriceLabel = LabelFactory.createLabel("Selling Price (₱)*", 12, Color.GRAY);
		sellingPrice = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 0.01));
		sellingPriceError = LabelFactory.createErrorLabel("Selling Price is required.", 10);

		srpLabel = LabelFactory.createLabel("SRP (₱)*", 12, Color.GRAY);
		srp = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 0.01));
		srpError = LabelFactory.createErrorLabel("SRP is required.", 10);

		supplierLabel = LabelFactory.createLabel("Supplier*", 12, Color.GRAY);
		supplier = new JComboBox<>();
		supplierNameError = LabelFactory.createErrorLabel("Supplier Name is required.", 10);

		costPriceLabel = LabelFactory.createLabel("Cost Price (₱ from Supplier)", 12, Color.GRAY);
		costPrice = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 0.01));
		costPriceError = LabelFactory.createErrorLabel("Cost Price is required.", 10);

		formScrollPane = new JScrollPane(formPanel);
		formScrollPane.setBorder(null);

		category.setRenderer(new PlaceholderRenderer(category));
		packaging.setRenderer(new PlaceholderRenderer(packaging));
		supplier.setRenderer(new PlaceholderRenderer(supplier));

		category.addItem(new ComboBoxItem(-1, "Select Category"));
		packaging.addItem(new ComboBoxItem(-1, "Select Packaging"));
		supplier.addItem(new ComboBoxItem(-1, "Select Supplier"));

		populateCategoryComboBox();
		populatePackagingComboBox();
		populateSupplierComboBox();
	}

	private void createHeaderPanel() {
		headerPanel = new JPanel();
		headerPanel.setLayout(new MigLayout("insets 0, flowy", "[grow, center]", "[]16[grow]"));

		title = LabelFactory.createBoldLabel("Add New Item", 26);
		subtitle = LabelFactory.createLabel("Input all required information for the new item.", 12, Color.GRAY);
	}

	private void disableButtons() {
		cancelButton.setEnabled(false);
		confirmButton.setEnabled(false);
	}

	private void enableButtons() {
		cancelButton.setEnabled(true);
		confirmButton.setEnabled(true);
	}

	private void handleCancelButton(ActionEvent ev) {
		dispose();
	}

	private void handleSaveButton(ActionEvent ev) {
		saveNewItem();
	}

	private boolean isValid(int initQty, int minQty, String itemName, String itemDescription,
			ComboBoxItem selectedCategory, ComboBoxItem selectedPackaging, ComboBoxItem selectedSupplier,
			BigDecimal sellingPrice, BigDecimal srp, BigDecimal costPrice) {
		var isValid = true;

		if (itemName.isEmpty()) {
			itemNameError.setText(HtmlUtils.wrapInHtml("Item Name is required."));
			isValid = false;
		} else if (itemName.length() > 100) {
			itemNameError.setText(HtmlUtils.wrapInHtml("Item Name must be less than 100 characters."));
			isValid = false;
		} else if (itemName.length() < 3) {
			itemNameError.setText(HtmlUtils.wrapInHtml("Item Name must be at least 3 characters."));
			isValid = false;
		}

		if (itemDescription.isEmpty()) {
			itemDescriptionError.setText(HtmlUtils.wrapInHtml("Description is required."));
			isValid = false;
		} else if (itemDescription.length() < 10) {
			itemDescriptionError.setText(HtmlUtils.wrapInHtml("Description must be at least 10 characters."));
			isValid = false;
		} else if (itemDescription.length() > 255) {
			itemDescriptionError.setText(HtmlUtils.wrapInHtml("Description must be less than 255 characters."));
			isValid = false;
		}

		if (selectedCategory == null || selectedCategory.getId() == -1) {
			categoryError.setText(HtmlUtils.wrapInHtml("Category is required."));
			isValid = false;
		}

		if (selectedPackaging == null || selectedPackaging.getId() == -1) {
			packagingError.setText(HtmlUtils.wrapInHtml("Packaging is required."));
			isValid = false;
		}

		if (selectedSupplier == null || selectedSupplier.getId() == -1) {
			supplierNameError.setText(HtmlUtils.wrapInHtml("Supplier is required."));
			isValid = false;
		}

		if (initQty <= 0) {
			initialQuantityError.setText(HtmlUtils.wrapInHtml("Initial Quantity must be greater than 0."));
			isValid = false;
		} else if (initQty > 10000) {
			initialQuantityError.setText(HtmlUtils.wrapInHtml("Initial Quantity must be less than 10,000."));
			isValid = false;
		}

		if (minQty < 0) {
			minQuantityError.setText(HtmlUtils.wrapInHtml("Min. Stock Qty must be greater than or equal to 0."));
			isValid = false;
		}

		if (sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
			sellingPriceError.setText(HtmlUtils.wrapInHtml("Selling Price must be greater than 0."));
			isValid = false;
		}

		if (srp.compareTo(BigDecimal.ZERO) <= 0) {
			srpError.setText(HtmlUtils.wrapInHtml("SRP must be greater than 0."));
			isValid = false;
		}

		if (costPrice.compareTo(BigDecimal.ZERO) <= 0) {
			costPriceError.setText(HtmlUtils.wrapInHtml("Cost Price must be greater than 0."));
			isValid = false;
		}

		return isValid;
	}

	private void populateCategoryComboBox() {
		try {
			var categories = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getCategoryDao()
					.getAllCategories();

			for (var category : categories) {
				this.category.addItem(new ComboBoxItem(category._itemCategoryId(), category.name()));
			}
		} catch (SQLException | IOException e) {
			LOGGER.severe("Error fetching categories: " + e.getMessage());
			JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void populatePackagingComboBox() {
		try {
			var packagings = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getPackagingDao()
					.getAllPackagings();

			for (var packaging : packagings) {
				this.packaging.addItem(new ComboBoxItem(packaging._packagingId(), packaging.name()));
			}
		} catch (SQLException | IOException e) {
			LOGGER.severe("Error fetching packagings: " + e.getMessage());
			JOptionPane.showMessageDialog(this, "Error loading packagings: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void populateSupplierComboBox() {
		try {
			var suppliers = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getSupplierDao()
					.getAllSuppliers();

			for (var supplier : suppliers) {
				this.supplier.addItem(new ComboBoxItem(supplier._supplierId(), supplier.name()));
			}
		} catch (SQLException | IOException e) {
			LOGGER.severe("Error fetching packagings: " + e.getMessage());
			JOptionPane.showMessageDialog(this, "Error loading packagings: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void saveNewItem() {
		var itemName = this.itemName.getText().trim();
		var itemDescription = this.itemDescription.getText().trim();
		var selectedCategory = (ComboBoxItem) category.getSelectedItem();
		var selectedPackaging = (ComboBoxItem) packaging.getSelectedItem();
		var selectedSupplier = (ComboBoxItem) supplier.getSelectedItem();
		var initialQty = (int) initialQuantity.getValue();
		var minQty = (int) minQuantity.getValue();
		var sellingPrice = NumberUtils.getSpinnerBigDecimal(this.sellingPrice);
		var srp = NumberUtils.getSpinnerBigDecimal(this.srp);
		var costPrice = NumberUtils.getSpinnerBigDecimal(this.costPrice);

		clearErrorMessages();

		if (!isValid(initialQty, minQty, itemName, itemDescription, selectedCategory, selectedPackaging,
				selectedSupplier, sellingPrice, srp, costPrice)) {
			return;
		}

		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		disableButtons();

		updateThread = new Thread(() -> {
			try {
				factory.getItemDao().addItem(initialQty, minQty, itemName, itemDescription, selectedCategory.getId(),
						selectedPackaging.getId(), selectedSupplier.getId(), sellingPrice, srp, costPrice);
				SwingUtilities.invokeLater(() -> {
					enableButtons();
					JOptionPane.showMessageDialog(this, "Item '" + itemName + "' added successfully!", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
					parent.refreshTableData();
				});
			} catch (SQLException | IOException e) {
				LOGGER.log(Level.SEVERE, "Error adding item: ", e);
				SwingUtilities.invokeLater(() -> {
					enableButtons();
					LOGGER.severe("Error adding item: " + e.getMessage());
					JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(), "Database Error",
							JOptionPane.ERROR_MESSAGE);
				});
			}
		});

		updateThread.start();
	}
}