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
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SessionDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.session.Session;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.session.SessionStatus;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class MySqlSessionDao implements SessionDao {
	private static final Logger LOGGER = MuricoLogger.getLogger(MySqlSessionDao.class);

	@Override
	public @NotNull Session createSession(@NotNull User user) throws IOException, SQLException {
		return createSession(user, "", "");
	}

	@Override
	public @NotNull Session createSession(@NotNull User user, String ipAddress) throws IOException, SQLException {
		return createSession(user, ipAddress, "");
	}

	@Override
	public @NotNull Session createSession(@NotNull User user, String ipAddress, String userAgent)
			throws IOException, SQLException {
		Session session = null;
		String query = MySqlQueryLoader.getInstance().get("create_session", "sessions", SqlQueryType.INSERT);

		try (var conn = MySqlFactoryDao.createConnection();
				var statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {

			statement.setInt(1, user._userId());
			statement.setString(2, ipAddress);
			statement.setString(3, userAgent);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				session = new Session(resultSet.getInt("_session_id"), resultSet.getInt("_user_id"),
						resultSet.getString("_session_token"), resultSet.getTimestamp("_created_at"),
						resultSet.getTimestamp("expires_at"), resultSet.getTimestamp("updated_at"),
						resultSet.getString("ip_address"), resultSet.getString("user_agent"),
						SessionStatus.fromString(resultSet.getString("status")),
						resultSet.getTimestamp("status_updated_at"));
			}

			statement.close();
			conn.close();
		} catch (SQLFeatureNotSupportedException e) {
			LOGGER.log(Level.SEVERE, "SQL feature not supported", e);
		}

		return session;
	}

	@Override
	public Session getSessionByToken(@NotNull String _sessionUid) throws IOException, SQLException {
		Session session = null;
		var query = MySqlQueryLoader.getInstance().get("select_by_token", "sessions", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setString(1, _sessionUid);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				session = new Session(resultSet.getInt("_session_id"), resultSet.getInt("_user_id"),
						resultSet.getString("_session_token"), resultSet.getTimestamp("_created_at"),
						resultSet.getTimestamp("expires_at"), resultSet.getTimestamp("updated_at"),
						resultSet.getString("ip_address"), resultSet.getString("user_agent"),
						SessionStatus.fromString(resultSet.getString("status")),
						resultSet.getTimestamp("status_updated_at"));
			}

			statement.close();
			conn.close();
		}

		return session;
	}

	@Override
	public void printSessionTableOfUser(@NotNull User user) throws IOException, SQLException {
		// TODO: Implement this method
	}

	@Override
	public boolean sessionExists(@NotNull String _sessionUid) throws IOException, SQLException {
		var sessionExists = false;
		String query = MySqlQueryLoader.getInstance().get("select_exists_by_token", "sessions", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setString(1, _sessionUid);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				sessionExists = resultSet.getInt(1) != 0;
			}

			statement.close();
			conn.close();
		}

		return sessionExists;
	}
}
