package com.github.hanzm_10.murico.swingapp.lib.database.entity.item;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an item stock in the inventory system.
 * <p>
 * This class contains information about the item stock, including its ID,
 * packaging type, supplier name, item name, stock quantity, unit price, and
 * minimum quantity.
 * </p>
 *
 * @param _itemStockId    The unique identifier for the item stock.
 * @param _itemId         The unique identifier for the item.
 * @param packagingType   The type of packaging for the item.
 * @param supplierName    The name of the supplier for the item.
 * @param itemName        The name of the item.
 * @param stockQuantity   The quantity of the item in stock.
 * @param unitPrice       The unit price of the item in PHP.
 * @param minimumQuantity The minimum quantity of the item that should be in
 *                        stock.
 */
public record ItemStock(@NotNull int _itemStockId, @NotNull int _itemId, @NotNull String categoryType,
		@NotNull String packagingType, @NotNull String supplierName, @NotNull String itemName,
		@NotNull int stockQuantity, @NotNull BigDecimal unitPrice, @NotNull int minimumQuantity) {

	public static String[] getColumnNames() {
		return new String[] { "Item Stock ID", "Item ID", "Category Type", "Packaging Type", "Supplier Name",
				"Item Name", "Unit Price (PHP)", "Stock Quantity", "Minimum Quantity" };
	}
}
