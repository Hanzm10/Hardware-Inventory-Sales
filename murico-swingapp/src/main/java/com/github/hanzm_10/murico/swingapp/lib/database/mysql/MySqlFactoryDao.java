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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.hanzm_10.murico.swingapp.constants.PropertyKey;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.AccountDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.CategoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.ItemDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.PackagingDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.RoleDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SalesDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SessionDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SupplierDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySQLSalesDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySqlAccountDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySqlCategoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySqlItemDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySqlPackagingDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySqlRoleDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySqlSessionDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySqlSupplierDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql.MySqlUserDao;
import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;

// Consider adding your logger import if needed elsewhere too
// import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public final class MySqlFactoryDao extends AbstractSqlFactoryDao {
	// Use a standard logger instance
	private static final Logger LOGGER = Logger.getLogger(MySqlFactoryDao.class.getName());

	public static String DB_URL;
	public static String DB_USER;
	public static String DB_PASSWORD;
	public static String DB_NAME; // This doesn't seem to be used in createConnection

	static {
		var properties = new Properties();
		// Consider setting logger level here if needed globally
		// LOGGER.setLevel(Level.ALL);

		try {
			PropertiesIO.loadProperties(MySqlFactoryDao.class, properties, "config");

			DB_URL = properties.getProperty(PropertyKey.Database.DB_URL);
			DB_USER = properties.getProperty(PropertyKey.Database.DB_USER);
			DB_PASSWORD = properties.getProperty(PropertyKey.Database.DB_PASSWORD);
			DB_NAME = properties.getProperty(PropertyKey.Database.DB_NAME); // Loaded but not used below?

			// Log loaded properties (be careful with password in logs)
			LOGGER.info("Database properties loaded.");
			LOGGER.fine("DB URL: " + DB_URL);
			LOGGER.fine("DB User: " + DB_USER);
			// Avoid logging password directly: LOGGER.fine("DB Password: " + DB_PASSWORD);
			LOGGER.fine("DB Name: " + DB_NAME);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to load MySQL properties file from 'config.properties'", e);
			// Throwing RuntimeException might be too harsh depending on app needs
			throw new RuntimeException("Failed to load MySQL properties file", e);
		} catch (IllegalArgumentException e) {
			// This usually means the properties file itself wasn't found
			LOGGER.log(Level.SEVERE, "'config.properties' file not found in classpath.", e);
			throw new RuntimeException("'config.properties' file not found.", e);
		} catch (NullPointerException e) {
			// This might happen if a required property key is missing
			LOGGER.log(Level.SEVERE,
					"A required database property (URL, User, Password, Name) is missing in" + " 'config.properties'",
					e);
			throw new RuntimeException("Missing required database property.", e);
		}
	}

	/**
	 * Safely closes the given database connection. Checks for null and if the
	 * connection is already closed. Logs any SQLException that occurs during
	 * closing.
	 *
	 * @param connection The Connection object to close.
	 */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
					LOGGER.finer("Database connection closed successfully."); // Use finer for routine closes
				} else {
					// Optional: Log if trying to close an already closed connection
					LOGGER.finest("Attempted to close an already closed connection.");
				}
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "Failed to close database connection.", e);
				// Don't re-throw usually, as closing errors are often secondary
			}
		} else {
			// Optional: Log if null connection was passed
			LOGGER.finest("Attempted to close a null connection.");
		}
	}

	/**
	 * Creates a new database connection using the loaded properties.
	 *
	 * @return A new Connection object.
	 * @throws SQLException        If a database access error occurs.
	 * @throws SQLTimeoutException If the driver has determined that the timeout
	 *                             value has been exceeded.
	 * @throws RuntimeException    If the JDBC driver class cannot be found.
	 */
	public static final Connection createConnection() throws SQLException, SQLTimeoutException {
		// Ensure driver is loaded (optional for modern JDBC, but good practice)
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "MySQL JDBC Driver (com.mysql.cj.jdbc.Driver) not found in classpath.", e);
			throw new RuntimeException("MySQL JDBC Driver not found", e);
		}

		// Check if properties were loaded successfully
		if (DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
			throw new SQLException("Database connection properties are not initialized. Check config.properties.");
		}

		LOGGER.fine("Attempting to connect to database: " + DB_URL);
		// Consider adding connection properties (SSL, timeouts, etc.) if needed
		// Properties props = new Properties();
		// props.setProperty("user", DB_USER);
		// props.setProperty("password", DB_PASSWORD);
		// props.setProperty("useSSL", "true"); // Example
		// return DriverManager.getConnection(DB_URL, props);
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	@Override
	public AccountDao getAccountDao() {
		return new MySqlAccountDao();
	}

	@Override
	public CategoryDao getCategoryDao() {
		return new MySqlCategoryDao();
	}

	@Override
	public ItemDao getItemDao() {
		return new MySqlItemDao();
	}

	@Override
	public PackagingDao getPackagingDao() {
		return new MySqlPackagingDao();
	}

	@Override
	public RoleDao getRoleDao() {
		return new MySqlRoleDao();
	}

	@Override
	public SalesDao getSalesDao() {
		// TODO Auto-generated method stub
		return new MySQLSalesDao();
	}

	// --- Instance methods for DAO retrieval ---
	@Override
	public SessionDao getSessionDao() {
		return new MySqlSessionDao();
	}

	@Override
	public SupplierDao getSupplierDao() {
		// TODO Auto-generated method stub
		return new MySqlSupplierDao();
	}

	@Override
	public UserDao getUserDao() {
		return new MySqlUserDao();
	}
}
