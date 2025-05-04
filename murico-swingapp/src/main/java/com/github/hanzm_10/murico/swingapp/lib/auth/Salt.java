package com.github.hanzm_10.murico.swingapp.lib.auth;

import java.security.SecureRandom;
import java.util.Base64;

import org.jetbrains.annotations.NotNull;

public class Salt {
	public static Salt fromBase64(@NotNull final String encodedSalt) {
		return new Salt(Base64.getDecoder().decode(encodedSalt));
	}

	public static byte[] generateSalt() {
		var secureRandom = new SecureRandom();
		var salt = new byte[16];

		secureRandom.nextBytes(salt);

		return salt;
	}

	public static String toBase64(Salt salt) {
		return Base64.getEncoder().encodeToString(salt.getValue());
	}

	private byte[] value;

	public Salt() {
		value = generateSalt();
	}

	public Salt(byte[] salt) {
		value = salt;
	}

	public byte[] getValue() {
		return value;
	}

	public String toBase64() {
		return Salt.toBase64(this);
	}
}
