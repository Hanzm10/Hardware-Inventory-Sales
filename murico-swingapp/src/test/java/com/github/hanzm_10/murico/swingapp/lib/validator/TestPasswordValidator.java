package com.github.hanzm_10.murico.swingapp.lib.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestPasswordValidator {
	@Test
	@DisplayName("Test strong password pattern")
	void testStrongPasswordMathes() {
		var weak = "12345";
		var weak2 = "abcdef";
		var weak3 = "ABCDEF";
		var weak4 = "@$#$#%";
		var med = "1adadasd24234";
		var med2 = "SDFDFDR344523FD";
		var med3 = "@$#43423343$#@$";
		var med4 = "423432FfsdfsfsdfsFfsd";
		var med5 = "fdfds454$%%$#";
		var med6 = "FDSFSD42342$@#$#@";

		var strong = "123@heLoL";
		var strongweak = "1@Ll";

		assertFalse(PasswordValidator.isPasswordValid(weak, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(weak2, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(weak3, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(weak4, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(med, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(med2, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(med3, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(med4, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(med5, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(med6, PasswordValidator.STRONG_PASSWORD));
		assertFalse(PasswordValidator.isPasswordValid(strongweak, PasswordValidator.STRONG_PASSWORD));

		assertTrue(PasswordValidator.isPasswordValid(strong, PasswordValidator.STRONG_PASSWORD));
	}
}
