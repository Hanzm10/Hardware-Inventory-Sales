package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;
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
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.combobox_renderers.PlaceholderRenderer;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.ItemStock;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.HtmlUtils;
import com.github.hanzm_10.murico.swingapp.lib.utils.NumberUtils;
import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.dialogs.SuccessDialog;
import com.github.hanzm_10.murico.swingapp.ui.inputs.TextFieldFactory;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;

import net.miginfocom.swing.MigLayout;

public class AddItemDialog extends JDialog {

	private static record ComboBoxItem(@NotNull int id, @NotNull String name) {
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
	private JLabel subTitle;

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

	private ExecutorService executor;

	private WindowAdapter windowListener;
	private ComponentAdapter componentListener;

	private Consumer<ItemStock> onUpdate;

	public AddItemDialog(Window owner, Consumer<ItemStock> onUpdate) {
		super(owner, "Add New Item", Dialog.ModalityType.APPLICATION_MODAL);

		this.onUpdate = onUpdate;

		setLayout(new MigLayout("insets 16, flowy, gap 2 16", "[grow]", "[grow]"));

		createComponents();
		attachComponents();

		this.windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (executor != null && !executor.isShutdown()) {
					executor.shutdownNow();
				}

				clearErrorMessages();
				clearFields();

				validate();

				dispose();
			}
		};
		this.componentListener = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				if (executor != null && !executor.isShutdown()) {
					executor.shutdownNow();
				}

				executor = Executors.newFixedThreadPool(2);

				executor.submit(() -> {
					populateCategoryComboBox();
					populatePackagingComboBox();
					populateSupplierComboBox();
				});
			}
		};

		pack();
		setSize(new Dimension(500, 500));

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(windowListener);
		addComponentListener(componentListener);
	}

	private void attachComponents() {
		headerPanel.add(title, "grow");
		headerPanel.add(subTitle, "grow");

		formPanel.add(itemNameLabel, "grow");
		formPanel.add(itemName, "grow");
		formPanel.add(itemNameError, "grow");

		formPanel.add(Box.createVerticalStrut(2));

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

	private void clearFields() {
		itemName.setText("");
		itemDescription.setText("");
		category.setSelectedIndex(0);
		packaging.setSelectedIndex(0);
		initialQuantity.setValue(1);
		minQuantity.setValue(0);
		sellingPrice.setValue(0.00);
		srp.setValue(0.00);
		supplier.setSelectedIndex(0);
		costPrice.setValue(0.00);
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));

		buttonPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getForeground()),
						BorderFactory.createEmptyBorder(8, 0, 0, 0)));

		cancelButton = StyledButtonFactory.createButton("Cancel", ButtonStyles.SECONDARY);
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

		itemNameLabel = LabelFactory.createBoldLabel("Item Name*", 12, Color.GRAY);
		itemName = TextFieldFactory.createTextField("Item Name", 14);
		itemNameError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Item Name is required."), 9);

		itemDescriptionLabel = LabelFactory.createBoldLabel("Description", 12, Color.GRAY);
		itemDescription = TextFieldFactory.createTextArea(14);
		itemDescription.setRows(3);
		itemDescriptionError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Description is required."), 9);

		categoryLabel = LabelFactory.createBoldLabel("Category*", 12, Color.GRAY);
		category = new JComboBox<>();
		category.setFont(category.getFont().deriveFont(14f));
		categoryError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Category is required."), 9);

		packagingLabel = LabelFactory.createBoldLabel("Packaging*", 12, Color.GRAY);
		packaging = new JComboBox<>();
		packaging.setFont(packaging.getFont().deriveFont(14f));
		packagingError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Packaging is required."), 9);

		initialQuantityLabel = LabelFactory.createBoldLabel("Initial Quantity*", 12, Color.GRAY);
		initialQuantity = new JSpinner(new SpinnerNumberModel(1, 0, 1_000_000, 1));
		initialQuantity.setFont(initialQuantity.getFont().deriveFont(14f));
		initialQuantityError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Initial Quantity is required."), 9);

		minQuantityLabel = LabelFactory.createBoldLabel("Min. Stock Qty*", 12, Color.GRAY);
		minQuantity = new JSpinner(new SpinnerNumberModel(0, 0, 100_000, 1));
		minQuantity.setFont(minQuantity.getFont().deriveFont(14f));
		minQuantityError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Min. Stock Qty is required."), 9);

		sellingPriceLabel = LabelFactory.createBoldLabel("Selling Price (₱)*", 12, Color.GRAY);
		sellingPrice = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 10));
		sellingPrice.setFont(sellingPrice.getFont().deriveFont(14f));
		sellingPriceError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Selling Price is required."), 9);

		srpLabel = LabelFactory.createBoldLabel("SRP (₱)*", 12, Color.GRAY);
		srp = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 10));
		srp.setFont(srp.getFont().deriveFont(14f));
		srpError = LabelFactory.createErrorLabel("SRP is required.", 9);

		supplierLabel = LabelFactory.createBoldLabel("Supplier*", 12, Color.GRAY);
		supplier = new JComboBox<>();
		supplier.setFont(supplier.getFont().deriveFont(14f));
		supplierNameError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Supplier Name is required."), 9);

		costPriceLabel = LabelFactory.createBoldLabel("Cost Price (₱ from Supplier)", 12, Color.GRAY);
		costPrice = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 10));
		costPrice.setFont(costPrice.getFont().deriveFont(14f));
		costPriceError = LabelFactory.createErrorLabel(HtmlUtils.wrapInHtml("Cost Price is required."), 9);

		formScrollPane = new JScrollPane(formPanel);
		formScrollPane.setBorder(null);
		formScrollPane.getVerticalScrollBar().setUnitIncrement(16);

		category.setRenderer(new PlaceholderRenderer<ComboBoxItem>(category));
		packaging.setRenderer(new PlaceholderRenderer<ComboBoxItem>(packaging));
		supplier.setRenderer(new PlaceholderRenderer<ComboBoxItem>(supplier));

		category.addItem(new ComboBoxItem(-1, "Select Category"));
		packaging.addItem(new ComboBoxItem(-1, "Select Packaging"));
		supplier.addItem(new ComboBoxItem(-1, "Select Supplier"));
	}

	private void createHeaderPanel() {
		headerPanel = new JPanel(new MigLayout("insets 0, flowy, gap 2 4", "[grow]", "[top]"));

		title = LabelFactory.createBoldLabel("Add New Item", 24);
		subTitle = LabelFactory.createBoldLabel("Fill in the details below to add a new item.", 14, Color.GRAY);
	}

	public void destroy() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}

		removeWindowListener(windowListener);
		removeComponentListener(componentListener);

		cancelButton.removeActionListener(this::handleCancelButton);
		confirmButton.removeActionListener(this::handleSaveButton);
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
		SwingUtilities.invokeLater(() -> {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		});
	}

	private void handleSaveButton(ActionEvent ev) {
		SwingUtilities.invokeLater(this::saveNewItem);
	}

	private boolean isValid(int initQty, int minQty, String itemName, String itemDescription,
			ComboBoxItem selectedCategory, ComboBoxItem selectedPackaging, ComboBoxItem selectedSupplier,
			BigDecimal sellingPrice, BigDecimal srp, BigDecimal costPrice) {
		var isValid = true;

		if (itemName.isEmpty()) {
			SwingUtilities.invokeLater(() -> {
				itemNameError.setText(HtmlUtils.wrapInHtml("Item Name is required."));
			});
			isValid = false;
		} else if (itemName.length() > 100) {
			SwingUtilities.invokeLater(() -> {
				itemNameError.setText(HtmlUtils.wrapInHtml("Item Name must be less than 100 characters."));
			});
			isValid = false;
		} else if (itemName.length() < 3) {
			SwingUtilities.invokeLater(() -> {
				itemNameError.setText(HtmlUtils.wrapInHtml("Item Name must be at least 3 characters."));
			});
			isValid = false;
		}

		if (itemDescription.length() > 255) {
			SwingUtilities.invokeLater(() -> {
				itemDescriptionError.setText(HtmlUtils.wrapInHtml("Description must be less than 255 characters."));
			});
			isValid = false;
		}

		if (selectedCategory == null || selectedCategory.id() == -1) {
			SwingUtilities.invokeLater(() -> {
				categoryError.setText(HtmlUtils.wrapInHtml("Category is required."));
			});
			isValid = false;
		}

		if (selectedPackaging == null || selectedPackaging.id() == -1) {
			SwingUtilities.invokeLater(() -> {
				packagingError.setText(HtmlUtils.wrapInHtml("Packaging is required."));
			});
			isValid = false;
		}

		if (selectedSupplier == null || selectedSupplier.id() == -1) {
			SwingUtilities.invokeLater(() -> {
				supplierNameError.setText(HtmlUtils.wrapInHtml("Supplier is required."));
			});
			isValid = false;
		}

		if (initQty <= 0) {
			SwingUtilities.invokeLater(() -> {
				initialQuantityError.setText(HtmlUtils.wrapInHtml("Initial Quantity must be greater than 0."));
			});
			isValid = false;
		} else if (initQty > 10000) {
			SwingUtilities.invokeLater(() -> {
				initialQuantityError.setText(HtmlUtils.wrapInHtml("Initial Quantity must be less than 10,000."));
			});
			isValid = false;
		}

		if (minQty < 0) {
			SwingUtilities.invokeLater(() -> {
				minQuantityError.setText(HtmlUtils.wrapInHtml("Min. Stock Qty must be greater than or equal to 0."));
			});
			isValid = false;
		}

		if (sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
			SwingUtilities.invokeLater(() -> {
				sellingPriceError.setText(HtmlUtils.wrapInHtml("Selling Price must be greater than 0."));
			});
			isValid = false;
		}

		if (srp.compareTo(BigDecimal.ZERO) <= 0) {
			SwingUtilities.invokeLater(() -> {
				srpError.setText(HtmlUtils.wrapInHtml("SRP must be greater than 0."));
			});
			isValid = false;
		}

		if (costPrice.compareTo(BigDecimal.ZERO) <= 0) {
			SwingUtilities.invokeLater(() -> {
				costPriceError.setText(HtmlUtils.wrapInHtml("Cost Price must be greater than 0."));
			});
			isValid = false;
		}

		return isValid;
	}

	private void populateCategoryComboBox() {
		try {
			var categories = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getCategoryDao()
					.getAllCategories();

			SwingUtilities.invokeLater(() -> {
				var selectedItem = (ComboBoxItem) category.getSelectedItem();

				this.category.removeAllItems();
				category.addItem(new ComboBoxItem(-1, "Select Category"));

				for (var category : categories) {
					this.category.addItem(new ComboBoxItem(category._itemCategoryId(), category.name()));
				}

				if (selectedItem != null) {
					this.category.setSelectedItem(selectedItem);
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

	private void populatePackagingComboBox() {
		try {
			var packagings = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getPackagingDao()
					.getAllPackagings();

			SwingUtilities.invokeLater(() -> {
				var selectedItem = (ComboBoxItem) packaging.getSelectedItem();

				this.packaging.removeAllItems();
				packaging.addItem(new ComboBoxItem(-1, "Select Packaging"));

				for (var packaging : packagings) {
					this.packaging.addItem(new ComboBoxItem(packaging._packagingId(), packaging.name()));
				}

				if (selectedItem != null) {
					this.packaging.setSelectedItem(selectedItem);
				}
			});
		} catch (SQLException | IOException e) {
			LOGGER.severe("Error fetching packagings: " + e.getMessage());

			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, "Error loading packagings: " + e.getMessage(), "Database Error",
						JOptionPane.ERROR_MESSAGE);
			});
		}
	}

	private void populateSupplierComboBox() {
		try {
			var suppliers = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL).getSupplierDao()
					.getAllSuppliers();

			SwingUtilities.invokeLater(() -> {
				var selectedItem = (ComboBoxItem) supplier.getSelectedItem();

				this.supplier.removeAllItems();
				supplier.addItem(new ComboBoxItem(-1, "Select Supplier"));

				for (var supplier : suppliers) {
					this.supplier.addItem(new ComboBoxItem(supplier._supplierId(), supplier.name()));
				}

				if (selectedItem != null) {
					this.supplier.setSelectedItem(selectedItem);
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

		SwingUtilities.invokeLater(() -> {
			clearErrorMessages();
		});

		if (!isValid(initialQty, minQty, itemName, itemDescription, selectedCategory, selectedPackaging,
				selectedSupplier, sellingPrice, srp, costPrice)) {
			return;
		}

		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		SwingUtilities.invokeLater(() -> {
			disableButtons();
		});

		executor.submit(() -> {
			isUpdating.set(true);

			try {
				var generatedIds = factory.getItemDao().addItem(initialQty, minQty, itemName, itemDescription,
						selectedCategory.id(), selectedPackaging.id(), selectedSupplier.id(), sellingPrice, srp,
						costPrice);

				SwingUtilities.invokeLater(() -> {
					new SuccessDialog(this, "Item '" + itemName + "' added successfully!").setVisible(true);
					onUpdate.accept(new ItemStock(generatedIds.itemStockId(), generatedIds.itemId(),
							selectedCategory.name(), selectedPackaging.name(), selectedSupplier.name(), itemName,
							initialQty, sellingPrice, minQty));
					dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				});
			} catch (SQLException | IOException e) {
				LOGGER.log(Level.SEVERE, "Error adding item: ", e);

				SwingUtilities.invokeLater(() -> {
					LOGGER.severe("Error adding item: " + e.getMessage());
					JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				});
			} finally {
				SwingUtilities.invokeLater(() -> {
					enableButtons();
					isUpdating.set(false);
				});
			}
		});
	}
}