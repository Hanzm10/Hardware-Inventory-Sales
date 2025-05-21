package com.github.hanzm_10.murico.swingapp.lib.database.entity.sales;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

public record YearlyGross(@NotNull String year, @NotNull BigDecimal gross) {

}
