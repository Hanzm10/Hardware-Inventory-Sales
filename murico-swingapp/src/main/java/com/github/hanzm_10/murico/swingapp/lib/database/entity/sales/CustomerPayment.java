package com.github.hanzm_10.murico.swingapp.lib.database.entity.sales;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record CustomerPayment(@NotNull int _customerPaymentId, @NotNull int _customerOrderId,
		@NotNull Timestamp _createdAt, @NotNull String paymentMethod, @NotNull double amountPhp) {

}
