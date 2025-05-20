package com.github.hanzm_10.murico.swingapp.lib.database.entity.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public record InventoryBreakdown(@Range(from = 0, to = Integer.MAX_VALUE) int _itemId, @NotNull String itemName,
		@NotNull String categoryType, @NotNull String packagingType,
		@Range(from = 0, to = Integer.MAX_VALUE) int initialItemQuantity,
		@Range(from = 0, to = Integer.MAX_VALUE) int amountOfItemsSold,
		@Range(from = 0, to = Integer.MAX_VALUE) int amountOfItemsRestocked,
		@Range(from = 0, to = Integer.MAX_VALUE) int currentItemQuantity, @NotNull InventoryBreakdownRemarks remarks) {

	public static enum InventoryBreakdownRemarks {
		CRITICAL, LOW, NORMAL, HIGH;

		public static InventoryBreakdownRemarks fromCurrAndMin(int current, int min) {
			if (current <= 0) {
				return CRITICAL;
			} else if (current <= min) {
				return LOW;
			} else if (current <= min * 2) {
				return NORMAL;
			} else {
				return HIGH;
			}
		}

		public static InventoryBreakdownRemarks fromString(String str) {
			return switch (str.toUpperCase()) {
			case "CRITICAL" -> CRITICAL;
			case "LOW" -> LOW;
			case "NORMAL" -> NORMAL;
			case "HIGH" -> HIGH;
			default -> throw new IllegalArgumentException("Unknown InventoryBreakdownRemarks: " + str);
			};
		}

		public static String toString(InventoryBreakdownRemarks remarks) {
			return switch (remarks) {
			case CRITICAL -> "Critical";
			case LOW -> "Low";
			case NORMAL -> "Normal";
			case HIGH -> "High";
			};
		}
	}

	public static String[] getColumnNames() {
		return new String[] { "Item ID", "Item Name", "Category Type", "Packaging Type", "Initial Item Quantity",
				"Sold", "Restock", "Current Stock", "Remarks" };
	}
}
