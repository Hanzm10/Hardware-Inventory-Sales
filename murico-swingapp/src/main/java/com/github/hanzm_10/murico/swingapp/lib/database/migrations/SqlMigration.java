package com.github.hanzm_10.murico.swingapp.lib.database.migrations;

import org.jetbrains.annotations.NotNull;

public record SqlMigration(@NotNull String fileName, @NotNull String query) {
	public static record ParsedSqlMigration(@NotNull int versionNumber, @NotNull String migrationName,
			@NotNull String query) {
	}
}
