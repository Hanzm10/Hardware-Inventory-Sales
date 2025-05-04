package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
// --- End Imports ---

// --- Imports --- // Adjust if needed
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;

/**
 * A component responsible for searching inventory items (including packaging)
 * with live suggestions, based on the item_stocks table.
 */
public class CheckoutSearchComponent extends JPanel {

	// --- Updated Data Record ---
	// Includes item_stock_id and packaging name
	public static record ItemData(int itemStockId, // _item_stock_id
			int itemId, // _item_id
			String itemName, // items.name
			String packagingName, // packagings.name
			BigDecimal price, // item_stocks.srp_php
			int currentStock // item_stocks.quantity
	) {
		// Updated toString for display in JList
		@Override
		public String toString() {
			final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₱ #,##0.00");
			return String.format("%s (%s) (Stock:%d, %s)", itemName, packagingName, currentStock,
					CURRENCY_FORMAT.format(price));
			// Example: Hammer (Each) (Stock:150, ₱ 15.99)
		}
	}

	// --- Listener Interface for Selection (using updated ItemData) ---
	public interface ItemSelectedListener {
		void itemSelected(ItemData selectedItem);
	}

	// --- SwingWorker for Asynchronous Database Search ---
	private class SearchWorker extends SwingWorker<List<ItemData>, Void> {
		private final String searchTerm;

		SearchWorker(String searchTerm) {
			this.searchTerm = searchTerm;
		}

		@Override
		protected List<ItemData> doInBackground() throws Exception {
			System.out.println("SearchWorker: Searching DB for '" + searchTerm + "'...");
			List<ItemData> results = new ArrayList<>();

			// --- UPDATED SQL QUERY ---
			String sql = """
					SELECT
					    iss._item_stock_id,
					    i._item_id,
					    i.name,
					    p.name AS packaging_name,
					    iss.srp_php,
					    iss.quantity
					FROM item_stocks iss
					JOIN items i ON iss._item_id = i._item_id
					JOIN packagings p ON iss._packaging_id = p._packaging_id
					WHERE i.name LIKE ? AND iss.quantity > 0
					ORDER BY i.name, p.name
					LIMIT 10;
					"""; // Removed branch filter, added joins, updated columns

			try (Connection conn = MySqlFactoryDao.createConnection();
					PreparedStatement pstmt = conn.prepareStatement(sql)) {

				pstmt.setString(1, "%" + searchTerm + "%"); // Set search term

				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next() && !isCancelled()) {
						// --- Instantiate UPDATED ItemData record ---
						results.add(new ItemData(rs.getInt("_item_stock_id"), rs.getInt("_item_id"),
								rs.getString("name"), rs.getString("packaging_name"), rs.getBigDecimal("srp_php"), // Use
																													// srp_php
								rs.getInt("quantity")));
					}
				}
			} catch (SQLException e) {
				System.err.println("SearchWorker DB Error: " + e.getMessage());
				throw e; // Propagate exception
			}
			System.out.println("SearchWorker: Found " + results.size() + " stock entries for '" + searchTerm + "'.");
			return results;
		}

		@Override
		protected void done() {
			// --- done() method remains largely the same ---
			// It updates the suggestionsListModel with ItemData objects
			// and calculates/sets the popup height.
			if (isCancelled()) {
				System.out.println("SearchWorker cancelled: " + searchTerm);
				return;
			}
			try {
				List<ItemData> results = get();
				suggestionsListModel.clear();
				boolean hasResults = !results.isEmpty();
				if (hasResults) {
					for (ItemData item : results) {
						suggestionsListModel.addElement(item);
					}
				}

				if (hasResults && itemSearchField.isFocusOwner()
						&& itemSearchField.getText().trim().equals(searchTerm)) {
					int listSize = suggestionsListModel.getSize();
					int desiredPopupHeight = 0;
					if (listSize > 0) {
						Rectangle cb = suggestionsList.getCellBounds(0, 0);
						if (cb == null) {
							suggestionsList.validate();
							cb = suggestionsList.getCellBounds(0, 0);
						}
						if (cb != null) {
							int cH = cb.height > 0 ? cb.height : 18;
							desiredPopupHeight = cH * listSize;
							Insets si = suggestionScrollPane.getInsets();
							desiredPopupHeight += si.top + si.bottom + 2;
						} else {
							desiredPopupHeight = 150;
						}
						int minH = 30;
						int maxH = 200;
						desiredPopupHeight = Math.max(minH, Math.min(desiredPopupHeight, maxH));
					}
					if (desiredPopupHeight > 0) {
						suggestionScrollPane
								.setPreferredSize(new Dimension(itemSearchField.getWidth(), desiredPopupHeight));
						suggestionsPopup.pack();
						if (!suggestionsPopup.isVisible()) {
							suggestionsPopup.show(itemSearchField, 0, itemSearchField.getHeight());
							System.out.println("Showing suggestions: " + searchTerm);
						}
					} else {
						if (suggestionsPopup.isVisible()) {
							suggestionsPopup.setVisible(false);
						}
					}
				} else {
					if (suggestionsPopup.isVisible()) {
						suggestionsPopup.setVisible(false);
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				if (suggestionsPopup != null) {
					suggestionsPopup.setVisible(false);
				}
			} catch (ExecutionException e) {
				e.getCause().printStackTrace();
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(CheckoutSearchComponent.this),
						"Search Err: " + e.getCause().getMessage(), "Error", 0);
				if (suggestionsPopup != null) {
					suggestionsPopup.setVisible(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (suggestionsPopup != null) {
					suggestionsPopup.setVisible(false);
				}
			} finally {
				if (currentSearchWorker == this) {
					currentSearchWorker = null;
				}
			}
		}
	} // --- End SearchWorker ---

	private static final int SEARCH_TRIGGER_LENGTH = 2;
	// --- UI Components ---
	private final JTextField itemSearchField;
	private final JPopupMenu suggestionsPopup;
	private final JList<ItemData> suggestionsList;

	private final DefaultListModel<ItemData> suggestionsListModel;
	private final JScrollPane suggestionScrollPane;
	// --- State & Configuration ---
	private final ItemSelectedListener itemSelectedListener;

	private SearchWorker currentSearchWorker = null;

	/**
	 * Creates the search component. No longer needs branchId.
	 *
	 * @param listener The listener to notify when an item is selected.
	 */
	public CheckoutSearchComponent(ItemSelectedListener listener) {
		// this.currentBranchId = branchId; // Removed branchId dependency
		this.itemSelectedListener = listener;
		if (listener == null) {
			throw new IllegalArgumentException("ItemSelectedListener cannot be null.");
		}

		setLayout(new BorderLayout(5, 0));
		setOpaque(false);

		// --- Initialize Components ---
		add(new JLabel("Search Item: "), BorderLayout.WEST);

		itemSearchField = new JTextField();
		itemSearchField.setFont(new Font("Montserrat Regular", Font.BOLD, 12));
		add(itemSearchField, BorderLayout.CENTER);

		JLabel searchIconLabel = new JLabel("\uD83D\uDD0D");
		styleIconButtonLabel(searchIconLabel);
		add(searchIconLabel, BorderLayout.EAST);

		// --- Initialize Search Suggestions Popup ---
		suggestionsPopup = new JPopupMenu();
		suggestionsPopup.setFocusable(false);
		suggestionsPopup.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		suggestionsListModel = new DefaultListModel<>();
		suggestionsList = new JList<>(suggestionsListModel);
		suggestionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		suggestionsList.setFocusable(false);
		suggestionsList.setFont(new Font("Montserrat Regular", Font.BOLD, 11));

		suggestionScrollPane = new JScrollPane(suggestionsList);
		// Size set dynamically later
		suggestionsPopup.add(suggestionScrollPane);

		// --- Add Listeners ---
		addListeners();
	}

	/**
	 * Attaches necessary listeners to components.
	 */
	private void addListeners() {
		itemSearchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				triggerSearchUpdate();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				triggerSearchUpdate();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				triggerSearchUpdate();
			}
		});

		itemSearchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				Timer timer = new Timer(200, ae -> {
					boolean popupHasFocus = false;
					if (suggestionsPopup.isVisible()) {
						Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
						if (focusOwner != null && SwingUtilities.isDescendingFrom(focusOwner, suggestionsPopup)) {
							popupHasFocus = true;
						}
					}
					if (!popupHasFocus && !itemSearchField.isFocusOwner()) {
						suggestionsPopup.setVisible(false);
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		});

		suggestionsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
					int index = suggestionsList.locationToIndex(e.getPoint());
					if (index >= 0 && index < suggestionsListModel.getSize()) {
						ItemData selected = suggestionsListModel.getElementAt(index);
						System.out.println(
								"Suggestion clicked: " + selected.itemName() + " (" + selected.packagingName() + ")");
						itemSelectedListener.itemSelected(selected); // Notify listener
						itemSearchField.setText("");
						suggestionsPopup.setVisible(false);
						// itemSearchField.requestFocusInWindow(); // Maybe not needed
					}
				}
			}
		});

		itemSearchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (suggestionsPopup.isVisible()) {
					int currentSelection = suggestionsList.getSelectedIndex();
					int listSize = suggestionsListModel.getSize();
					switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						if (listSize > 0) {
							suggestionsList.setSelectedIndex(Math.min(currentSelection + 1, listSize - 1));
							suggestionsList.ensureIndexIsVisible(suggestionsList.getSelectedIndex());
							e.consume();
						}
						break;
					case KeyEvent.VK_UP:
						if (listSize > 0) {
							suggestionsList.setSelectedIndex(Math.max(currentSelection - 1, 0));
							suggestionsList.ensureIndexIsVisible(suggestionsList.getSelectedIndex());
							e.consume();
						}
						break;
					case KeyEvent.VK_ENTER:
						if (currentSelection != -1) {
							ItemData selected = suggestionsListModel.getElementAt(currentSelection);
							itemSelectedListener.itemSelected(selected);
							itemSearchField.setText("");
							suggestionsPopup.setVisible(false);
							e.consume();
						}
						break;
					case KeyEvent.VK_ESCAPE:
						suggestionsPopup.setVisible(false);
						e.consume();
						break;
					}
				}
			}
		});
	}

	/**
	 * Public method to clear the search field text.
	 */
	public void clearSearchField() {
		if (itemSearchField != null) {
			itemSearchField.setText("");
		}
		if (suggestionsPopup != null && suggestionsPopup.isVisible()) {
			suggestionsPopup.setVisible(false);
		}
	}

	// --- Helper: Style Icon Button / Label ---
	private void styleIconButtonLabel(JLabel label) {
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.setToolTipText("Search");
		label.setBorder(new EmptyBorder(0, 0, 0, 3));
	}

	/**
	 * Called by the DocumentListener. Manages background search execution.
	 */
	private void triggerSearchUpdate() {
		if (currentSearchWorker != null && !currentSearchWorker.isDone()) {
			currentSearchWorker.cancel(true);
			currentSearchWorker = null;
		}
		final String searchText = itemSearchField.getText().trim();
		Timer searchTimer = new Timer(300, (ActionEvent e) -> {
			if (searchText.length() >= SEARCH_TRIGGER_LENGTH && searchText.equals(itemSearchField.getText().trim())) {
				System.out.println("Starting search worker for: " + searchText);
				currentSearchWorker = new SearchWorker(searchText);
				currentSearchWorker.execute();
			} else {
				if (suggestionsPopup != null) {
					suggestionsPopup.setVisible(false);
				}
			}
		});
		searchTimer.setRepeats(false);
		searchTimer.start();
	}

} // End CheckoutSearchComponent