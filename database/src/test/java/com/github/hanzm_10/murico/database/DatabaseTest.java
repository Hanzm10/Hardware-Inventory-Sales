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
package com.github.hanzm_10.murico.database;

import java.sql.SQLException;

import com.github.hanzm_10.murico.database.mysql.MySQLFactoryDAO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DatabaseTest {
	@Test
	@DisplayName("Test the database connection")
	public void testDatabaseConnection() throws SQLException {
		// Test the database connection
		var connection = MySQLFactoryDAO.createConnection();
		assert connection != null : "Database connection should not be null";
		connection.close();
	}

	@Test
	@DisplayName("Print session table")
	public void testPrintSessionTable() throws SQLException {
		// Test if the session exists
		var factory = AbstractSQLFactoryDAO.getSQLFactoryDAO(AbstractSQLFactoryDAO.MYSQL);
		var session = factory.getSessionDAO();
		session.printSessionTable();
	}

	@Test
	@DisplayName("Test session shouldn't exist")
	public void testSessionExists() throws SQLException {
		// Test if the session exists
		var factory = AbstractSQLFactoryDAO.getSQLFactoryDAO(AbstractSQLFactoryDAO.MYSQL);
		var session = factory.getSessionDAO();
		var sessionExists = session.sessionExists("testSession");

		assert !sessionExists : "Session should not exist";
	}
}
