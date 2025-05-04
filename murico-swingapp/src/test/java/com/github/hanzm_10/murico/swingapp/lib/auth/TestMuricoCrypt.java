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
		var pass = "123456";
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
		var pass = "123456";
		var pass2 = "654321";
		var hasher = new MuricoCrypt();
		assertNotEquals(hasher.hash(pass, salt).hashedString(), hasher.hash(pass2, salt).hashedString());
	}

	@Test
	@DisplayName("Tests HashedPassword produces the same hash for the same input and salt")
	void testPasswordHashingSimilarity() throws NoSuchAlgorithmException, InvalidKeySpecException {
		var salt = new Salt();
		var pass = "123456";
		var hasher = new MuricoCrypt();
		assertEquals(hasher.hash(pass, salt).hashedString(), hasher.hash(pass, salt).hashedString());
	}
}
