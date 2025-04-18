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

import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.database.dao.SessionDAO;
import com.github.hanzm_10.murico.database.dao.UserCredentialsDAO;
import com.github.hanzm_10.murico.database.dao.UserDAO;
import com.github.hanzm_10.murico.database.mysql.MySQLFactoryDAO;

/**
 * This class is responsible for creating the appropriate SQL factory DAO based
 * on the database type. It uses a factory method pattern to create the
 * appropriate instance.
 *
 * @see <a href=
 *      "https://www.oracle.com/java/technologies/dataaccessobject.html">DAO
 *      Pattern by Oracle</a>
 */
public abstract class AbstractSQLFactoryDAO {
	public static final int MYSQL = 1;

	public static AbstractSQLFactoryDAO getSQLFactoryDAO(@Range(from = 1, to = 1) int type) {
		return switch (type) {
			case MYSQL -> new MySQLFactoryDAO();
			default -> throw new IllegalArgumentException("Invalid database type: " + type);
		};
	}

	public abstract SessionDAO getSessionDAO();

	public abstract UserCredentialsDAO getUserCredentialsDAO();

	public abstract UserDAO getUserDAO();
}
