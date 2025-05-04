package com.github.hanzm_10.murico.swingapp.lib.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.jetbrains.annotations.NotNull;

public class MuricoCrypt {
	public final record HashedStringWithSalt(@NotNull String hashedString, @NotNull Salt salt) {
	}

	/** Default iteration count (work factor) for the PBKDF2 algorithm. */
	public static final int DEFAULT_STRENGTH = 65536;
	/** Length of the derived key in bits. */
	public static final int KEY_LENGTH = 256;
	public static final String ALGORITHM = "PBKDF2WithHmacSHA256";

	public HashedStringWithSalt hash(@NotNull final String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return hash(password, new Salt());
	}

	public HashedStringWithSalt hash(@NotNull final String password, @NotNull final Salt salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		var spec = new PBEKeySpec(password.toCharArray(), salt.getValue(), DEFAULT_STRENGTH, KEY_LENGTH);
		var hash = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();

		return new HashedStringWithSalt(Base64.getEncoder().encodeToString(hash), salt);
	}
}
