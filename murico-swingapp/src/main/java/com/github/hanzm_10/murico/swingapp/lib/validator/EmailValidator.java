package com.github.hanzm_10.murico.swingapp.lib.validator;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

public final class EmailValidator {
	/**
     * From ASP.Net's codebase
	 */
	public static @NotNull final Pattern EMAIL_REGEX = Pattern
			.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

	public static boolean isEmailValid(@NotNull final String email, @NotNull final Pattern regex) {
		return regex.matcher(email).find();
	}
}

