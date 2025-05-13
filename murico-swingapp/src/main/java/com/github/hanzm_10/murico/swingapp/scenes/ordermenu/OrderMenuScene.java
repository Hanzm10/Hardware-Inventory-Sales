package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
// --- Imports ---
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
// --- End Imports ---
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.CheckoutPanel;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.OrderHistoryPanel;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.TransactionHistoryPanel;

public class OrderMenuScene extends JPanel implements Scene {

	// Constants
	private static final String CARD_ORDER_HISTORY = "OrderHistory";
	private static final String CARD_TRANSACTION_HISTORY = "TransactionHistory";
	private static final String CARD_CHECKOUT = "Checkout";
	private static final Color NAV_BUTTON_HOVER_COLOR = new Color(210, 230, 255);

	// UI Components
	private JPanel viewSwitchPanel;
	private CardLayout cardLayout;

	// Panel instances
	private CheckoutPanel checkoutPanel;
	private OrderHistoryPanel orderHistoryPanel;
	private TransactionHistoryPanel transactionHistoryPanel;

	private boolean uiInitialized = false;
	private int currentBranchId = 1; // TODO: Make dynamic

	public OrderMenuScene() {
		setLayout(new BorderLayout(0, 0));
	}

	private void addHoverEffect(JButton button) {
		/* ... */ Color originalBg = button.getBackground();
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(NAV_BUTTON_HOVER_COLOR);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(originalBg);
			}
		});
	}

	// --- Scene Interface Methods ---
	@Override
	public String getSceneName() {
		return "ordermenu";
	}

	@Override
	public JPanel getSceneView() {
		return this;
	}

	private void initializeOrderMenuUI() {
		System.out.println(getSceneName() + ": Initializing Main UI Structure...");

		// --- Top Section (Title, Nav Buttons) ---
		JPanel topSectionPanel = new JPanel(new BorderLayout(0, 5));
		// ... (Top bar with title and notification icon) ...
		JPanel topBar = new JPanel(new BorderLayout());
		topBar.setBorder(new EmptyBorder(5, 10, 5, 10));
		JLabel titleLabel = new JLabel("ORDER MENU");
		titleLabel.setFont(new Font("Montserrat ExtraBold", Font.BOLD, 24));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel notificationLabel = new JLabel("\uD83D\uDD14");
		notificationLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
		topBar.add(titleLabel, BorderLayout.CENTER);
		topBar.add(notificationLabel, BorderLayout.EAST);
		topSectionPanel.add(topBar, BorderLayout.NORTH);

		// --- Navigation Button Bar ---
		JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		navigationPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

		JButton checkoutButton = new JButton("Checkout");
		JButton orderHistoryButton = new JButton("Order History");
		JButton transactionHistoryButton = new JButton("Transaction History");

		styleNavButton(checkoutButton);
		addHoverEffect(checkoutButton);
		checkoutButton.addActionListener(e -> showCard(CARD_CHECKOUT)); // Use helper

		styleNavButton(orderHistoryButton);
		addHoverEffect(orderHistoryButton);
		orderHistoryButton.addActionListener(e -> showCard(CARD_ORDER_HISTORY)); // Use helper

		styleNavButton(transactionHistoryButton);
		addHoverEffect(transactionHistoryButton);
		transactionHistoryButton.addActionListener(e -> showCard(CARD_TRANSACTION_HISTORY)); // Use helper

		navigationPanel.add(orderHistoryButton);
		navigationPanel.add(transactionHistoryButton);
		navigationPanel.add(checkoutButton);

		topSectionPanel.add(navigationPanel, BorderLayout.CENTER);
		this.add(topSectionPanel, BorderLayout.NORTH);

		// --- Center Panel using CardLayout ---
		cardLayout = new CardLayout();
		viewSwitchPanel = new JPanel(cardLayout); // Just the container
		this.add(viewSwitchPanel, BorderLayout.CENTER);

		// Sub-panels are created and added in onCreate
	}

	@Override
	public void onCreate() {
		System.out.println(getSceneName() + ": onCreate");
		if (!uiInitialized) {
			initializeOrderMenuUI(); // Build the main structure
			// Create instances of the sub-panels
			checkoutPanel = new CheckoutPanel(currentBranchId); // Keep branchId HERE if CheckoutPanel still needs it
																// for display/logging
			orderHistoryPanel = new OrderHistoryPanel(); // Use no-arg constructor
			transactionHistoryPanel = new TransactionHistoryPanel(); // Use no-arg constructor
			// --- End Fix ---

			// Add panels to the CardLayout container
			viewSwitchPanel.add(checkoutPanel, CARD_CHECKOUT);
			viewSwitchPanel.add(orderHistoryPanel, CARD_ORDER_HISTORY);
			viewSwitchPanel.add(transactionHistoryPanel, CARD_TRANSACTION_HISTORY);

			uiInitialized = true;
		}
	}

	@Override
	public boolean onDestroy() {
		removeAll();
		uiInitialized = false;
		/* Call destroy on sub-panels? */ return true;
	}
	// --- End Scene Implementation ---

	@Override
	public void onHide() {
		/* ... */ }

	@Override
	public void onShow() {
		System.out.println(getSceneName() + ": onShow");
		if (!uiInitialized) {
			onCreate(); // Ensure UI and panels are created
		}
		// Show default view and potentially load its data
		cardLayout.show(viewSwitchPanel, CARD_CHECKOUT);
		// checkoutPanel.loadData(); // Add loadData method to CheckoutPanel if needed
		// for reset
	}

	/** Helper to switch cards and potentially trigger data loading */
	private void showCard(String cardName) {
		System.out.println("Switching to card: " + cardName);
		cardLayout.show(viewSwitchPanel, cardName);

		// Trigger data loading for the specific panel when shown
		switch (cardName) {
		case CARD_ORDER_HISTORY:
			if (orderHistoryPanel != null) {
				orderHistoryPanel.loadData();
			}
			break;
		case CARD_TRANSACTION_HISTORY:
			if (transactionHistoryPanel != null) {
				transactionHistoryPanel.loadData();
			}
			break;
		case CARD_CHECKOUT:
			// Checkout panel usually loads interactively, maybe clear state?
			// if (checkoutPanel != null) checkoutPanel.clearCheckoutState();
			break;
		}
	}

	// --- Styling Helpers (No changes) ---
	private void styleNavButton(JButton button) {
		/* ... */ button.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setBackground(UIManager.getColor("Button.background"));
		button.setForeground(UIManager.getColor("Button.foreground"));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

} // --- End OrderMenuScene Class ---