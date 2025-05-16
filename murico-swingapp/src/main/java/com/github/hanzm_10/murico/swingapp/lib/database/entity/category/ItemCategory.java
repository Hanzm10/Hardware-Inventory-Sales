package com.github.hanzm_10.murico.swingapp.lib.database.entity.category;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record ItemCategory(@NotNull int _itemCategoryId, @NotNull Timestamp _createdAt, @NotNull String name) {

}
