package com.github.hanzm_10.murico.swingapp.lib.database.entity.supplier;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record Supplier(@NotNull int _supplierId, @NotNull Timestamp _createdAt, @NotNull Timestamp updatedAt,
		@NotNull String name, @NotNull String email, String street, String city, String postalCode, String country) {

}
