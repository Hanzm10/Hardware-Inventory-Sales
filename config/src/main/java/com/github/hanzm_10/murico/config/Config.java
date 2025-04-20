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
package com.github.hanzm_10.murico.config;

import java.util.logging.Logger;

import com.github.hanzm_10.murico.utils.MuricoLogUtils;

public abstract class Config {
	protected static final Logger LOGGER = MuricoLogUtils.getLogger(Config.class);

	/**
	 * The configuration's file extension. This is used to determine the file type.
	 */
	public abstract String getFileExtension();

	/**
	 * The configuration's file name. This is used to determine the file name. <br>
	 * <br>
	 * The returned file name is the class name of the configuration class, followed
	 * by a dot and the file extension. For example, if the class name is
	 * "GlobalConfig" and the file extension is "json", the returned file name will
	 * be "GlobalConfig.json".
	 *
	 * @return String
	 */
	public String getFileName() {
		return getClass().getSimpleName() + "." + getFileExtension();
	}

	/**
	 * The configuration's file path. This is used to determine the file path.
	 *
	 * @return String
	 */
	public abstract String getFilePath();

	/** The header comment for the configuration file. */
	public abstract String getHeaderComment();
}
