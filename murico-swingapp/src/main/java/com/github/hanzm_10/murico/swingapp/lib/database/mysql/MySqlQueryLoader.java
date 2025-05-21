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
package com.github.hanzm_10.murico.swingapp.lib.database.mysql;

import com.github.hanzm_10.murico.swingapp.constants.Misc;
import com.github.hanzm_10.murico.swingapp.lib.cache.LRU;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader;

/**
 * This class is responsible for loading SQL queries from files for MySQL
 * database. <br>
 * <br>
 * It extends the AbstractSqlQueryLoader class and implements the
 * getDatabaseName method. The queries are loaded from the resources directory
 * under the sql/mysql/ directory. <br>
 * <br>
 *
 * @see AbstractSqlQueryLoader
 */
public class MySqlQueryLoader extends AbstractSqlQueryLoader {
	private static MySqlQueryLoader instance;

	public static synchronized MySqlQueryLoader getInstance() {
		if (instance == null) {
			instance = new MySqlQueryLoader();
		}

		return instance;
	}

	private MySqlQueryLoader() {
		super();

		queryCache = new LRU<>(Misc.DEFAULT_MAX_STRING_CACHE_SIZE);
	}

	private MySqlQueryLoader(int maxCacheSize) {
		super();

		if (maxCacheSize <= 0) {
			throw new IllegalArgumentException("Cache size must be greater than 0");
		}

		queryCache = new LRU<>(maxCacheSize);
	}

	@Override
	public String getDatabaseName() {
		return "mysql";
	}

}
