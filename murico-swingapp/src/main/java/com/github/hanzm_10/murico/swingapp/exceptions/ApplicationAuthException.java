/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.exceptions;

/**
 * Custom exception class for handling authentication-related errors in the
 * application. This class extends RuntimeException and provides specific error
 * codes for different authentication scenarios.
 */
public class ApplicationAuthException extends RuntimeException {
	public enum ErrorCode {
		INVALID_PASSWORD("Invalid password"), INVALID_USERNAME("Invalid username"), USER_NOT_FOUND(
				"User not found"), USERNAME_ALREADY_EXISTS(
						"Username already exists"), FORBIDDEN("Unauthorized to perform this action");

		private String message;

		ErrorCode(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	private ErrorCode errorCode;

	public ApplicationAuthException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ApplicationAuthException(String message) {
		super(message);
	}

	public ApplicationAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationAuthException(Throwable cause) {
		super(cause);
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
