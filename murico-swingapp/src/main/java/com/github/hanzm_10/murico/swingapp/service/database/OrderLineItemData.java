package com.github.hanzm_10.murico.swingapp.service.database; // Or a dto package

import java.math.BigDecimal;

/**
 * Data Transfer Object holding information for finalizing a single order line item.
 */
public record OrderLineItemData(
	    int itemStockId,    // The specific _item_stock_id sold
	    int itemId,         // The base _item_id (useful for logging/errors) <-- ADDED
	    String itemName,    // The item name (useful for logging/errors) <-- ADDED
	    int quantity,       // The quantity sold
	    BigDecimal priceAtSale // The price (SRP) at the time of sale
	) {}