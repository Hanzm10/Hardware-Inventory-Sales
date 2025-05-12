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
package com.github.hanzm_10.murico.swingapp.lib.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MuricoLogger {
	private static final Logger PARENT_LOGGER = Logger.getLogger("com.github.hanzm_10.murico");
	private static final Handler HANDLER = new MuricoLogHandler();

	static {
		HANDLER.setFormatter(new MuricoLogFormatter());
		PARENT_LOGGER.setUseParentHandlers(false);
		PARENT_LOGGER.addHandler(HANDLER);
	}

	public static Logger getDetachedLogger(final Class<?> c) {
		var logger = getLogger(c);
		logger.setUseParentHandlers(false);
		logger.addHandler(HANDLER);

		return logger;
	}

	public static Level getLevel() {
		return PARENT_LOGGER.getLevel();
	}

	public static Logger getLogger(final Class<?> c) {
		var logger = Logger.getLogger(c.getName());
		logger.setUseParentHandlers(true);
		logger.setParent(PARENT_LOGGER);

		return logger;
	}

	public static <T> T log(final T obj) {
		PARENT_LOGGER.info(String.valueOf(obj));
		return obj;
	}

	public static void setLevel(final Level level) {
		PARENT_LOGGER.setLevel(level);
		HANDLER.setLevel(level);
	}
}
