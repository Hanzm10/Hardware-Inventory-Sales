package com.github.hanzm_10.murico.swingapp.scenes.home;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.CheckoutPanel;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.OrderHistoryPanel;
import com.github.hanzm_10.murico.swingapp.scenes.home.order_menu.TransactionHistoryPanel;

public class OrderMenuScene implements Scene {

	private static final String CARD_ORDER_HISTORY = "OrderHistory";
	private static final String CARD_TRANSACTION_HISTORY = "TransactionHistory";
	private static final String CARD_CHECKOUT = "Checkout";

	private JPanel view;

	private JPanel viewSwitchPanel;
	private CardLayout cardLayout;

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

		checkoutButton.addActionListener(this::showCheckout);

		orderHistoryButton.addActionListener(this::showOrderHistory);

		transactionHistoryButton.addActionListener(this::showTransactionHistory);

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

	@Override
	public boolean onDestroy() {
		if (checkoutPanel != null) {
			checkoutPanel.destroy();
		}
		if (orderHistoryPanel != null) {
			orderHistoryPanel.destroy();
		}
		if (transactionHistoryPanel != null) {
			transactionHistoryPanel.destroy();
		}
		return true;
	}

	private void showCard(String cardName) {
		cardLayout.show(viewSwitchPanel, cardName);

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
			if (checkoutPanel != null) {
				checkoutPanel.clearCheckoutState();
			}
			break;
		}
	}

	private void showCheckout(ActionEvent e) {
		showCard(CARD_CHECKOUT);
	}

	private void showOrderHistory(ActionEvent e) {
		showCard(CARD_ORDER_HISTORY);
	}

	private void showTransactionHistory(ActionEvent e) {
		showCard(CARD_TRANSACTION_HISTORY);
	}

}
