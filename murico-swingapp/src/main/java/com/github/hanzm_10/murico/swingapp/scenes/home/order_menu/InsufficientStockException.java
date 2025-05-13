package com.github.hanzm_10.murico.swingapp.scenes.home.order_menu; // Adjust if needed

/**
 * Custom exception thrown when finalizing an order fails due to insufficient stock.
 */
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}