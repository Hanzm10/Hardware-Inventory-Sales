package com.github.hanzm_10.murico.swingapp.lib.database.entity.packaging;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record ItemPackaging(@NotNull int _packagingId, @NotNull Timestamp _createdAt, @NotNull String name,
		@NotNull String description) {

}
