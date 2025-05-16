package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.CheckoutPanel;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.OrderHistoryPanel;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.TransactionHistoryPanel;

public class OrderMenuScene implements Scene {
	// Constants
	private static final String CARD_ORDER_HISTORY = "OrderHistory";
	private static final String CARD_TRANSACTION_HISTORY = "TransactionHistory";
	private static final String CARD_CHECKOUT = "Checkout";
	private static final Color NAV_BUTTON_HOVER_COLOR = new Color(210, 230, 255);

	private JPanel view;

	// UI Components
	private JPanel viewSwitchPanel;
	private CardLayout cardLayout;

	// Panel instances
	private CheckoutPanel checkoutPanel;
	private OrderHistoryPanel orderHistoryPanel;
	private TransactionHistoryPanel transactionHistoryPanel;

	private JPanel topSectionPanel;
	private JPanel navigationPanel;

	private JButton checkoutButton;
	private JButton orderHistoryButton;
	private JButton transactionHistoryButton;

	private void attachComponents() {
		viewSwitchPanel.add(checkoutPanel, CARD_CHECKOUT);
		viewSwitchPanel.add(orderHistoryPanel, CARD_ORDER_HISTORY);
		viewSwitchPanel.add(transactionHistoryPanel, CARD_TRANSACTION_HISTORY);

		checkoutButton.addActionListener(e -> showCard(CARD_CHECKOUT)); // Use helper

		orderHistoryButton.addActionListener(e -> showCard(CARD_ORDER_HISTORY)); // Use helper

		transactionHistoryButton.addActionListener(e -> showCard(CARD_TRANSACTION_HISTORY)); // Use helper

		navigationPanel.add(orderHistoryButton);
		navigationPanel.add(transactionHistoryButton);
		navigationPanel.add(checkoutButton);

		topSectionPanel.add(navigationPanel, BorderLayout.CENTER);

		view.add(topSectionPanel, BorderLayout.NORTH);
		view.add(viewSwitchPanel, BorderLayout.CENTER);
	}

	private void createComponents() {
		cardLayout = new CardLayout();
		viewSwitchPanel = new JPanel(cardLayout);

		topSectionPanel = new JPanel(new BorderLayout(0, 5));

		// --- Navigation Button Bar ---
		navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		navigationPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

		checkoutButton = new JButton("Checkout");
		orderHistoryButton = new JButton("Order History");
		transactionHistoryButton = new JButton("Transaction History");

		checkoutPanel = new CheckoutPanel(1); // Keep branchId HERE if CheckoutPanel still needs it

		orderHistoryPanel = new OrderHistoryPanel();
		transactionHistoryPanel = new TransactionHistoryPanel();
	}

	@Override
	public String getSceneName() {
		return "order menu";
	}

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

	@Override
	public void onCreate() {
		view.setLayout(new BorderLayout(0, 0));
		createComponents();
		attachComponents();
	}

	private void showCard(String cardName) {
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

}
