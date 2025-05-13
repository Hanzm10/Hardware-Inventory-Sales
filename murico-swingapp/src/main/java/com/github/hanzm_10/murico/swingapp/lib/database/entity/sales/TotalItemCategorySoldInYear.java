package com.github.hanzm_10.murico.swingapp.lib.database.entity.sales;

import org.jetbrains.annotations.NotNull;

public record TotalItemCategorySoldInYear(@NotNull String year, @NotNull String itemCategory,
		@NotNull int totalQuanitySold) {

}
