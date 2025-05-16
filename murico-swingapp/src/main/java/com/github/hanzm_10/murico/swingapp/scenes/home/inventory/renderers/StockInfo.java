package com.github.hanzm_10.murico.swingapp.scenes.home.inventory.renderers;

public class StockInfo {
    private final int itemId; // Added
    private final int quantity;
    private final int minimumQuantity;

    public StockInfo(int itemId, int quantity, int minimumQuantity) {
        this.itemId = itemId; // Added
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
    }

    public int getItemId() { return itemId; } // Added
    public int getQuantity() { return quantity; }
    public int getMinimumQuantity() { return minimumQuantity; }

    @Override
    public String toString() {
        return quantity + " unit(s)";
    }
}