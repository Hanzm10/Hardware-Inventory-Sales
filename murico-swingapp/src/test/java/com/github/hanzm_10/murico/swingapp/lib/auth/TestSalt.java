package com.github.hanzm_10.murico.swingapp.lib.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestSalt {
	@Test
	@DisplayName("Test encoded salt is decoded the same")
	void testSaltEncoding() {
		var salt = new Salt();
		var decodedEncoded = Salt.fromBase64(salt.toBase64());
		assertTrue(Arrays.equals(salt.getValue(), decodedEncoded.getValue()));
	}
}
