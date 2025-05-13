package com.github.hanzm_10.murico.swingapp.lib.database.entity.sales;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public record TotalOfSales(@NotNull double totalRevenue, @NotNull double totalGross, @NotNull double totalNetSales,
		@NotNull @Range(from = 0, to = Integer.MAX_VALUE) int totalItemsSold) {

}
