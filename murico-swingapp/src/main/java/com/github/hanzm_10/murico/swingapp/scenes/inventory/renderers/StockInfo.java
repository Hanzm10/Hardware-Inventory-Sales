package com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers;

/**
 * Simple data holder for stock quantity and minimum quantity.
 * Used by StockLevelRenderer.
 */
public class StockInfo {
    private final int quantity;
    private final int minimumQuantity;

    public StockInfo(int quantity, int minimumQuantity) {
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
    }

    public int getQuantity() { return quantity; }
    public int getMinimumQuantity() { return minimumQuantity; }

    @Override
    public String toString() {
        // Basic text representation, renderer provides more detail
        return quantity + " unit(s)";
    }
}