package com.github.hanzm_10.murico.swingapp.lib.database.entity.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public record UserContact(@Range(from = 0, to = Integer.MAX_VALUE) int _userId, @NotNull String displayName,
		@NotNull String firstName, @NotNull String lastName, @NotNull String roles) {
	public static String[] getColumnNames() {
		return new String[] { "ID", "Display Name", "First Name", "Last Name", "Roles" };
	}
}
