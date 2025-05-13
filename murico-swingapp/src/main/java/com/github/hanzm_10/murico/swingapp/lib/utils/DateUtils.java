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
package com.github.hanzm_10.murico.swingapp.lib.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jetbrains.annotations.NotNull;

public final class DateUtils {

	public enum DateFormat {
		ISO("yyyy-MM-dd'T'HH:mm:ss.SSS"), ISO_WITHOUT_TIME("yyyy/MM/dd"), ISO_TIME("HH:mm:ss"),
		CUSTOM("yyyy-MM-dd HH:mm:ss");

		private final String format;

		DateFormat(String format) {
			this.format = format;
		}

		public String getFormat() {
			return format;
		}
	}

	public static final String formatDate(@NotNull final String date, @NotNull DateFormat dateFormat) {
		var formatter = DateTimeFormatter.ofPattern(dateFormat.format);
		var localDateTime = LocalDateTime.parse(date, formatter);
		return localDateTime.format(formatter);
	}

	public static final String getDateWithFormat() {
		return getDateWithFormat(DateFormat.ISO);
	}

	/**
	 * Returns the current date and time in the specified format.
	 *
	 * @param dateFormat The format to use.
	 * @return The current date and time in the specified format.
	 */
	public static final String getDateWithFormat(@NotNull DateFormat dateFormat) {
		var now = LocalDateTime.now();
		var formatter = DateTimeFormatter.ofPattern(dateFormat.format);
		var formattedDate = now.format(formatter);

		return formattedDate;
	}
}
