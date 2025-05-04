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
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.jetbrains.annotations.NotNull;

public class MuricoCrypt {
	public final record HashedStringWithSalt(@NotNull String hashedString, @NotNull Salt salt) {
		public boolean equalsHashedString(@NotNull final HashedStringWithSalt hashedStringWithSalt) {
			return hashedStringWithSalt.hashedString().equals(this.hashedString);
		}

		public boolean equalsSalt(@NotNull final HashedStringWithSalt hashedStringWithSalt) {
			return hashedStringWithSalt.salt.equals(this.salt);
		}

		public boolean equals(@NotNull final HashedStringWithSalt hashedStringWithSalt) {
			return equals(hashedStringWithSalt) && equalsSalt(hashedStringWithSalt);
		}
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
