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
package com.github.hanzm_10.murico.swingapp.lib.exceptions;

public enum MuricoErrorCodes {
	INVALID_CREDENTIALS(1001, "Invalid credentials. Please check your username and password."),
	FORBIDDEN_ACCESS(1002, "You do not have permission to access this resource."),
	DATABASE_CONNECTION_FAILED(2001, "Unable to connect to the database. Please check your configuration."),
	DATABASE_FAILED_INSERT(2002, "Unable to perform the speified action."),
	DATABASE_OPERATION_FAILED(2003, "Something went wrong while querying the database"),
	UNKNOWN_ERROR(9999, "An unknown error has occurred."), SCENE_NOT_FOUND(3001, "The requested scene was not found."),
	INVALID_INPUT(4001, "Invalid input provided. Please check your data."),
	NETWORK_ERROR(5001, "Network error occurred. Please check your connection."),
	FILE_NOT_FOUND(6001, "The specified file was not found."),
	UNSUPPORTED_OPERATION(7001, "This operation is not supported."),
	TIMEOUT_ERROR(8001, "The operation timed out. Please try again later."),
	UNREACHABLE_ERROR(9998, "An event that shouldn't have happened occured.");

	private final int code;
	private final String defaultMessage;

	MuricoErrorCodes(int code, String defaultMessage) {
		this.code = code;
		this.defaultMessage = defaultMessage;
	}

	public int getCode() {
		return code;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	@Override
	public String toString() {
		return code + " - " + defaultMessage;
	}
}
