package com.github.hanzm_10.murico.swingapp.lib.database.entity.item;

import org.jetbrains.annotations.NotNull;

public record ItemQuantityPerPackaging(@NotNull int _itemId, @NotNull String itemName,
		@NotNull PackagingQuantity[] packagingQuantities) {

	public static record PackagingQuantity(@NotNull int _packagingId, @NotNull String packagingName,
			@NotNull int quantity) {
	}

}
