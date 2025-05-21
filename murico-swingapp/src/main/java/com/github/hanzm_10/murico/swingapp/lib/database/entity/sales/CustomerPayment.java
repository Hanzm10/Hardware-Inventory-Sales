package com.github.hanzm_10.murico.swingapp.lib.database.entity.sales;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

/**
 * CustomerPayment.java
 *
 * @param _customerPaymentId
 * @param _customerOrderId
 * @param _createdAt
 * @param paymentMethod
 * @param amountPhp
 */
public record CustomerPayment(@NotNull int _customerPaymentId, @NotNull int _customerOrderId,
		@NotNull Timestamp _createdAt, @NotNull String paymentMethod, @NotNull double amountPhp) {
	public Object[] toObjectArray() {
		return new Object[] { _customerPaymentId, _customerOrderId,
				_createdAt.toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "/"), paymentMethod,
				amountPhp };
	}

	public static String[] getColumnNames() {
		return Arrays.stream(CustomerPayment.class.getRecordComponents()).map(recordComponent -> {
			var name = recordComponent.getName();

			if (name.equals("_customerPaymentId")) {
				return "ID";
			} else if (name.equals("_customerOrderId")) {
				return "Order ID";
			} else if (name.equals("_createdAt")) {
				return "Date";
			} else if (name.equals("paymentMethod")) {
				return "Payment Method";
			} else if (name.equals("amountPhp")) {
				return "Amount (PHP)";
			}

			return name.substring(1, 2).toUpperCase() + name.substring(2);
		}).toArray(String[]::new);

	}
}
