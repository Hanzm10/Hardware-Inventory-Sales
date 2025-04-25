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
package com.github.hanzm_10.murico.swingapp.config;

import java.util.logging.Logger;

import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public final class ApplicationConfig {
	public class FileSystemApplicationConfig extends AbstractFileSystemConfigProperties {
		private FileSystemApplicationConfig() {
			super();
		}

		@Override
		public String getFileName() {
			return ApplicationConfig.class.getSimpleName();
		}
	}

	public class ReadOnlyApplicationConfig extends AbstractResourceConfigProperties {
		private ReadOnlyApplicationConfig() {
			super();
		}

		@Override
		public String getFileName() {
			return ApplicationConfig.class.getSimpleName();
		}

		@Override
		public Class<? extends AbstractResourceConfig> getResourceClass() {
			return ReadOnlyApplicationConfig.class;
		}
	}

	public static final Logger LOGGER = MuricoLogger.getLogger(ApplicationConfig.class);

	private static ApplicationConfig instance;

	public static synchronized ApplicationConfig getInstance() {
		if (instance == null) {
			return new ApplicationConfig();
		}

		return instance;
	}

	private final FileSystemApplicationConfig config;

	private final ReadOnlyApplicationConfig defaultConfig;

	private ApplicationConfig() {
		super();

		config = new FileSystemApplicationConfig();
		defaultConfig = new ReadOnlyApplicationConfig();
	}

	public FileSystemApplicationConfig getConfig() {
		return config;
	}

	public ReadOnlyApplicationConfig getDefaultConfig() {
		return defaultConfig;
	}

	public String getProperty(String key) {
		var value = config.getProperty(key);

		if (value == null) {
			value = defaultConfig.getProperty(key);
		}

		return value;
	}
}
