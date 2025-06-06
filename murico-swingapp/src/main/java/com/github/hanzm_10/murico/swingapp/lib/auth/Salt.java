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
