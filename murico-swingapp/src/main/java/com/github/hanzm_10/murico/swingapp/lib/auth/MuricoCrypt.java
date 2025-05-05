/**
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.lib.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.CharUtils;

public class MuricoCrypt {
	public final record HashedStringWithSalt(@NotNull byte[] hashedString, @NotNull Salt salt) {
		public boolean equalsHashedString(@NotNull final HashedStringWithSalt hashedStringWithSalt) {
			return CharUtils.constantTimeEquals(hashedStringWithSalt.hashedString, this.hashedString);
		}

		public boolean equalsSalt(@NotNull final HashedStringWithSalt hashedStringWithSalt) {
			return CharUtils.constantTimeEquals(hashedStringWithSalt.salt.getValue(), this.salt.getValue());
		}

		/**
		 * Compares values
		 *
		 * @param hashedStringWithSalt
		 * @return
		 */
		public boolean equals(@NotNull final HashedStringWithSalt hashedStringWithSalt) {
			return equalsHashedString(hashedStringWithSalt) && equalsSalt(hashedStringWithSalt);
		}

		/**
		 * Essentially removes the information of this hashed string
		 */
		public void clearHashedStringBytes() {
			for (int i = 0; i < hashedString().length; ++i) {
				hashedString()[i] = 0;
			}
		}
	}

	public static final Logger LOGGER = MuricoLogger.getLogger(MuricoCrypt.class);

	/** Default iteration count (work factor) for the PBKDF2 algorithm. */
	public static final int DEFAULT_STRENGTH = 65536;

	/** Length of the derived key in bits. */
	public static final int KEY_LENGTH = 256;

	public static final String ALGORITHM = "PBKDF2WithHmacSHA256";

	public HashedStringWithSalt hash(@NotNull final char[] password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return hash(password, new Salt());
	}

	public HashedStringWithSalt hash(@NotNull final char[] password, @NotNull final Salt salt) {
		try {
			var spec = new PBEKeySpec(password, salt.getValue(), DEFAULT_STRENGTH, KEY_LENGTH);
			var hash = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
			spec.clearPassword();

			return new HashedStringWithSalt(hash, salt);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.log(Level.SEVERE, "Cannot hash string", e);
		}

		return null;
	}
}
