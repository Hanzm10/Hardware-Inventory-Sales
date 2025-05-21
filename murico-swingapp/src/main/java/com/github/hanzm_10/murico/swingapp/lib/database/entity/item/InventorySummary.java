package com.github.hanzm_10.murico.swingapp.lib.database.entity.item;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public record InventorySummary(@Range(from = 0, to = Integer.MAX_VALUE) int totalInventoryValue,
		@Range(from = 0, to = Integer.MAX_VALUE) int totalItemsInStock,
		@Range(from = 0, to = Integer.MAX_VALUE) int totalItemsBelowCriticalLevel,
		@NotNull BigDecimal averageStockPerItem) {

}
