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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestMuricoCrypt {
	@Test
	@DisplayName("Tests MuricoCrypt produces different hashes for the same input with different salts.")
	void testPasswordHashing() throws NoSuchAlgorithmException, InvalidKeySpecException {
		var pass = "123456".toCharArray();
		var salt = new Salt();
		var salt2 = new Salt();
		var hasher = new MuricoCrypt();

		assertNotEquals(hasher.hash(pass, salt).hashedString(), hasher.hash(pass, salt2).hashedString());
		assertNotEquals(hasher.hash(pass).hashedString(), hasher.hash(pass).hashedString());
	}

	@Test
	@DisplayName("Tests MuricoCrypt produces different hashes for different inputs with the same salts")
	void testPasswordHashingDifferences() throws NoSuchAlgorithmException, InvalidKeySpecException {
		var salt = new Salt();
		var pass = "123456".toCharArray();
		var pass2 = "654321".toCharArray();
		var hasher = new MuricoCrypt();
		assertNotEquals(hasher.hash(pass, salt).hashedString(), hasher.hash(pass2, salt).hashedString());
	}

	@Test
	@DisplayName("Tests HashedPassword produces the same hash for the same input and salt")
	void testPasswordHashingSimilarity() throws NoSuchAlgorithmException, InvalidKeySpecException {
		var salt = new Salt();
		var pass = "123456".toCharArray();
		var hasher = new MuricoCrypt();
		assertEquals(hasher.hash(pass, salt).hashedString(), hasher.hash(pass, salt).hashedString());
	}
}
