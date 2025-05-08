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
package com.github.hanzm_10.murico.swingapp.lib.database;

import java.util.logging.Logger;

import com.github.hanzm_10.murico.swingapp.lib.database.dao.AccountDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.ItemDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SessionDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

/**
 * This class is responsible for creating the appropriate SQL factory DAO based
 * on the database type. It uses a factory method pattern to create the
 * appropriate instance. <br>
 * <br>
 * Example usage of the AbstractSqlFactoryDao to retrieve a platform-specific
 * DAO implementation.
 *
 * <p>
 * This snippet demonstrates how to obtain a MySQL-specific factory instance
 * using the factory method {@link AbstractSqlFactoryDao#getSqlFactoryDao(int)},
 * and then retrieve the {@link SessionDao} to perform a data access operation.
 * This promotes loose coupling between database-specific implementations and
 * the business logic, making the codebase easier to maintain and extend for
 * other database platforms.
 *
 * <pre>
 * // Step 1: Get the factory for the specified database type (MySQL in this
 * // case)
 * var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
 *
 * // Step 2: Retrieve the SessionDao implementation from the factory
 * var sessionDao = factory.getSessionDao();
 *
 * // Step 3: Use the Dao to perform operations, such as checking if a session
 * // exists
 * var sessionExists = sessionDao.sessionExists(sessionUid);
 * </pre>
 *
 * <p>
 * This design allows easy substitution of different database backends by
 * changing only the factory input type, without modifying the business logic
 * that relies on the DAO interfaces.
 *
 * @see <a href=
 *      "https://www.oracle.com/java/technologies/dataaccessobject.html">Article
 *      about DAO Pattern by Oracle</a>
 */
public abstract class AbstractSqlFactoryDao {
	protected static final Logger LOGGER = MuricoLogger.getLogger(AbstractSqlFactoryDao.class);
	public static final int MYSQL = 1;

	public static AbstractSqlFactoryDao getSqlFactoryDao(int type) {
		return switch (type) {
		case MYSQL -> new MySqlFactoryDao();
		default -> throw new IllegalArgumentException("Invalid database type: " + type);
		};
	}

	public abstract AccountDao getAccountDao();

	public abstract ItemDao getItemDao();

	public abstract SessionDao getSessionDao();

	public abstract UserDao getUserDao();
}