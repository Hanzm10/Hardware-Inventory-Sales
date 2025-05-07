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
package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserGender;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;

public class MySqlUserDao implements UserDao {

	@Override
	public User getUserByDisplayName(@NotNull String _userDisplayName) throws IOException, SQLException {
		User user = null;
		var query = MySqlQueryLoader.getInstance().get("get_user_by_display_name", "users", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setString(1, _userDisplayName);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				user = new User(resultSet.getInt("_user_id"), resultSet.getTimestamp("_created_at"),
						resultSet.getTimestamp("updated_at"), resultSet.getString("display_name"),
						resultSet.getString("display_image"), UserGender.fromString(resultSet.getString("gender")),
						resultSet.getString("first_name"), resultSet.getString("last_name"),
						resultSet.getString("biography"));
			}

			statement.close();
			conn.close();
		}

		return user;
	}

	@Override
	public User getUserByEmail(@NotNull String _userEmail) throws IOException, SQLException {
		User user = null;
		var query = MySqlQueryLoader.getInstance().get("get_user_by_email", "users", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setString(1, _userEmail);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				user = new User(resultSet.getInt("_user_id"), resultSet.getTimestamp("_created_at"),
						resultSet.getTimestamp("updated_at"), resultSet.getString("display_name"),
						resultSet.getString("display_image"), UserGender.fromString(resultSet.getString("gender")),
						resultSet.getString("first_name"), resultSet.getString("last_name"),
						resultSet.getString("biography"));
			}

			statement.close();
			conn.close();
		}
		return user;
	}

	@Override
	public User getUserById(@Range(from = 0, to = 2147483647) int _userID) throws IOException, SQLException {
		User user = null;
		var query = MySqlQueryLoader.getInstance().get("get_user_by_id", "users", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setInt(1, _userID);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				user = new User(resultSet.getInt("_user_id"), resultSet.getTimestamp("_created_at"),
						resultSet.getTimestamp("updated_at"), resultSet.getString("display_name"),
						resultSet.getString("display_image"), UserGender.fromString(resultSet.getString("gender")),
						resultSet.getString("first_name"), resultSet.getString("last_name"),
						resultSet.getString("biography"));
			}

			statement.close();
			conn.close();
		}
		return user;
	}

	@Override
	public boolean isUsernameTaken(@NotNull String name) throws IOException, SQLException {
		var usernameTaken = false;
		var query = MySqlQueryLoader.getInstance().get("is_username_taken", "users", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setString(1, name);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				usernameTaken = resultSet.getInt(1) != 0;
			}

			statement.close();
			conn.close();
		}

		return usernameTaken;
	}
}
