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
package com.github.hanzm_10.murico.database.query;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.database.AbstractSQLFactoryDAO;
import com.github.hanzm_10.murico.utils.LogUtils;

/**
 * This class is responsible for caching SQL queries. It uses a simple in-memory
 * cache to store the queries.
 */
public class SQLQueryCache {
	public enum SQLQueryType {
		SELECT, INSERT, UPDATE, DELETE;

		@Override
		public String toString() {
			return switch (this) {
				case SELECT -> "select";
				case INSERT -> "insert";
				case UPDATE -> "update";
				case DELETE -> "delete";
			};
		}
	}

	private static final Logger LOGGER = LogUtils.getLogger(SQLQueryCache.class);
	private static final String MY_SQL_QUERY_DIRECTORY = "mysql/";
	private static final String SQL_QUERY_DIRECTORY = "/sql/";
	private static final String SQL_QUERY_FILE_EXTENSION = ".sql";

	private static SQLQueryCache instance;

	/**
	 * Returns the singleton instance of SQLQueryCache.
	 *
	 * @return the singleton instance of SQLQueryCache
	 */
	public static synchronized SQLQueryCache getInstance() {
		if (instance == null) {
			instance = new SQLQueryCache();
		}

		return instance;
	}

	private HashMap<String, String> queryCacheHashMap;

	private SQLQueryCache() {
		queryCacheHashMap = new HashMap<>();
	}

	/**
	 * Clears the SQL query cache. <br>
	 * <br>
	 * This method removes all cached SQL queries, allowing them to be reloaded from
	 * the file system if needed.
	 */
	public void clearCache() {
		queryCacheHashMap.clear();
		LOGGER.info("Clearing SQL query cache.");
	}

	/**
	 * Clears the SQL query cache for a specific file. <br>
	 * <br>
	 * This method removes the specified SQL query from the cache, allowing it to be
	 * reloaded from the file system if needed. The file name should not include the
	 * ".sql" extension.
	 *
	 * @param fileName
	 *            the name of the SQL file (without extension)
	 * @param factoryType
	 *            the type of SQL factory (e.g., MySQL, PostgreSQL)
	 * @param queryType
	 *            the type of SQL query (e.g., SELECT, INSERT, UPDATE, DELETE)
	 * @throws IllegalArgumentException
	 *             if the factory type is invalid
	 */
	public void clearCache(@NotNull String fileName, @NotNull int factoryType, @NotNull SQLQueryType queryType)
			throws IllegalArgumentException {
		var fileDir = switch (factoryType) {
			case AbstractSQLFactoryDAO.MYSQL -> MY_SQL_QUERY_DIRECTORY;
			default -> throw new IllegalArgumentException("Invalid factory type: " + factoryType);
		};
		var key = SQL_QUERY_DIRECTORY + fileDir + queryType.toString() + "/" + fileName;

		if (queryCacheHashMap.containsKey(key)) {
			queryCacheHashMap.remove(key);
			LOGGER.info("Clearing SQL query cache for: " + key);
		}
	}

	/**
	 * Retrieves an SQL query from the cache or loads it from a file if not already
	 * cached. <br>
	 * <br>
	 * This method constructs the file path based on the factory type and query
	 * type, then attempts to load the SQL query from the corresponding file. If the
	 * query is found in the cache, it is returned directly. Otherwise, the query is
	 * loaded from the file and cached for future use. <br>
	 * <br>
	 * The loader used is {@link Class#getResourceAsStream}, which loads the file
	 * from the classpath. The file name should not include the ".sql" extension.
	 *
	 * @param fileName
	 *            the name of the SQL file (without extension)
	 * @param factoryType
	 *            the type of SQL factory (e.g., MySQL, PostgreSQL)
	 * @param queryType
	 *            the type of SQL query (e.g., SELECT, INSERT, UPDATE, DELETE)
	 * @return the SQL query as a string
	 * @throws IllegalArgumentException
	 *             if the factory type is invalid
	 * @throws FileNotFoundException
	 *             if the SQL file is not found
	 */
	public String getQuery(@NotNull String fileName, @NotNull int factoryType, @NotNull SQLQueryType queryType)
			throws IllegalArgumentException, FileNotFoundException {
		var fileDir = switch (factoryType) {
			case AbstractSQLFactoryDAO.MYSQL -> MY_SQL_QUERY_DIRECTORY;
			default -> throw new IllegalArgumentException("Invalid factory type: " + factoryType);
		};
		var key = SQL_QUERY_DIRECTORY + fileDir + queryType.toString() + "/" + fileName;

		if (queryCacheHashMap.containsKey(key)) {
			return queryCacheHashMap.get(key);
		}

		var filePath = key + SQL_QUERY_FILE_EXTENSION;

		try (var inputStream = getClass().getResourceAsStream(filePath)) {
			if (inputStream == null) {
				throw new FileNotFoundException("SQL query file not found: " + filePath);
			}

			var query = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			queryCacheHashMap.put(key, query);

			return query;
		} catch (IOException e) {
			LOGGER.severe("Failed to load SQL query: " + filePath);
		} catch (OutOfMemoryError e) {
			LOGGER.severe("Out of memory while loading SQL query: " + filePath);
		}

		return null;
	}
}
