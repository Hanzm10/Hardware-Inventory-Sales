package com.github.hanzm_10.murico.swingapp.lib.database.entity.sales;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record Transaction(@NotNull Timestamp transactionDate, @NotNull String transactionType, @NotNull int refId,
		@NotNull String orderNumber, @NotNull BigDecimal amount, @NotNull String employeeHandler, @NotNull String party,
		@NotNull String status) {

}
