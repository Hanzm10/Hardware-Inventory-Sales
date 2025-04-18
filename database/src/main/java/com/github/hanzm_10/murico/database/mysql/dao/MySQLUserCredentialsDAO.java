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
package com.github.hanzm_10.murico.database.mysql.dao;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.database.AbstractSQLFactoryDAO;
import com.github.hanzm_10.murico.database.dao.UserCredentialsDAO;
import com.github.hanzm_10.murico.database.mysql.MySQLFactoryDAO;
import com.github.hanzm_10.murico.database.query.SQLQueryCache;
import com.github.hanzm_10.murico.database.query.SQLQueryCache.SQLQueryType;

public class MySQLUserCredentialsDAO implements UserCredentialsDAO {

	@Override
	public String getUserPasswordByUserDisplayName(@NotNull String _userDisplayName) {
		String userPassword = null;

		try (var conn = MySQLFactoryDAO.createConnection()) {
			var query = SQLQueryCache.getInstance().getQuery("user_credentials_user_password_by_display_name",
					AbstractSQLFactoryDAO.MYSQL, SQLQueryType.SELECT);
			var statement = conn.prepareStatement(query);
			statement.setString(1, _userDisplayName);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				userPassword = resultSet.getString("user_password");
			}

			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userPassword;
	}
}
