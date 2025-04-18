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
package com.github.hanzm_10.murico.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import com.github.hanzm_10.murico.database.AbstractSQLFactoryDAO;
import com.github.hanzm_10.murico.database.dao.SessionDAO;
import com.github.hanzm_10.murico.database.dao.UserCredentialsDAO;
import com.github.hanzm_10.murico.database.dao.UserDAO;
import com.github.hanzm_10.murico.database.mysql.dao.MySQLSessionDAO;
import com.github.hanzm_10.murico.database.mysql.dao.MySQLUserCredentialsDAO;
import com.github.hanzm_10.murico.database.mysql.dao.MySQLUserDAO;
import com.github.hanzm_10.murico.utils.MuricoLogUtils;

import com.github.weisj.darklaf.properties.PropertyLoader;

public class MySQLFactoryDAO extends AbstractSQLFactoryDAO {
	private static final Logger LOGGER = MuricoLogUtils.getLogger(MySQLFactoryDAO.class);

	public static final String DB_URL;
	public static final String DB_USER;
	public static final String DB_PASSWORD;

	static {
		var properties = PropertyLoader.loadProperties(MySQLFactoryDAO.class, "MySQLDatabase", "/");

		DB_URL = properties.getProperty("url");
		DB_USER = properties.getProperty("user");
		DB_PASSWORD = properties.getProperty("password");

		LOGGER.info("Loaded MySQL Database URL: " + DB_URL);
	}

	public static Connection createConnection() {
		try {
			var conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			LOGGER.info("MySQL connection created successfully.");

			return conn;
		} catch (Exception e) {
			LOGGER.severe("Failed to create MySQL connection: " + e.getMessage());
		}

		return null;
	}

	@Override
	public SessionDAO getSessionDAO() {
		// TODO Auto-generated method stub
		return new MySQLSessionDAO();
	}

	@Override
	public UserCredentialsDAO getUserCredentialsDAO() {
		return new MySQLUserCredentialsDAO();
	}

	@Override
	public UserDAO getUserDAO() {
		// TODO Auto-generated method stub
		return new MySQLUserDAO();
	}
}
